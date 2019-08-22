
package Interfaces

import chisel3._

class AXILiteIO(addrWidth: Int, dataSize: Int) extends Bundle {

  val awvalid = Output(Bool())
  val awready = Input(Bool())
  val awaddr = Output(UInt(addrWidth.W))

  val wvalid = Output(Bool())
  val wready = Input(Bool())
  val wdata = Output(UInt((dataSize * 8).W))
  val wstrb = Output(UInt(dataSize.W))

  val bready = Output(Bool())
  val bvalid = Input(Bool())
  val bresp = Input(UInt(2.W))

  val arvalid = Output(Bool())
  val arready = Input(Bool())
  val araddr = Output(UInt(addrWidth.W))

  val rvalid = Input(Bool())
  val rready = Output(Bool())
  val rdata = Input(UInt((dataSize * 8).W))
  val rresp = Input(UInt(2.W))
}
