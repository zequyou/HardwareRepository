package IPRepository.SAXIBridgeComponent

import chisel3._

class ReadLast extends Module {
  val io = IO(new Bundle {
    val arvalid = Input(Bool())
    val arready = Input(Bool())
    val arlen = Input(UInt(8.W))
    val rvalid = Input(Bool())
    val rready = Input(Bool())
    val readExp = Input(UInt(8.W))
    val readAct = Input(UInt(8.W))
    val readLast = Output(Bool())
  })

  val readLast = RegInit(false.B)

  when (io.arvalid & io.arready & io.arlen === 0.U) {
    readLast := true.B
  } .elsewhen (io.rvalid & io.rready & io.readExp === io.readAct + 1.U) {
    readLast := true.B
  } .elsewhen (io.readExp === io.readAct) {
    readLast := true.B
  } .otherwise {
    readLast := false.B
  }

  io.readLast := readLast
}

object ReadLast {
  /**
    * generate rlast value
    */
  def apply(arvalid: Bool,
            arready: Bool,
            arlen: UInt,
            rvalid: Bool,
            rready: Bool,
            rExp: UInt,
            rAct: UInt):
  Bool = {
    val module = Module(new ReadLast())
    module.io.arvalid := arvalid
    module.io.arready := arready
    module.io.arlen := arlen
    module.io.rvalid := rvalid
    module.io.rready := rready
    module.io.readExp := rExp
    module.io.readAct := rAct
    module.io.readLast
  }
}
