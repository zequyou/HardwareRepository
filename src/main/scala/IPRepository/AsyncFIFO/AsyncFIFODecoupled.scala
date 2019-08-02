package IPRepository.AsyncFIFO

import chisel3._
import chisel3.core.withClockAndReset
import chisel3.internal.naming.chiselName
import chisel3.util.Decoupled

@chiselName
class AsyncFIFODecoupled(val depth: Int, val width: Int) extends Module {

  private val PTR_WIDTH = chisel3.util.log2Ceil(depth) + 1

  val io = IO(new Bundle {
    val wr = Flipped(Decoupled(UInt(width.W)))
    val wr_enable = Input(Bool())
    val rd = Decoupled(UInt(width.W))
    val rd_enable = Input(Bool())
    val rd_reset = Input(Bool())
    val wr_reset = Input(Bool())
    val wr_clock = Input(Clock())
    val rd_clock = Input(Clock())
  })
  val asyncFIFO = Module(new AsyncFIFO2(depth, width))

  // clock and reset
  asyncFIFO.io.wr_clock := io.wr_clock
  asyncFIFO.io.wr_reset := io.wr_reset
  asyncFIFO.io.rd_clock := io.rd_clock
  asyncFIFO.io.rd_reset := io.rd_reset

  // write part
  asyncFIFO.io.wr.bits := io.wr.bits
  asyncFIFO.io.wr.valid := io.wr.valid
  asyncFIFO.io.wr.enable := io.wr_enable
  io.wr.ready := asyncFIFO.io.wr.not_full

  withClockAndReset(io.rd_clock, io.rd_reset) {
    val readFirstData = Wire(UInt(width.W))
    val readFirstValid = RegInit(false.B)
    val readSecondData = RegInit(0.U(width.W))
    val readSecondValid = RegInit(false.B)
    val readThirdData = RegInit(0.U(width.W))
    val readThirdValid = RegInit(false.B)
    val readValid = RegInit(false.B)
    val readEnableDelay1 = RegNext(io.rd_enable)

    val willUpdateThird = Wire(Bool())
    val willUpdateSecond = Wire(Bool())
    val readFIFOEnable = Wire(Bool())

    readFirstData := asyncFIFO.io.rd.bits
    willUpdateThird := (readSecondValid | readFirstValid) & (!readThirdValid | (io.rd.ready & io.rd.valid))
    willUpdateSecond := readFirstValid & (readSecondValid === willUpdateThird)
    readFIFOEnable := !(readFirstValid & readSecondValid) & asyncFIFO.io.rd.not_empty

    when (! io.rd_enable) {
      readValid := false.B
    } .otherwise {
      when (willUpdateThird | (readThirdValid & !readEnableDelay1)) {
        readValid := true.B
      } .elsewhen(io.rd.ready & io.rd.valid) {
        readValid := false.B
      }
    }

    when(willUpdateThird) {
      readThirdValid := true.B
    }.elsewhen(io.rd.ready & io.rd.valid) {
      readThirdValid := false.B
    }

    when(willUpdateSecond) {
      readSecondValid := true.B
    }.elsewhen(willUpdateThird) {
      readSecondValid := false.B
    }

    when(readFIFOEnable) {
      readFirstValid := true.B
    }.elsewhen(willUpdateSecond | willUpdateThird) {
      readFirstValid := false.B
    }

    when(willUpdateThird) {
      when(readSecondValid) {
        readThirdData := readSecondData
      }.otherwise {
        readThirdData := readFirstData
      }
    }

    when(willUpdateSecond) {
      readSecondData := readFirstData
    }

    asyncFIFO.io.rd.valid := readFIFOEnable
    asyncFIFO.io.rd.enable := 1.U
    io.rd.valid := readValid
    io.rd.bits := readThirdData
  }
}

object AsyncFIFODecoupled extends App {
  Driver.execute(
    Array("-td", Support.MyPath.getSourceDir(AsyncFIFODecoupled.getClass)),
    () => new AsyncFIFODecoupled(1024, 32)
  )
}