package IPRepository.MAXILiteBridge

import Interfaces.{AxiLite, AxiLiteSimplified}
import chisel3._

class MAXILiteBridge(val dataSize: Int = 4) extends Module {

  private val AXI_DATA_WIDTH = dataSize * 8
  private val AXI_ADDR_WIDTH = 32

  val io = IO(new Bundle{
    val m_axi = new AxiLite(AXI_ADDR_WIDTH, AXI_DATA_WIDTH)
    val m_axi_simplified = Flipped(new AxiLiteSimplified(AXI_ADDR_WIDTH, dataSize))
  })

  // write addr
  val writeAddrAcceptable = RegInit(true.B)
  val awaddr: UInt = Wire(UInt(AXI_ADDR_WIDTH.W))

  awaddr := io.m_axi_simplified.wr.bits.addr

  when (io.m_axi.awvalid & io.m_axi.awready) {
    writeAddrAcceptable := false.B
  } .elsewhen (io.m_axi.bready & io.m_axi.bvalid) {
    writeAddrAcceptable := true.B
  }

  io.m_axi.awvalid := writeAddrAcceptable & io.m_axi_simplified.wr.valid
  io.m_axi.awaddr := awaddr

  // write data
  val writeDataAcceptable = RegInit(true.B)
  val wdata: UInt = Wire(UInt(AXI_ADDR_WIDTH.W))
  val wstrb: UInt = Wire(UInt(dataSize.W))

  wdata := io.m_axi_simplified.wr.bits.data
  wstrb := io.m_axi_simplified.wr.bits.strb

  when (io.m_axi.wvalid & io.m_axi.wready) {
    writeDataAcceptable := false.B
  } .elsewhen(io.m_axi.bready & io.m_axi.bvalid) {
    writeDataAcceptable := true.B
  }

  io.m_axi.wvalid := writeDataAcceptable & io.m_axi_simplified.wr.valid
  io.m_axi.wdata := wdata
  io.m_axi.wstrb := wstrb

  // write response
  io.m_axi.bready := 1.U
  io.m_axi_simplified.wr.ready := io.m_axi.bvalid & io.m_axi.bready


  // read address
  val readAddrAcceptable = RegInit(true.B)

  when (io.m_axi.arvalid & io.m_axi.arready) {
    readAddrAcceptable := false.B
  } .elsewhen(io.m_axi.rvalid & io.m_axi.rready) {
    readAddrAcceptable := true.B
  }

  io.m_axi.arvalid := readAddrAcceptable & io.m_axi_simplified.rd.addr.valid
  io.m_axi.araddr := io.m_axi_simplified.rd.addr.bits
  io.m_axi.rready := io.m_axi_simplified.rd.addr.valid

  io.m_axi_simplified.rd.data := io.m_axi.rdata
  io.m_axi_simplified.rd.addr.ready := io.m_axi.rvalid
}
