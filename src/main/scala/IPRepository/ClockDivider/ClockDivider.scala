package IPRepository.ClockDivider

import chisel3._

class ClockDivider(val ratio: Int) extends Module {

  assert(ratio > 1)
  assert(ratio % 2 == 0)

  private val COUNTER_MAX = ratio - 1
  private val COUNTER_WIDTH = Math.ceil(Math.log(COUNTER_MAX) / Math.log(2)).toInt

  val io = IO(new Bundle{
    val clock_out = Output(Bool())
  })

  val counter = RegInit(0.U(COUNTER_WIDTH.W))

  when (counter === COUNTER_MAX.U) {
    counter := 0.U
  } .otherwise {
    counter := counter + 1.U
  }

  io.clock_out := counter >= (ratio / 2).U
}
