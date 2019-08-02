
package IPRepository.SEIDirectSyncRam

import Interfaces.Immediate
import chisel3._

class SyncRam(wrAddrWidth: Int, rdAddrWidth: Int, wrDataSize: Int) extends Module {

  private val rdDataSize = (Math.pow(2, wrAddrWidth - rdAddrWidth) * wrDataSize).toInt

  private val WR_ADDR_WIDTH = wrAddrWidth
  private val WR_DATA_WIDTH = wrDataSize * 8
  private val RD_ADDR_WIDTH = rdAddrWidth
  private val RD_DATA_WIDTH = rdDataSize * 8

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

  if (rdDataSize > wrDataSize) {
    val generatedRam = Module(new SEIDirectSyncRamRdFirst(wrAddrWidth, rdAddrWidth, wrDataSize, rdDataSize))
    generatedRam.io.rd <> io.rd
    generatedRam.io.wr <> io.wr
  } else if (rdDataSize < wrDataSize) {
    val generatedRam = Module(new SEIDirectSyncRamWrFirst(wrAddrWidth, rdAddrWidth, wrDataSize, rdDataSize))
    generatedRam.io.rd <> io.rd
    generatedRam.io.wr <> io.wr
  } else {
    val generatedRam = Module(new SEIDirectSyncRamCommon(wrAddrWidth, rdAddrWidth, wrDataSize))
    generatedRam.io.rd <> io.rd
    generatedRam.io.wr <> io.wr
  }
}