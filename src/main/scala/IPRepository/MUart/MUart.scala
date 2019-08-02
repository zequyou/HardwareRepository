package IPRepository.MUart

import Interfaces.Uart
import chisel3._
import chisel3.util.Decoupled

class MUart(val dataWidth: Int) extends Module {

  assert(dataWidth >= 5 && dataWidth <= 8)

  val io = IO(new Bundle {
    val send = Flipped(Decoupled(UInt(dataWidth.W)))
    val recv = Decoupled(UInt(dataWidth.W))
    val uart = new Uart()
  })


}
