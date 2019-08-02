package IPRepository.AsyncFIFO

import chisel3._
import chisel3.core.withClockAndReset
import chisel3.internal.naming.chiselName

@chiselName
class AsyncFIFO2(val depth: Int, val width: Int) extends Module {

  val io = IO(new Bundle {
    val wr = new Bundle {
      val valid = Input(Bool())
      val bits = Input(UInt(width.W))
      val not_full = Output(Bool())
      val enable = Input(Bool())
    }
    val rd = new Bundle {
      val valid = Input(Bool())
      val bits = Output(UInt(width.W))
      val not_empty = Output(Bool())
      val enable = Input(Bool())
    }
    val rd_reset = Input(Bool())
    val wr_reset = Input(Bool())
    val wr_clock = Input(Clock())
    val rd_clock = Input(Clock())
  })
  val ram = Mem(depth, UInt(width.W))
  val wrGrayWire: UInt = Wire(UInt(width.W))
  val rdGrayWire: UInt = Wire(UInt(width.W))
  private val PTR_WIDTH = chisel3.util.log2Ceil(depth) + 1

  withClockAndReset(io.wr_clock, io.wr_reset) {
    val wrPtr = RegInit(0.U(PTR_WIDTH.W))
    val wrPtrNext = Wire(UInt(PTR_WIDTH.W))
    val wrGray = RegInit(0.U(PTR_WIDTH.W))
    val wrGrayNext = Wire(UInt(PTR_WIDTH.W))
    val rdGrayToWr = RegNext(rdGrayWire)
    val rdGrayInWr = RegNext(rdGrayToWr)
    val wrNotFull = RegInit(false.B)

    when(io.wr.valid & io.wr.not_full) {
      wrPtrNext := wrPtr + 1.U
    }.otherwise {
      wrPtrNext := wrPtr
    }
    wrGrayNext := wrPtrNext ^ wrPtrNext(PTR_WIDTH - 1, 1)

    wrPtr := wrPtrNext
    wrGray := wrGrayNext
    wrNotFull := !(
        (wrGrayNext(PTR_WIDTH - 1, PTR_WIDTH - 2) === (~rdGrayInWr(PTR_WIDTH - 1, PTR_WIDTH - 2)).asUInt()) &&
        (wrGrayNext(PTR_WIDTH - 3, 0) === rdGrayInWr(PTR_WIDTH - 3, 0))
      ) & io.wr.enable

    when(io.wr.valid & io.wr.not_full) {
      ram.write(wrPtr(PTR_WIDTH - 2, 0), io.wr.bits)
    }

    wrGrayWire := wrGray
    io.wr.not_full := wrNotFull
  }

  withClockAndReset(io.rd_clock, io.rd_reset) {
    val rdPtr = RegInit(0.U(PTR_WIDTH.W))
    val rdPtrNext = Wire(UInt(PTR_WIDTH.W))
    val rdGray = RegInit(0.U(PTR_WIDTH.W))
    val rdGrayNext = Wire(UInt(PTR_WIDTH.W))
    val wrGrayToRd = RegNext(wrGrayWire)
    val wrGrayInRd = RegNext(wrGrayToRd)
    val rdNotEmpty = RegInit(false.B)
    val rdData = RegInit(0.U(width.W))

    when(io.rd.valid & io.rd.not_empty) {
      rdPtrNext := rdPtr + 1.U
    }.otherwise {
      rdPtrNext := rdPtr
    }
    rdGrayNext := rdPtrNext ^ rdPtrNext(PTR_WIDTH - 1, 1)

    rdPtr := rdPtrNext
    rdGray := rdGrayNext
    rdNotEmpty := rdGrayNext =/= wrGrayInRd & io.rd.enable
    when(io.rd.valid) {
      rdData := ram.read(rdPtr(PTR_WIDTH - 2, 0))
    }

    rdGrayWire := rdGray
    io.rd.bits := rdData
    io.rd.not_empty := rdNotEmpty
  }
}

object AsyncFIFO2 extends App {
  Driver.execute(
    Array("-td", Support.MyPath.getSourceDir(AsyncFIFO2.getClass)),
    () => new AsyncFIFO2(1024, 32)
  )
}