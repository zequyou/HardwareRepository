package IPRepository.AsyncFIFO

import chisel3._
import chisel3.core.withClockAndReset
import chisel3.internal.naming.chiselName
import chisel3.util._

@chiselName
class AsyncFIFODecoupledExample extends Module {

  val io = IO(new Bundle {
    val wr_clock = Input(Clock())
    val wr_reset = Input(Bool())
    val wr_valid = Output(Bool())
    val wr_ready = Output(Bool())
    val wr_bits = Output(UInt(10.W))
    val rd_clock = Input(Clock())
    val rd_reset = Input(Bool())
    val rd_valid = Output(Bool())
    val rd_ready = Output(Bool())
    val rd_bits = Output(UInt(10.W))
  })

  val fifo = Module(new AsyncFIFODecoupled(128, 10))
  fifo.io.wr_enable := 1.U
  fifo.io.wr_clock := io.wr_clock
  fifo.io.wr_reset := io.wr_reset
  fifo.io.rd_enable := 1.U
  fifo.io.rd_clock := io.rd_clock
  fifo.io.rd_reset := io.rd_reset


  withClockAndReset(io.wr_clock, io.wr_reset) {
    val idle :: running :: wait :: Nil = Enum(3)
    val counter = RegInit(0.U(10.W))
    val delay = RegInit(0.U(8.W))
    val state = RegInit(idle)
    val writeValid = RegInit(false.B)

    switch(state) {
      is(idle) {
        state := running
      }
      is(running) {
        when(!fifo.io.wr.ready) {
          state := wait
        }
      }
      is(wait) {
        when(delay === 255.U) {
          state := running
        }
      }
    }

    when(state === wait) {
      delay := delay + 1.U
    }.otherwise {
      delay := 0.U
    }

    when(fifo.io.wr.valid & fifo.io.wr.ready) {
      counter := counter + 1.U
    }

    when(state === running) {
      writeValid := true.B
    }.otherwise {
      writeValid := false.B
    }

    fifo.io.wr.valid := writeValid
    fifo.io.wr.bits := counter

    io.wr_bits := fifo.io.wr.bits
    io.wr_valid := writeValid
    io.wr_ready := fifo.io.wr.ready
  }


  withClockAndReset(io.rd_clock, io.rd_reset) {
    val idle :: running :: wait :: Nil = Enum(3)
    val delay = RegInit(0.U(8.W))
    val state = RegInit(idle)
    val readReady = RegInit(false.B)

    switch(state) {
      is(idle) {
        state := running
      }
      is(running) {
        when (! fifo.io.rd.valid) {
          state := wait
        }
      }
      is(wait) {
        when (delay === 255.U) {
          state := running
        }
      }
    }

    when(state === wait) {
      delay := delay + 1.U
    } .otherwise {
      delay := 0.U
    }

    when (state === running) {
      readReady := true.B
    } .otherwise {
      readReady := false.B
    }

    fifo.io.rd.ready := readReady

    io.rd_bits := fifo.io.rd.bits
    io.rd_valid := fifo.io.rd.valid
    io.rd_ready := fifo.io.rd.ready
  }
}

object AsyncFIFODecoupledExample extends App {
  Driver.execute(
    Array("-td", Support.MyPath.getSourceDir(AsyncFIFODecoupledExample.getClass)),
    () => new AsyncFIFODecoupledExample
  )
}