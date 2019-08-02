package Interfaces

import chisel3._

class AxiFull(val addrWidth: Int, val dataSize: Int, val IDWidth: Int, val userWidth: Int) extends Bundle {

  require(dataSize > 0)
  require(addrWidth > 0)

  private val DATA_WIDTH = dataSize * 8

  val awvalid = Output(Bool())
  val awready = Input(Bool())
  val awaddr = Output(UInt(addrWidth.W))
  val awburst = Output(UInt(2.W))
  val awsize = Output(UInt(3.W))
  val awlen = Output(UInt(8.W))
  val awcache = Output(UInt(4.W))
  val awlock = Output(UInt(1.W))
  val awprot = Output(UInt(3.W))
  val awqos = Output(UInt(4.W))
  val awregion = Output(UInt(4.W))
  val awid = if (IDWidth > 0) Some(Output(UInt(IDWidth.W))) else None
  val awuser = if (userWidth > 0) Some(Output(UInt(userWidth.W))) else None

  val wvalid = Output(Bool())
  val wready = Input(Bool())
  val wdata = Output(UInt(DATA_WIDTH.W))
  val wstrb = Output(UInt(dataSize.W))
  val wlast = Output(Bool())
  val wid = Output(UInt(IDWidth.W))
  val wuser = Output(UInt(userWidth.W))

  val bvalid = Input(Bool())
  val bready = Output(Bool())
  val bresp = Input(UInt(2.W))
  val bid = if (IDWidth > 0) Some(Input(UInt(IDWidth.W))) else None
  val buser = if (userWidth > 0) Some(Input(UInt(userWidth.W))) else None

  val arvalid = Output(Bool())
  val arready = Input(Bool())
  val araddr = Output(UInt(addrWidth.W))
  val arburst = Output(UInt(2.W))
  val arsize = Output(UInt(3.W))
  val arlen = Output(UInt(8.W))
  val arcache = Output(UInt(4.W))
  val arlock = Output(UInt(1.W))
  val arprot = Output(UInt(3.W))
  val arqos = Output(UInt(4.W))
  val arregion = Output(UInt(4.W))
  val arid = if (IDWidth > 0) Some(Output(UInt(IDWidth.W))) else None
  val aruser = if (userWidth > 0) Some(Output(UInt(userWidth.W))) else None

  val rvalid = Input(Bool())
  val rready = Output(Bool())
  val rdata = Input(UInt(DATA_WIDTH.W))
  val rresp = Input(UInt(2.W))
  val rlast = Input(Bool())
  val rid = if (IDWidth > 0) Some(Input(UInt(IDWidth.W))) else None
  val ruser = if (userWidth > 0) Some(Input(UInt(userWidth.W))) else None
}
