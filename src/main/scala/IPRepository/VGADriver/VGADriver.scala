package IPRepository.VGADriver


import Interfaces.Immediate
import chisel3._
import chisel3.util.Irrevocable

class VGADriver(colorWidth: Int, resolution: Int) extends Module {

  assert(resolution == 720 | resolution == 1080)

  private val resolutionMap = Map(
    720 -> Map(
      "H_ACTIVE" -> 1280,
      "H_FP" -> 110,
      "H_SYNC" -> 40,
      "H_BP" -> 220,
      "H_TOTAL" -> 1650,
      "V_ACTIVE" -> 720,
      "V_FP" -> 5,
      "V_SYNC" -> 5,
      "V_BP" -> 20,
      "V_TOTAL" -> 750
    ),
    1080 -> Map(
      "H_ACTIVE" -> 1920,
      "H_FP" -> 88,
      "H_SYNC" -> 44,
      "H_BP" -> 148,
      "H_TOTAL" -> 2200,
      "V_ACTIVE" -> 1080,
      "V_FP" -> 4,
      "V_SYNC" -> 5,
      "V_BP" -> 36,
      "V_TOTAL" -> 1125
    )
  )

  private val H_ACTIVE = resolutionMap(resolution)("H_ACTIVE")
  private val H_FP = resolutionMap(resolution)("H_FP")
  private val H_SYNC = resolutionMap(resolution)("H_FP")
  private val H_BP = resolutionMap(resolution)("H_FP")
  private val H_TOTAL = resolutionMap(resolution)("H_TOTAL")
  private val V_ACTIVE = resolutionMap(resolution)("V_ACTIVE")
  private val V_FP = resolutionMap(resolution)("V_FP")
  private val V_SYNC = resolutionMap(resolution)("V_SYNC")
  private val V_BP = resolutionMap(resolution)("V_BP")
  private val V_TOTAL = resolutionMap(resolution)("V_TOTAL")

  private val H_COUNTER_WIDTH = util.log2Ceil(H_TOTAL)
  private val V_COUNTER_WIDTH = util.log2Ceil(V_TOTAL)


  val io = IO(new Bundle {
    val src_color = Flipped(Irrevocable(UInt(colorWidth.W)))
    val dst_color = Immediate(UInt(colorWidth.W))
    val error = Output(Bool())
    val hsync = Output(Bool())
    val vsync = Output(Bool())
  })


  // vertical and horizontal counter logic
  val hCounter = RegInit(0.U(H_COUNTER_WIDTH.W))
  val vCounter = RegInit(0.U(V_COUNTER_WIDTH.W))

  when (hCounter === (H_TOTAL - 1).U) {
    hCounter := 0.U
  } .otherwise {
    hCounter := hCounter + 1.U
  }

  when (hCounter === (H_TOTAL - 1).U) {
    when (vCounter === (V_TOTAL - 1).U) {
      vCounter := 0.U
    } .otherwise {
      vCounter := vCounter + 1.U
    }
  }

  // sync logic
  val vSync = RegInit(false.B)
  val hSync = RegInit(false.B)
  val hSyncWillBegin = RegInit(false.B)
  val vSyncWillBegin = RegInit(false.B)
  val hSyncWillEnd = RegInit(false.B)
  val vSyncWillEnd = RegInit(false.B)

  when (hCounter === (H_TOTAL - 2).U) {
    hSyncWillBegin := true.B
  } .otherwise {
    hSyncWillBegin := false.B
  }

  when (vCounter === (V_TOTAL - 1).U) {
    vSyncWillBegin := true.B
  } .otherwise {
    vSyncWillBegin := false.B
  }

  when (hCounter === (H_SYNC - 2).U) {
    hSyncWillEnd := true.B
  } .otherwise {
    hSyncWillEnd := false.B
  }

  when (vCounter === (V_SYNC - 1).U) {
    vSyncWillEnd := true.B
  } .otherwise {
    vSyncWillEnd := false.B
  }

  when (hSyncWillEnd) {
    hSync := true.B
  } .elsewhen(hSyncWillBegin) {
    hSync := false.B
  }

  when (hSyncWillBegin) {
    when (hSyncWillEnd) {
      vSync := true.B
    } .elsewhen(hSyncWillBegin) {
      vSync := false.B
    }
  }

  io.hsync := hSync
  io.vsync := vSync

  // active logic
  val hWillBeActive = RegInit(false.B)
  val vWillBeActive = RegInit(false.B)
  val hWillBeInactive = RegInit(false.B)
  val vWillBeInactive = RegInit(false.B)
  val videoActive = RegInit(false.B)

  when (hCounter === (H_SYNC + H_BP - 2).U) {
    hWillBeActive := true.B
  } .otherwise {
    hWillBeActive := false.B
  }

  when (vCounter === (V_SYNC + V_BP - 1).U) {
    vWillBeActive := true.B
  } .otherwise {
    vWillBeActive := false.B
  }

  when (hCounter === (H_BP + H_SYNC + H_ACTIVE).U) {
    hWillBeInactive := true.B
  } .otherwise {
    hWillBeInactive := false.B
  }

  when (vCounter === (V_SYNC + V_BP + V_ACTIVE - 1).U) {
    vWillBeInactive := true.B
  } .otherwise {
    vWillBeInactive := false.B
  }

  when (hWillBeActive & vWillBeActive) {
    videoActive := true.B
  } .elsewhen(hWillBeInactive & vWillBeInactive) {
    videoActive := false.B
  }

  io.dst_color.bits := io.src_color.bits
  io.dst_color.valid := videoActive
  io.src_color.ready := videoActive

  // pixel error
  val error = RegInit(false.B)

  when (io.src_color.ready & !io.src_color.valid) {
    error := true.B
  }

  io.error := error
}

object VGADriver extends App {
  Driver.execute(
    Array("-td", Support.MyPath.getVerilogDir(VGADriver.getClass)),
    () => new VGADriver(24, 720)
  )
}
