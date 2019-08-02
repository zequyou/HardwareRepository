package IPRepository.AsyncFIFO

import chisel3._
import chisel3.core.withClockAndReset

class AsyncFIFO1(val depth: Int, val width: Int) extends Module {

  private val PTR_WIDTH = chisel3.util.log2Ceil(depth) + 1

  val io = IO(new Bundle {
    val wr = new Bundle {
      val valid = Input(Bool())
      val bits = Input(UInt(width.W))
      val full = Output(Bool())
      val enable = Input(Bool())
    }
    val rd = new Bundle {
      val valid = Input(Bool())
      val bits = Output(UInt(width.W))
      val empty = Output(Bool())
      val enable = Input(Bool())
    }
    val rd_reset = Input(Bool())
    val wr_reset = Input(Bool())
    val wr_clock = Input(Clock())
    val rd_clock = Input(Clock())
  })

  val ram = Mem(depth, UInt(width.W))
  val wrGrayWire: UInt = Wire(UInt(PTR_WIDTH.W))
  val rdGrayWire: UInt = Wire(UInt(PTR_WIDTH.W))

  withClockAndReset(io.wr_clock, io.wr_reset) {
    val wrPtr = RegInit(0.U(PTR_WIDTH.W))
    val wrPtrNext = Wire(UInt(PTR_WIDTH.W))
    val wrGray = RegInit(0.U(PTR_WIDTH.W))
    val wrGrayNext = Wire(UInt(PTR_WIDTH.W))
    val rdGrayToWr = RegNext(rdGrayWire)
    val rdGrayInWr = RegNext(rdGrayToWr)
    val wrFull = RegInit(true.B)

    when(io.wr.valid & !io.wr.full) {
      wrPtrNext := wrPtr + 1.U
    } .otherwise {
      wrPtrNext := wrPtr
    }
    wrGrayNext := wrPtrNext ^ wrPtrNext(PTR_WIDTH - 1, 1)

    wrPtr := wrPtrNext
    wrGray := wrGrayNext
    wrFull := (
                (wrGrayNext(PTR_WIDTH - 1, PTR_WIDTH - 2) === (~rdGrayInWr(PTR_WIDTH - 1, PTR_WIDTH - 2)).asUInt()) &
                (wrGrayNext(PTR_WIDTH - 3, 0) === rdGrayInWr(PTR_WIDTH - 3, 0))
              ) |
              io.wr.enable

    when (io.wr.valid & !io.wr.full) {
      ram.write(wrPtr(PTR_WIDTH - 2, 0), io.wr.bits)
    }

    wrGrayWire := wrGray
    io.wr.full := wrFull
  }

  withClockAndReset(io.rd_clock, io.rd_reset) {
    val rdPtr = RegInit(0.U(PTR_WIDTH.W))
    val rdPtrNext = Wire(UInt(PTR_WIDTH.W))
    val rdGray = RegInit(0.U(PTR_WIDTH.W))
    val rdGrayNext = Wire(UInt(PTR_WIDTH.W))
    val wrGrayToRd = RegNext(wrGrayWire)
    val wrGrayInRd = RegNext(wrGrayToRd)
    val rdEmpty = RegInit(true.B)
    val rdData = RegInit(0.U(width.W))

    when (io.rd.valid & !io.rd.empty) {
      rdPtrNext := rdPtr + 1.U
    } .otherwise {
      rdPtrNext := rdPtr
    }
    rdGrayNext := rdPtrNext ^ rdPtrNext(PTR_WIDTH - 1, 1)

    rdPtr := rdPtrNext
    rdGray := rdGrayNext
    rdEmpty := rdGrayNext === wrGrayInRd | io.rd.enable
    when (io.rd.valid) {
      rdData := ram.read(rdPtr(PTR_WIDTH - 2, 0))
    }

    rdGrayWire := rdGray
    io.rd.bits := rdData
    io.rd.empty := rdEmpty
  }
}

object AsyncFIFO1 extends App {
  Driver.execute(
    Array("-td", Support.MyPath.getSourceDir(AsyncFIFO1.getClass)),
    () => new AsyncFIFO1(1024, 32)
  )
}