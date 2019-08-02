package IPRepository.AXI

import IPRepository.SAXILiteBridge.SAXILiteBridge1
import Interfaces.AxiLite
import chisel3._

class AXIPWMIn(val channelCount: Int, val period: Int) extends Module {

  assert(channelCount > 0)
  assert(channelCount < 33)

  private val AXI_WIDTH = Math.ceil(Math.log(channelCount) / Math.log(2)).toInt + 2

  val io = IO(new Bundle {
    val s_axi = Flipped(new AxiLite(AXI_WIDTH, 32))
    val pwm = Input(Vec(channelCount, Bool()))
  })

  val bridge = Module(new SAXILiteBridge1(AXI_WIDTH, 4))
  bridge.io.s_axi <> io.s_axi

  val power = RegInit(0.U(channelCount.W))
  val counter_period = RegInit(VecInit(Seq.fill(channelCount)(1.U(32.W))))
  val counter_high = RegInit(VecInit(Seq.fill(channelCount)(0.U(32.W))))
  val result_high = RegInit(VecInit(Seq.fill(channelCount)(0.U(32.W))))
  val pwmDelay1 = RegInit(VecInit(Seq.fill(channelCount)(0.U(1.W))))
  val pwmCache1 = RegInit(VecInit(Seq.fill(channelCount)(0.U(1.W))))
  val pwmCache2 = RegInit(VecInit(Seq.fill(channelCount)(0.U(1.W))))

  // counter period will begin to count when power on
  for (i <- 0 until channelCount) {
    when (power(i).toBool()) {
      when (counter_period(i) === period.U) {
        counter_period(i) := 1.U
      } otherwise {
        counter_period(i) := counter_period(i) + 1.U
      }
    } .otherwise {
      counter_period(i) := 1.U
    }
  }

  // counter high
  for (i <- 0 until channelCount) {
    when (power(i).toBool()) {
      when (counter_period(i) === period.U) {
        counter_high(i) := 0.U
      } .elsewhen(pwmCache2(i) === 1.U) {
        counter_high(i) := counter_high(i) + 1.U
      }
    } .otherwise {
      counter_high(i) := 0.U
    }
  }

  // result
  for (i <- 0 until channelCount) {
    when(power(i).toBool() & counter_period(i) === period.U) {
      result_high := counter_high
    }
  }

  // pwm cache and delay
  for (i <- 0 until channelCount) {
    pwmDelay1(i) := io.pwm(i)
    pwmCache1(i) := pwmDelay1(i)
    pwmCache2(i) := pwmCache1(i)
  }

  // sei read
  val readBuffer = RegInit(0.U(32.W))
  when(bridge.io.s_axi_simplified.rd.addr.valid) {
    for (i <- 0 until channelCount) {
      when(bridge.io.s_axi_simplified.rd.addr.bits === i.U) {
        readBuffer := result_high(i)
      }
    }
  }
  bridge.io.s_axi_simplified.rd.data := readBuffer

  // sei write
  when(bridge.io.s_axi_simplified.wr.valid & bridge.io.s_axi_simplified.wr.bits.addr === 0.U) {
    power := bridge.io.s_axi_simplified.wr.bits.data
  }
}

