package IPRepository.CrossClockIrrevocable

import IPRepository.CrossClockBuffer.CrossClockBuffer
import chisel3._
import chisel3.core.withClockAndReset
import chisel3.internal.naming.chiselName
import chisel3.util._

@chiselName
class CrossClockIrrevocable[T <: chisel3.Data](gen: T) extends Module {

  val io = IO(new Bundle {
    val src = Flipped(Irrevocable(gen))
    val dst = Irrevocable(gen)
    val src_clock = Input(Clock())
    val src_reset = Input(Bool())
    val dst_clock = Input(Clock())
    val dst_reset = Input(Bool())
  })

  val ackWire: Bool = Wire(Bool())
  val reqWire: Bool = Wire(Bool())
  val ackInSrc: Bool = Wire(Bool())
  val reqInDst: Bool = Wire(Bool())

  // sync ack from dst to src
  val ackCrossClockBuffer = Module(new CrossClockBuffer(Bool()))
  ackCrossClockBuffer.io.src := ackWire
  ackCrossClockBuffer.io.dst_clock := io.src_clock
  ackCrossClockBuffer.io.dst_reset := io.src_reset
  ackInSrc := ackCrossClockBuffer.io.dst

  // sync req from src to dst
  val reqCrossClockBuffer = Module(new CrossClockBuffer(Bool()))
  reqCrossClockBuffer.io.src := reqWire
  reqCrossClockBuffer.io.dst_clock := io.dst_clock
  reqCrossClockBuffer.io.dst_reset := io.dst_reset
  reqInDst := reqCrossClockBuffer.io.dst

  withClockAndReset(io.src_clock, io.src_reset) {
    val running = RegInit(false.B)
    val req = RegInit(false.B)
    val ackInSrcDelay1 = RegNext(ackInSrc)
    val srcReady = RegInit(false.B)

    when (io.src.valid & !running) {
      running := true.B
    } .elsewhen(io.src.valid & io.src.ready) {
      running := false.B
    }

    when (!ackInSrc & ackInSrcDelay1) {
      // assert when ack is falling
      srcReady := true.B
    } .otherwise {
      srcReady := false.B
    }

    when (io.src.valid & !running) {
      req := true.B
    } .elsewhen(ackInSrc) {
      req := false.B
    }

    reqWire := req
    io.src.ready := srcReady
  }

  withClockAndReset(io.dst_clock, io.dst_reset) {
    val ack = RegInit(false.B)
    val reqInDstDelay1 = RegNext(reqInDst)
    val dstValid = RegInit(false.B)

    when (io.dst.valid & io.dst.ready) {
      ack := true.B
    } .elsewhen(!reqInDst & reqInDstDelay1) {
      // deassert when req is falling
      ack := false.B
    }

    when (reqInDst & !reqInDstDelay1) {
      // assert when req is rising
      dstValid := true.B
    } .elsewhen(io.dst.valid & io.dst.ready) {
      dstValid := false.B
    }

    ackWire := ack
    io.dst.valid := dstValid
    io.dst.bits := io.src.bits
  }
}

object CrossClockIrrevocable extends App {
  Driver.execute(
    Array("-td", Support.MyPath.getVerilogDir(CrossClockIrrevocable.getClass)),
    () => new CrossClockIrrevocable(UInt(8.W))
  )
}