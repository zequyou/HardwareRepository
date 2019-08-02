package IPRepository.SAXILiteBridge

import Interfaces.{AxiLite, AxiLiteSimplified}
import chisel3._

class SAXILiteBridge(val addrWidth: Int, val dataSize: Int, val otherOptions: Map[String, Boolean] = Map())
  extends Module {

  val io = IO(new Bundle {
    val s_axi = Flipped(new AxiLite(addrWidth, dataSize))
    val s_axi_simplified = new AxiLiteSimplified(addrWidth, dataSize)
  })

  private val FLOW_CTRL = otherOptions.getOrElse("FLOW_CTRL", true).toString.toBoolean

  val bridgeInst: Module = if (FLOW_CTRL) {
    Module(new SAXILiteBridge0(addrWidth, dataSize))
  } else {
    Module(new SAXILiteBridge1(addrWidth, dataSize))
  }

  io <> bridgeInst.io
}
