package IPRepository.CrossClockBuffer

import chisel3._
import chisel3.core.withClockAndReset
import chisel3.internal.naming.chiselName

@chiselName
class CrossClockBuffer[T <: chisel3.Data](val gen: T, val stage: Int = 2) extends Module {

  assert(stage >= 2)

  val io = IO(new Bundle {
    val src = Input(gen)
    val dst = Output(gen)
    val dst_clock = Input(Clock())
    val dst_reset = Input(Bool())
  })

  withClockAndReset(io.dst_clock, io.dst_reset) {
    val innerBuffer = Reg(Vec(stage, gen))

    innerBuffer(0) := io.src
    io.dst := innerBuffer(stage - 1)

    for (i <- 1 until stage) {
      innerBuffer(i) := innerBuffer(i - 1)
    }
  }
}