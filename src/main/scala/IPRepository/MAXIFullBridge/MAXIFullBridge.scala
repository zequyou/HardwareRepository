package IPRepository.MAXIFullBridge

import Interfaces.{AxiFull, AxiFullSimplified}
import Support.MyMath._
import chisel3._

class MAXIFullBridge(val dataSize: Int) extends Module {

  private val AXI_ADDR_WIDTH = 32
  private val AXI_DATA_WIDTH = dataSize * 8

  val io = IO(new Bundle {
    val m_axi = new AxiFull(AXI_ADDR_WIDTH, AXI_DATA_WIDTH, 0, 0)
    val m_axi_simplified = Flipped(new AxiFullSimplified(AXI_ADDR_WIDTH, dataSize))
  })

  val writing = RegInit(false.B)

  // write address
  val writeAddrComplete = RegInit(false.B)
  val awaddr: UInt = Wire(UInt(AXI_ADDR_WIDTH.W))
  val awburst: UInt = Wire(UInt(2.W))
  val awlen: UInt = Wire(UInt(8.W))

  awaddr := io.m_axi_simplified.wcmd.bits.addr
  awburst := io.m_axi_simplified.wcmd.bits.burst
  awlen := io.m_axi_simplified.wcmd.bits.len

  when (io.m_axi.awvalid & io.m_axi.awready) {
    writeAddrComplete := true.B
  } .elsewhen (!writing) {
    writeAddrComplete := false.B
  }

  io.m_axi.awvalid := Mux(writing, !writeAddrComplete, io.m_axi_simplified.wcmd.valid)
  io.m_axi_simplified.wcmd.ready := io.m_axi.bvalid
  io.m_axi.awaddr := awaddr
  io.m_axi.awburst := awburst
  io.m_axi.awsize := clog2(dataSize).U
  io.m_axi.awlen := awlen
  io.m_axi.awcache := 3.U
  io.m_axi.awlock := 0.U
  io.m_axi.awprot := 0.U
  io.m_axi.awqos := 0.U
  io.m_axi.awregion := 0.U

  // write data
  val wvalid: UInt = Wire(Bool())
  val wready: UInt = Wire(Bool())
  val wdata: UInt = Wire(UInt(AXI_DATA_WIDTH.W))
  val wstrb: UInt = Wire(UInt(dataSize.W))
  val wcounter = RegInit(0.U(8.W))
  val wdesired = RegInit(0.U(8.W))

  wvalid := io.m_axi_simplified.wdata.valid
  wready := io.m_axi.wready
  wdata := io.m_axi_simplified.wdata.bits.data
  wstrb := io.m_axi_simplified.wdata.bits.strb

  when(io.m_axi_simplified.wcmd.valid) {
    wdesired := io.m_axi_simplified.wcmd.bits.len
  }

  when (io.m_axi.wvalid & io.m_axi.wready) {
    wcounter := wcounter + 1.U
  } .elsewhen(!writing) {
    wcounter := 0.U
  }

  io.m_axi.wvalid := wvalid
  io.m_axi_simplified.wdata.ready := wready
  io.m_axi.wdata := wdata
  io.m_axi.wstrb := wstrb
  io.m_axi.wlast := Mux(writing, wcounter === wdesired, io.m_axi.awlen === 0.U)
  io.m_axi.wid := 0.U
  io.m_axi.wuser := 0.U

  // write response
  io.m_axi.bready := 1.U

  // writing
  when(io.m_axi.bvalid) {
    writing := false.B
  }.elsewhen(io.m_axi_simplified.wcmd.valid) {
    writing := true.B
  }

  // read address
  val readAddrNotComplete = RegInit(true.B)
  val araddr: UInt = Wire(UInt(AXI_ADDR_WIDTH.W))
  val arburst: UInt = Wire(UInt(2.W))
  val arlen: UInt = Wire(UInt(8.W))

  araddr := io.m_axi_simplified.rcmd.bits.addr
  arburst := io.m_axi_simplified.rcmd.bits.burst
  arlen := io.m_axi_simplified.rcmd.bits.len

  when (io.m_axi.arvalid & io.m_axi.arready) {
    readAddrNotComplete := false.B
  } .elsewhen(io.m_axi.rlast & io.m_axi.rvalid & io.m_axi.rready) {
    readAddrNotComplete := true.B
  }

  io.m_axi.arvalid := readAddrNotComplete & io.m_axi_simplified.rcmd.valid
  io.m_axi_simplified.rcmd.ready := io.m_axi.rlast & io.m_axi.rvalid & io.m_axi.rready
  io.m_axi.araddr := araddr
  io.m_axi.arburst := arburst
  io.m_axi.arsize := clog2(dataSize).U
  io.m_axi.arlen := arlen
  io.m_axi.arcache := 3.U
  io.m_axi.arlock := 0.U
  io.m_axi.arprot := 0.U
  io.m_axi.arqos := 0.U
  io.m_axi.arregion := 0.U

  // read data
  val rvalid: Bool = Wire(Bool())
  val rready: Bool = Wire(Bool())
  val rdata: UInt = Wire(UInt(AXI_DATA_WIDTH.W))

  rvalid := io.m_axi.rvalid
  rready := io.m_axi_simplified.rdata.ready
  rdata := io.m_axi.rdata

  io.m_axi_simplified.rdata.valid := rvalid
  io.m_axi.rready := rready
  io.m_axi_simplified.rdata.bits := rdata

}
