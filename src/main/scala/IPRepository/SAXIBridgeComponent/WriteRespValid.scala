package IPRepository.SAXIBridgeComponent

import chisel3._

class WriteRespValid extends Module {
  val io = IO(new Bundle {
    val wvalid = Input(Bool())
    val wready = Input(Bool())
    val wlast = Input(Bool())
    val bvalid = Output(Bool())
    val bready = Input(Bool())
  })

  val bvalid = RegInit(false.B)

  when (io.wvalid & io.wready & io.wlast) {
    bvalid := true.B
  } .elsewhen(io.bready & io.bvalid) {
    bvalid := false.B
  }

  io.bvalid := bvalid
}

object WriteRespValid {

  /**
    * generate bvalid reg value
    * @param wvalid wire
    * @param wready reg
    * @param wlast  wire
    * @param bready wire
    * @return bvalid reg value
    */
  def apply(wvalid: Bool,
            wready: Bool,
            wlast: Bool,
            bready: Bool):
  Bool = {
    val module = Module(new WriteRespValid)
    module.io.wvalid := wvalid
    module.io.wready := wready
    module.io.wlast := wlast
    module.io.bready := bready
    module.io.bvalid
  }
}
