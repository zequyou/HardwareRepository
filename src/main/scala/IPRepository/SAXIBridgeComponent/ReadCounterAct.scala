package IPRepository.SAXIBridgeComponent

import chisel3._

class ReadCounterAct extends Module {
  val io = IO(new Bundle {
    val rvalid = Input(Bool())
    val rready = Input(Bool())
    val rlast = Input(Bool())
    val counter = Output(UInt(8.W))
  })

  val counter = RegInit(0.U(8.W))

  when (io.rvalid & io.rready & io.rlast) {
    counter := 0.U
  } .elsewhen(io.rvalid & io.rready) {
    counter := counter + 1.U
  }

  io.counter := counter
}

object ReadCounterAct {

  /**
    * generate read actual reg value
    * @param rvalid wire
    * @param rready reg
    * @param rlast  reg
    * @return
    */
  def apply(rvalid: Bool,
              rready: Bool,
              rlast: Bool):
  UInt = {
    val module = Module(new ReadCounterAct)
    module.io.rvalid := rvalid
    module.io.rready := rready
    module.io.rlast := rlast
    module.io.counter
  }
}
