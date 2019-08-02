package IPRepository.AXI

import IPRepository.AsyncTx.AsyncTxFixed
import IPRepository.SAXILiteBridge.SAXILiteBridge1
import Interfaces.AxiLite
import chisel3._
import chisel3.util._

class AXIAsyncTxSimplified(val levelPeriod: Int) extends Module {
  val io = IO(new Bundle{
    val s_axi = Flipped(new AxiLite(2, 32))
    val tx = Output(Bool())
  })

  val bridge = Module(new SAXILiteBridge1(2, 4))
  val asyncTx = Module(new AsyncTxFixed(0, 0, 8, levelPeriod))
  val dataBuffer = RegInit(0.U(8.W))
  bridge.io.s_axi <> io.s_axi

  val idle :: send :: Nil = Enum(2)
  val stateMain = RegInit(idle)

  switch(stateMain) {
    is(idle) {
      when (bridge.io.s_axi_simplified.wr.valid) {
        stateMain := send
      }
    }
    is(send) {
      when (asyncTx.io.s_wr.ready) {
        stateMain := idle
      }
    }
  }

  when (bridge.io.s_axi_simplified.wr.valid) {
    dataBuffer := bridge.io.s_axi_simplified.wr.bits.data
  }

  bridge.io.s_axi_simplified.rd.data := stateMain
  asyncTx.io.s_wr.bits := dataBuffer
  asyncTx.io.s_wr.valid := stateMain === send
  io.tx := asyncTx.io.tx
}
