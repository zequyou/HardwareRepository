
package IPRepository.SEIDirectSyncRam

import Interfaces.Immediate
import chisel3._

class SEIDirectSyncRamWrFirst(wrAddrWidth: Int, rdAddrWidth: Int, wrDataSize: Int, rdDataSize: Int) extends Module {

  private val RAM_DATA_WIDTH = wrDataSize * 8
  private val RD_ADDR_WIDTH = rdAddrWidth
  private val RD_DATA_WIDTH = rdDataSize * 8
  private val WR_ADDR_WIDTH = wrAddrWidth
  private val WR_DATA_WIDTH = wrDataSize * 8
  private val RAM_READ_GROUP_COUNT = Math.pow(2, rdAddrWidth - wrAddrWidth).toInt

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

  val mem = SyncReadMem(Math.pow(2, wrAddrWidth).toInt, Vec(1, UInt(WR_DATA_WIDTH.W)))
  val writeData: Vec[UInt] = Wire(Vec(1, UInt(RAM_DATA_WIDTH.W)))
  val writeMask: Vec[Bool] = Wire(Vec(1, Bool()))
  val readData:  Vec[UInt] = Wire(Vec(1, UInt(RAM_DATA_WIDTH.W)))
  val readDataGroup: Vec[UInt] = Wire(Vec(RAM_READ_GROUP_COUNT, UInt(RD_DATA_WIDTH.W)))
  val readAddrHi: UInt = io.rd.addr.bits(rdAddrWidth - 1, rdAddrWidth - wrAddrWidth)
  val readAddrLo: UInt = io.rd.addr.bits(rdAddrWidth - wrAddrWidth - 1, 0)
  val readAddrLoBuffer = RegInit(0.U((rdAddrWidth - wrAddrWidth).W))

  writeData(0) := io.wr.data
  writeMask(0) := io.wr.addr.valid

  mem.write(io.wr.addr.bits, writeData, writeMask)

  readData := mem.read(readAddrHi, io.rd.addr.valid)

  for(i <- 0 until RAM_READ_GROUP_COUNT) {
    readDataGroup(i) := readData(0)((i + 1) * RD_DATA_WIDTH - 1, i * RD_DATA_WIDTH)
  }

  when(io.rd.addr.valid) {
    readAddrLoBuffer := readAddrLo
  }

  io.rd.data := readDataGroup(readAddrLoBuffer)
}