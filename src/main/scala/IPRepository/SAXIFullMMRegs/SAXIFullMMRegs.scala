package IPRepository.SAXIFullMMRegs

import Interfaces.{AXIFull, Immediate}
import chisel3._
import chisel3.util.log2Ceil

class SAXIFullMMRegs(regCount: Int, regSize: Int) extends Module {

  assert(regCount >= 1)
  assert(List(1, 2, 4, 8, 16, 32, 64, 128).contains(regSize))

  private val ADDR_WIDTH = log2Ceil(regCount) + log2Ceil(regSize)
  private val INNER_ADDR_WIDTH = log2Ceil(regCount)
  private val ADDR_MBITS = ADDR_WIDTH - 1
  private val ADDR_LBITS = log2Ceil(regSize)
  private val DATA_WIDTH = regSize * 8

  val io = IO(new Bundle {
    val s_axi = AXIFull(ADDR_WIDTH, regSize)
    val w = Immediate(new Bundle {
      val addr = UInt(INNER_ADDR_WIDTH.W)
      val data = UInt(DATA_WIDTH.W)
      val strb = UInt(regSize.W)
    })
    val r = new Bundle {
      val valid = Output(Bool())
      val bits = new Bundle {
        val addr = Output(UInt(INNER_ADDR_WIDTH.W))
        val data = Input(UInt(DATA_WIDTH.W))
      }
    }
  })

  // axi write addr
  val writeAddrReady = RegInit(true.B)
  val writeAddr = RegInit(0.U(INNER_ADDR_WIDTH.W))

  when (io.s_axi.awvalid & io.s_axi.awready) {
    writeAddrReady := false.B
  } .elsewhen(io.s_axi.bvalid & io.s_axi.bready) {
    writeAddrReady := true.B
  }

  when (io.s_axi.awvalid & io.s_axi.awready) {
    writeAddr := io.s_axi.awaddr(ADDR_MBITS, ADDR_LBITS)
  } .elsewhen(io.s_axi.wvalid & io.s_axi.wready) {
    writeAddr := writeAddr + 1.U
  }

  io.s_axi.awready := writeAddrReady


  // axi write data
  val writeDataReady = RegInit(false.B)
  val writeLast = RegInit(false.B)
  val writeDataExp = RegInit(0.U(8.W))
  val writeDataAct = RegInit(0.U(8.W))

  when (writeDataExp === writeDataAct & io.s_axi.wvalid & io.s_axi.wready) {
    writeDataReady := false.B
  } .elsewhen(io.s_axi.awvalid & io.s_axi.awready) {
    writeDataReady := true.B
  }

  when (writeDataAct + 1.U === writeDataExp & io.s_axi.wvalid & io.s_axi.wready) {
    writeLast := true.B
  } .elsewhen(io.s_axi.awvalid & io.s_axi.awready & io.s_axi.awlen === 0.U) {
    writeLast := false.B
  }

  when (io.s_axi.awvalid & io.s_axi.awready) {
    writeDataExp := io.s_axi.awlen
  }

  when (io.s_axi.wvalid & io.s_axi.wready) {
    writeDataAct := writeDataAct + 1.U
  } .elsewhen(io.s_axi.bvalid & io.s_axi.bready) {
    writeDataAct := 0.U
  }

  io.s_axi.wready := writeDataReady
  io.s_axi.wlast := writeLast


  // write response
  val writeResponse = RegInit(false.B)

  when (writeDataExp === writeDataAct & io.s_axi.wvalid & io.s_axi.wready) {
    writeResponse := true.B
  } .elsewhen(io.s_axi.bvalid & io.s_axi.bready) {
    writeResponse := false.B
  }

  io.s_axi.bvalid := writeResponse
  io.s_axi.bresp := 0.U


  // write inner
  io.w.bits.addr := writeAddr
  io.w.bits.data := io.s_axi.wdata
  io.w.bits.strb := io.s_axi.wstrb
  io.w.valid := io.s_axi.wvalid & io.s_axi.wready


  // read addr
  val readAddr = RegInit(0.U(INNER_ADDR_WIDTH.W))
  val readAddrReady = RegInit(true.B)

  when (io.s_axi.arvalid & io.s_axi.arready) {
    readAddrReady := false.B
  } .elsewhen(io.s_axi.rvalid & io.s_axi.rready & io.s_axi.rlast) {
    readAddrReady := true.B
  }

  when (io.s_axi.arvalid & io.s_axi.arready) {
    readAddr := io.s_axi.araddr(ADDR_MBITS, ADDR_LBITS)
  } .elsewhen(io.r.valid) {
    readAddr := readAddr + 1.U
  }

  io.s_axi.arready := readAddrReady


  // read data
  val readData = RegInit(0.U(DATA_WIDTH.W))
  val readDataValid = RegInit(false.B)
  val readFirst = RegInit(false.B)

  when (io.s_axi.arvalid & io.s_axi.arready) {
    readFirst := true.B
  } .otherwise {
    readFirst := false.B
  }

  when (io.r.valid) {
    readDataValid := true.B
  } .elsewhen(io.s_axi.rvalid & io.s_axi.rready) {
    readDataValid := false.B
  }

  when (io.r.valid) {
    readData := io.r.bits.data
  }

  io.s_axi.rdata := readData
  io.s_axi.rvalid := readDataValid


  // inner read
  io.r.bits.addr := readAddr
  io.r.valid := readFirst | (io.s_axi.rvalid & io.s_axi.rready & !io.s_axi.rlast)
}