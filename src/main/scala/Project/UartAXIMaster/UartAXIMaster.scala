package Project.UartAXIMaster

import IPRepository.AsyncRx.AsyncRxFixed
import IPRepository.AsyncTx.AsyncTxFixed
import Interfaces.{AxiFull, Uart}
import chisel3._

class UartAXIMaster extends Module {

  val io = IO(new Bundle {
    val m_axi = new AxiFull(32, 4, 0, 0)
    val uart = Flipped(new Uart())
  })

  val asyncRxFixedModule = Module(new AsyncRxFixed(0, 8, 868))
  val asyncTxFixedModule = Module(new AsyncTxFixed(0, 0, 8, 868))

  asyncRxFixedModule.io.rx := io.uart.tx
  asyncTxFixedModule.io.tx := io.uart.rx
}
