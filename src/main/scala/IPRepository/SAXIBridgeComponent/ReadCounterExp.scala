package IPRepository.SAXIBridgeComponent

import chisel3._

class ReadCounterExp extends Module {
  val io = IO(new Bundle {
    val arvalid = Input(Bool())
    val arready = Input(Bool())
    val arlen = Input(UInt(8.W))
    val readExp = Output(UInt(8.W))
  })

  val readExp = RegInit(0.U(8.W))

  when (io.arvalid & io.arready) {
    readExp := io.arlen
  }

  io.readExp := readExp
}

object ReadCounterExp {

  /**
    * generate read expect reg value
    *
    * @param arvalid wire
    * @param arready reg
    * @param arlen   wire
    * @return
    */
  def apply(arvalid: Bool,
            arready: Bool,
            arlen: UInt):
  UInt = {
    val module = Module(new ReadCounterExp())
    module.io.arvalid := arvalid
    module.io.arready := arready
    module.io.arlen := arlen
    module.io.readExp
  }
}
