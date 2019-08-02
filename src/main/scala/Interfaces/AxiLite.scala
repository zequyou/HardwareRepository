
package Interfaces

import chisel3._

class AxiLite(val addrWidth: Int, val dataWidth: Int) extends Bundle {
  require(dataWidth > 0)
  require(addrWidth > 0)

  val awvalid = Output(Bool())
  val awready = Input(Bool())
  val awaddr = Output(UInt(addrWidth.W))

  val wvalid = Output(Bool())
  val wready = Input(Bool())
  val wdata = Output(UInt(dataWidth.W))
  val wstrb = Output(UInt((dataWidth / 8).W))

  val bready = Output(Bool())
  val bvalid = Input(Bool())
  val bresp = Input(UInt(2.W))

  val arvalid = Output(Bool())
  val arready = Input(Bool())
  val araddr = Output(UInt(addrWidth.W))

  val rvalid = Input(Bool())
  val rready = Output(Bool())
  val rdata = Input(UInt(dataWidth.W))
  val rresp = Input(UInt(2.W))
}
