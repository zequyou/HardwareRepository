
package IPRepository.SEIDirectSyncRam

import Interfaces.Immediate
import chisel3._

class SEIDirectSyncRamCommon(wrAddrWidth: Int, rdAddrWidth: Int, dataSize: Int) extends Module {

  private val WR_ADDR_WIDTH = wrAddrWidth
  private val WR_DATA_WIDTH = dataSize * 8
  private val RD_ADDR_WIDTH = rdAddrWidth
  private val RD_DATA_WIDTH = dataSize * 8
  private val RAM_WIDTH = dataSize * 8
  private val RAM_DEPTH = Math.pow(2, wrAddrWidth).toInt

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

  val ram = SyncReadMem(RAM_DEPTH, UInt(RAM_WIDTH.W))
  val readData = RegInit(0.U(RAM_WIDTH.W))

  when (io.wr.addr.valid) {
    ram.write(io.wr.addr.bits, io.wr.data)
  }

  when (io.rd.addr.valid) {
    readData := ram.read(io.rd.addr.bits)
  }

  io.rd.data := readData
}
