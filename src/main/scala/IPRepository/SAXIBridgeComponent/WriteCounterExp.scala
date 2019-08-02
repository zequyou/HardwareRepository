package IPRepository.SAXIBridgeComponent

import chisel3._

class WriteCounterExp extends Module {
  val io = IO(new Bundle {
    val awvalid = Input(Bool())
    val awready = Input(Bool())
    val awlen = Input(UInt(8.W))
    val counter = Output(UInt(8.W))
  })

  val counter = RegInit(0.U(8.W))

  when (io.awvalid & io.awready) {
    counter := io.awlen
  }

  io.counter := counter
}

object WriteCounterExp {

  def apply(awvalid: Bool,
            awready: Bool,
            awlen: UInt):
  UInt = {
    val module = Module(new WriteCounterExp)
    module.io.awvalid := awvalid
    module.io.awready := awready
    module.io.awlen := awlen
    module.io.counter
  }
}
