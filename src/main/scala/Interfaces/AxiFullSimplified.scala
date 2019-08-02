package Interfaces

import chisel3._
import chisel3.util.Decoupled

class AxiFullSimplified(val addrWidth: Int, val dataSize: Int) extends Bundle {

  private val ADDR_WIDTH = addrWidth
  private val DATA_WIDTH = dataSize * 8

  val rcmd = Decoupled(new Bundle {
    val addr = Output(UInt(ADDR_WIDTH.W))
    val burst = Output(UInt(2.W))
    val len = Output(UInt(8.W))
  })

  val rdata = Flipped(Decoupled(UInt(DATA_WIDTH.W)))

  val wcmd = Decoupled(new Bundle {
    val addr = Output(UInt(ADDR_WIDTH.W))
    val burst = Output(UInt(2.W))
    val len = Output(UInt(8.W))
  })

  val wdata = Decoupled(new Bundle {
    val data = Output(UInt(DATA_WIDTH.W))
    val strb = Output(UInt(dataSize.W))
  })

}
