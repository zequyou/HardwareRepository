package IPRepository.AXI

import Interfaces.AxiLite
import chisel3._

class AXIAsyncTxSimplifiedWithFIFO(val levelPeriod: Int, val FIFODepth: Int) extends Module {

  val io = IO(new Bundle{
    val s_axi = Flipped(new AxiLite(3, 32))
    val tx = Output(Bool())
  })
}
