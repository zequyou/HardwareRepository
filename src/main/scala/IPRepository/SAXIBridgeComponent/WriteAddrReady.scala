package IPRepository.SAXIBridgeComponent

import chisel3._

class WriteAddrReady extends Module {
  val io = IO(new Bundle {
    val awvalid = Input(Bool())
    val awready = Output(Bool())
    val bvalid = Input(Bool())
    val bready = Input(Bool())
  })

  val addrWriteReady = RegInit(true.B)

  when (io.awready & io.awvalid) {
    addrWriteReady := false.B
  } .elsewhen(io.bvalid & io.bready) {
    addrWriteReady := true.B
  }

  io.awready := addrWriteReady
}

object WriteAddrReady {
  /**
    * generate awready reg value
    * @param awvalid wire
    * @param bvalid  wire
    * @param bready  wire
    * @return awready reg value
    */
  def apply(awvalid: Bool,
            bvalid: Bool,
            bready: Bool):
  Bool = {
    val module = Module(new WriteAddrReady)
    module.io.awvalid := awvalid
    module.io.bvalid := bvalid
    module.io.bready := bready
    module.io.awready
  }
}
