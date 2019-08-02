package Interfaces

import chisel3._
import chisel3.util.Decoupled

class AxiLiteSimplified(val addrWidth: Int, val dataSize: Int) extends Bundle {

  private val ADDR_WIDTH = addrWidth
  private val DATA_WIDTH = dataSize * 8
  private val DATA_SIZE = dataSize

  val wr = Decoupled(new Bundle {
    val addr = UInt(ADDR_WIDTH.W)
    val data = UInt(DATA_WIDTH.W)
    val strb = UInt(DATA_SIZE.W)
  })

  val rd = new Bundle {
    val addr = Decoupled(UInt(ADDR_WIDTH.W))
    val data = Input(UInt(DATA_WIDTH.W))
  }
}
