package IPRepository.SAXIBridgeComponent

import chisel3._

class WriteDataReady extends Module {
  val io = IO(new Bundle {
    val awvalid = Input(Bool())
    val awready = Input(Bool())
    val wvalid = Input(Bool())
    val wready = Output(Bool())
    val wlast = Input(Bool())
  })

  val wready = RegInit(false.B)

  when (io.awvalid & io.awready) {
    wready := true.B
  } .elsewhen(io.wvalid & io.wready & io.wlast) {
    wready := false.B
  }

  io.wready := wready
}

object WriteDataReady {

  /**
    * generate wready reg value
    * @param awvalid wire
    * @param awready reg
    * @param wvalid  wire
    * @param wlast   wire
    * @return
    */
  def apply(awvalid: Bool,
            awready: Bool,
            wvalid: Bool,
            wlast: Bool):
  Bool = {
    val module = Module(new WriteDataReady)
    module.io.awvalid := awvalid
    module.io.awready := awready
    module.io.wvalid := wvalid
    module.io.wlast := wlast
    module.io.wready
  }
}
