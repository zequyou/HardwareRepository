package IPRepository.SAXILiteMMRegs

import Interfaces.{AXILite, Immediate}
import Support.MyPath
import chisel3._
import chisel3.util.log2Ceil

class SAXILiteMMRegs(regCount: Int, regSize: Int) extends Module {

  assert(regCount >= 1)
  assert(regSize == 4 || regSize == 8)

  private val ADDR_WIDTH = log2Ceil(regCount) + log2Ceil(regSize)
  private val INNER_ADDR_WIDTH = log2Ceil(regCount)
  private val ADDR_MBITS = ADDR_WIDTH - 1
  private val ADDR_LBITS = log2Ceil(regSize)
  private val DATA_WIDTH = regSize * 8

  val io = IO(new Bundle {
    val s_axi = AXILite(ADDR_WIDTH, regSize)
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

  // axi write addr and data
  val writeAddr = RegInit(0.U(ADDR_WIDTH.W))
  val writeData = RegInit(0.U(DATA_WIDTH.W))
  val writeAddrReady = RegInit(true.B)
  val writeDataReady = RegInit(true.B)

  when(io.s_axi.awvalid & io.s_axi.awready) {
    writeAddr := io.s_axi.awaddr
  }

  when(io.s_axi.awvalid & io.s_axi.awready) {
    writeAddrReady := false.B
  }.elsewhen(io.s_axi.bvalid & io.s_axi.bready) {
    writeAddrReady := true.B
  }

  when(io.s_axi.wvalid & io.s_axi.wready) {
    writeData := io.s_axi.wdata
  }

  when(io.s_axi.wvalid & io.s_axi.wready) {
    writeDataReady := false.B
  }.elsewhen(io.s_axi.bvalid & io.s_axi.bready) {
    writeDataReady := true.B
  }

  io.s_axi.awready := writeAddrReady
  io.s_axi.wready := writeDataReady
  io.w.bits.strb := io.s_axi.wstrb

  // axi write response
  val writeAddrComplete: Bool = Wire(Bool())
  val writeDataComplete: Bool = Wire(Bool())
  val writeComplete: Bool = Wire(Bool())
  val writeRespValid = RegInit(false.B)

  writeAddrComplete := (io.s_axi.awvalid & io.s_axi.awready) | !writeAddrReady
  writeDataComplete := (io.s_axi.wvalid & io.s_axi.wready) | !writeDataReady
  writeComplete := writeAddrComplete & writeDataComplete

  when(writeComplete) {
    writeRespValid := true.B
  }.elsewhen(io.s_axi.bvalid & io.s_axi.bready) {
    writeRespValid := false.B
  }

  io.s_axi.bresp := 0.U
  io.s_axi.bvalid := writeRespValid

  // write out
  val writeValid = RegInit(false.B)

  when (io.s_axi.bvalid & io.s_axi.bready) {
    writeValid := true.B
  } .otherwise {
    writeValid := false.B
  }

  io.w.bits.addr := writeAddr(ADDR_MBITS, ADDR_LBITS)
  io.w.bits.data := writeData
  io.w.valid := writeValid


  // axi read addr
  val readAddrReady = RegInit(true.B)

  when (io.s_axi.arvalid & io.s_axi.arready) {
    readAddrReady := false.B
  } .elsewhen(io.s_axi.rvalid & io.s_axi.rready) {
    readAddrReady := true.B
  }

  io.s_axi.arready := readAddrReady

  // axi read data
  val readDataValid = RegInit(true.B)
  val readData = RegInit(UInt(DATA_WIDTH.W))

  when (io.s_axi.arvalid & io.s_axi.arready) {
    readDataValid := true.B
  } .elsewhen(io.s_axi.rvalid & io.s_axi.rready) {
    readDataValid := false.B
  }

  when (io.r.valid) {
    readData := io.s_axi.rdata
  }

  io.r.valid := io.s_axi.arvalid & io.s_axi.arready
  io.r.bits.addr := io.s_axi.araddr(ADDR_MBITS, ADDR_LBITS)

  io.s_axi.rvalid := readDataValid
  io.s_axi.rdata := readData
}

object SAXILiteMMRegs extends App {
  chisel3.Driver.execute(Array("-td", MyPath.getVerilogDir(SAXILiteMMRegs.getClass)),
    () => new SAXILiteMMRegs(32, 32))
}