package IPRepository.SAXIBridgeComponent

import chisel3._

class ReadAddrValidFlag extends Module {

  val io = IO(new Bundle {
    val arvalid = Input(Bool())
    val arready = Input(Bool())
    val rvalid = Input(Bool())
    val rready = Input(Bool())
    val rlast = Input(Bool())
    val flag = Output(Bool())
  })

  val flag = RegInit(false.B)

  when (io.arvalid & io.arready) {
    flag := true.B
  } .elsewhen(io.rvalid & io.rready & io.rlast) {
    flag := false.B
  }

  io.flag := flag
}

object ReadAddrValidFlag {

  def apply(arvalid: Bool,
            arready: Bool,
            rvalid: Bool,
            rready: Bool,
            rlast: Bool):
  Bool = {
    val module = Module(new ReadAddrValidFlag)
    module.io.arvalid := arvalid
    module.io.arready := arready
    module.io.rvalid := rvalid
    module.io.rready := rready
    module.io.rlast := rlast
    module.io.flag
  }
}
