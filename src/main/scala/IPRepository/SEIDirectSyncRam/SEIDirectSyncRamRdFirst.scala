
package IPRepository.SEIDirectSyncRam

import Interfaces.Immediate
import chisel3._
import chisel3.util._

class SEIDirectSyncRamRdFirst(wrAddrWidth: Int, rdAddrWidth: Int, wrDataSize: Int, rdDataSize: Int) extends Module {

  private val RAM_WIDTH = wrDataSize * 8
  private val RAM_DEPTH = Math.pow(2, rdAddrWidth).toInt
  private val RD_ADDR_WIDTH = rdAddrWidth
  private val RD_DATA_WIDTH = rdDataSize * 8
  private val WR_ADDR_WIDTH = wrAddrWidth
  private val WR_DATA_WIDTH = wrDataSize * 8
  private val WR_GROUP_COUNT = Math.pow(2, wrAddrWidth - rdAddrWidth).toInt

  val io = IO(new Bundle {
    val wr = new Bundle {
      val addr = Flipped(Immediate(UInt(WR_ADDR_WIDTH.W)))
      val data = Input(UInt(WR_DATA_WIDTH.W))
    }
    val rd = new Bundle {
      val addr = Flipped(Immediate(UInt(RD_ADDR_WIDTH.W)))
      val data = Input(UInt(RD_DATA_WIDTH.W))
    }
  })

  val mem = SyncReadMem(RAM_DEPTH, Vec(WR_GROUP_COUNT, UInt(WR_DATA_WIDTH.W)))
  val wrMask: Vec[Bool] = Wire(Vec(WR_GROUP_COUNT, Bool()))
  val wrData: Vec[UInt] = Wire(Vec(WR_GROUP_COUNT, UInt(WR_DATA_WIDTH.W)))
  val rdData: Vec[UInt] = Wire(Vec(WR_GROUP_COUNT, UInt(WR_DATA_WIDTH.W)))
  val wrAddrHi: UInt = io.wr.addr.bits(wrAddrWidth - 1, wrAddrWidth - rdAddrWidth)
  val wrAddrLo: UInt = io.wr.addr.bits(wrAddrWidth - rdAddrWidth - 1, 0)

  for(i <- 0 until WR_GROUP_COUNT) {
    wrData(i) := io.wr.data
    wrMask(i) := 0.U
  }

  wrMask(wrAddrLo) := io.wr.addr.valid

  mem.write(wrAddrHi, wrData, wrMask)

  rdData := mem.read(io.rd.addr.bits, io.rd.addr.valid)
  io.rd.data := Cat(rdData.reverse)
}