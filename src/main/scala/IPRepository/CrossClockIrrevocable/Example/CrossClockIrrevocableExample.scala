package IPRepository.CrossClockIrrevocable.Example

import IPRepository.CrossClockIrrevocable.CrossClockIrrevocable
import chisel3._
import chisel3.core.withClockAndReset
import chisel3.util._
import chisel3.internal.naming.chiselName

@chiselName
class CrossClockIrrevocableExample extends Module {

  val io = IO(new Bundle {
    val src_clock = Input(Clock())
    val src_reset = Input(Bool())
    val dst_clock = Input(Clock())
    val dst_reset = Input(Bool())
    val src_count = Output(UInt(8.W))
    val src_valid = Output(Bool())
    val src_ready = Output(Bool())
    val dst_count = Output(UInt(8.W))
    val dst_valid = Output(Bool())
    val dst_ready = Output(Bool())
  })

  val crossClockModule = Module(new CrossClockIrrevocable(UInt(8.W)))
  crossClockModule.io.dst_clock := io.dst_clock
  crossClockModule.io.dst_reset := io.dst_reset
  crossClockModule.io.src_clock := io.src_clock
  crossClockModule.io.src_reset := io.src_reset

  withClockAndReset(io.src_clock, io.src_reset) {
    val idle :: running :: delay :: Nil = Enum(3)
    val srcCounter = RegInit(0.U(8.W))
    val srcValid = RegInit(false.B)
    val delayCounter = RegInit(0.U(4.W))
    val state = RegInit(idle)
    val stateDelay1 = RegNext(state)

    switch(state) {
      is(idle) {
        state := running
      }
      is(running) {
        when (crossClockModule.io.src.valid & crossClockModule.io.src.ready) {
          state := delay
        }
      }
      is(delay) {
        when (delayCounter === 15.U) {
          state := running
        }
      }
    }

    when (crossClockModule.io.src.valid & crossClockModule.io.src.ready) {
      srcValid := false.B
    } .elsewhen(state === running) {
      srcValid := true.B
    }

    when (state === delay) {
      delayCounter := delayCounter + 1.U
    } .otherwise {
      delayCounter := 0.U
    }

    when (state === delay & stateDelay1 =/= delay) {
      srcCounter := srcCounter + 1.U
    }

    crossClockModule.io.src.valid := srcValid
    crossClockModule.io.src.bits := srcCounter
    io.src_count := srcCounter
    io.src_valid := srcValid
    io.src_ready := crossClockModule.io.src.ready
  }

  withClockAndReset(io.dst_clock, io.dst_reset) {
    val dstReady = RegInit(false.B)

    when (crossClockModule.io.dst.valid & crossClockModule.io.dst.ready) {
      dstReady := false.B
    } .elsewhen(crossClockModule.io.dst.valid) {
      dstReady := true.B
    }

    crossClockModule.io.dst.ready := dstReady
    io.dst_count := crossClockModule.io.dst.bits
    io.dst_valid := crossClockModule.io.dst.valid
    io.dst_ready := dstReady
  }
}

object CrossClockIrrevocableExample {
  Driver.execute(
    Array("-td", Support.MyPath.getSourceDir(CrossClockIrrevocableExample.getClass)),
    () => new CrossClockIrrevocableExample
  )
}