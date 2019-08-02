package IPRepository.SAXIBridgeComponent

import chisel3._

class WriteCounterAct extends Module {
  val io = IO(new Bundle {
    val wvalid = Input(Bool())
    val wready = Input(Bool())
    val bvalid = Input(Bool())
    val bready = Input(Bool())
    val counter = Output(UInt(8.W))
  })

  val counter = RegInit(0.U(8.W))

  when(io.wvalid & io.wready) {
    counter := counter + 1.U
  }.elsewhen(io.bvalid & io.bready) {
    counter := 0.U
  }

  io.counter := counter
}

object WriteCounterAct {

  /**
    * generate write actual reg value
    * @param wvalid wire
    * @param wready reg
    * @param bvalid reg
    * @param bready wire
    * @return
    */
  def apply(wvalid: Bool,
            wready: Bool,
            bvalid: Bool,
            bready: Bool):
  UInt = {
    val module = Module(new WriteCounterAct)
    module.io.wvalid := wvalid
    module.io.wready := wready
    module.io.bvalid := bvalid
    module.io.bready := bready
    module.io.counter
  }
}
