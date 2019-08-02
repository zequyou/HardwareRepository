package IPRepository.CrossClockBuffer

import chisel3._
import chisel3.core.withClockAndReset
import chisel3.internal.naming.chiselName

@chiselName
class CrossClockBufferExample extends Module {

  val io = IO(new Bundle {
    val src_clock = Input(Clock())
    val src_reset = Input(Bool())
    val dst_clock = Input(Clock())
    val dst_reset = Input(Bool())
    val src_count = Output(UInt(8.W))
    val dst_count = Output(UInt(8.W))
  })

  val clockCrossBuffer = Module(new CrossClockBuffer(UInt(8.W), 2))
  clockCrossBuffer.io.dst_clock := io.dst_clock
  clockCrossBuffer.io.dst_reset := io.dst_reset

  withClockAndReset(io.src_clock, io.src_reset) {
    val counter = RegInit(0.U(8.W))

    counter := counter + 1.U
    clockCrossBuffer.io.src := counter
    io.src_count := counter
  }

  io.dst_count := clockCrossBuffer.io.dst
}

object CrossClockBufferExample extends App {

  println("Generate CrossClockBuffer example")
  chisel3.Driver.execute(
    Array("-td", Support.MyPath.getSourceDir(CrossClockBufferExample.getClass)),
    () => new CrossClockBufferExample()
  )
}
