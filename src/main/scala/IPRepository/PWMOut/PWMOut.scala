package IPRepository.PWMOut

import IPRepository.SAXILiteBridge.SAXILiteBridge1
import Interfaces.AxiLite
import chisel3._

class PWMOut(val channelCount: Int) extends Module {

  assert(channelCount > 0)
  assert(channelCount < 33)

  private val AXI_WIDTH = Math.ceil(Math.log(channelCount * 2 + 1) / Math.log(2)).toInt + 2

  val io = IO(new Bundle {
    val s_axi = Flipped(new AxiLite(AXI_WIDTH, 32))
    val pwm = Output(Vec(channelCount, Bool()))
  })

  val bridge = Module(new SAXILiteBridge1(AXI_WIDTH, 4))
  bridge.io.s_axi <> io.s_axi

  val total = RegInit(VecInit(Seq.fill(channelCount)(1.U(32.W))))
  val high = RegInit(VecInit(Seq.fill(channelCount)(0.U(32.W))))
  val total_loaded = RegInit(VecInit(Seq.fill(channelCount)(1.U(32.W))))
  val high_loaded = RegInit(VecInit(Seq.fill(channelCount)(0.U(32.W))))
  val power = RegInit(0.U(channelCount.W))
  val counter = RegInit(VecInit(Seq.fill(channelCount)(0.U(32.W))))

  // counter and pwm out
  for (i <- 0 until channelCount) {
    when(counter(i) === total_loaded(i) - 1.U) {
      counter(i) := 0.U
    }.otherwise {
      counter(i) := counter(i) + 1.U
    }

    io.pwm(i) := high_loaded(i) > counter(i)
  }

  // load value
  for (i <- 0 until channelCount) {
    when(counter(i) === total_loaded(i) - 1.U) {
      when(!power(i).toBool()) {
        total_loaded(i) := 1.U
        high_loaded(i) := 0.U
      }.otherwise {
        total_loaded(i) := total(i)
        high_loaded(i) := high(i)
      }
    }
  }

  // sei read
  val readBuffer = RegInit(0.U(32.W))
  when(bridge.io.s_axi_simplified.rd.addr.valid) {
    when(bridge.io.s_axi_simplified.rd.addr.bits === 0.U) {
      readBuffer := power
    }
    for (i <- 0 until channelCount) {
      when(bridge.io.s_axi_simplified.rd.addr.bits === (i * 2 + 1).U) {
        readBuffer := total_loaded(i)
      }.elsewhen(bridge.io.s_axi_simplified.rd.addr.bits === (i * 2 + 2).U) {
        readBuffer := high_loaded(i)
      }
    }
  }
  bridge.io.s_axi_simplified.rd.data := readBuffer

  // sei write
  when(bridge.io.s_axi_simplified.wr.valid) {
    when(bridge.io.s_axi_simplified.wr.bits.addr === 0.U) {
      power := bridge.io.s_axi_simplified.wr.bits.data
    }
    for (i <- 0 until channelCount) {
      when(bridge.io.s_axi_simplified.wr.bits.addr === (i * 2 + 1).U) {
        total(i) := bridge.io.s_axi_simplified.wr.bits.data
      }.elsewhen(bridge.io.s_axi_simplified.wr.bits.addr === (i * 2 + 2).U) {
        high(i) := bridge.io.s_axi_simplified.wr.bits.data
      }
    }
  }
}

