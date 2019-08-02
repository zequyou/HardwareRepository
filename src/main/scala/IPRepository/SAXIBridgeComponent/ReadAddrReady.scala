package IPRepository.SAXIBridgeComponent

import chisel3._

class ReadAddrReady extends Module {
  val io = IO(new Bundle {
    val arvalid = Input(Bool())
    val arready = Output(Bool())
    val rvalid = Input(Bool())
    val rready = Input(Bool())
    val rlast = Input(Bool())
  })

  val addrReadReady = RegInit(true.B)

  when (io.arvalid & io.arready) {
    addrReadReady := false.B
  } .elsewhen(io.rvalid & io.rready & io.rlast) {
    addrReadReady := true.B
  }

  io.arready := addrReadReady
}

object ReadAddrReady {
  /**
    * generate arready reg value
    *
    * @param arvalid Wire
    * @param rvalid  wire
    * @param rready  Wire
    * @param rlast   wire
    * @return
    */
  def apply(arvalid: Bool,
            rvalid: Bool,
            rready: Bool,
            rlast: Bool = true.B):
  Bool = {
    val inst = Module(new ReadAddrReady())
    inst.io.arvalid := arvalid
    inst.io.rvalid := rvalid
    inst.io.rready := rready
    inst.io.rlast := rlast
    inst.io.arready
  }
}

