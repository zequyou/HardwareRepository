package IPRepository.AsyncRx

import Support.MyMath.clog2
import chisel3._
import chisel3.util._

class AsyncRxFixed(val parityBit: Int, val dataBits: Int, val levelPeriod: Int) extends Module {

  assert(parityBit >= 0)
  assert(parityBit <= 2)
  assert(dataBits >= 5)
  assert(dataBits <= 8)

  private val LEVEL_COUNTER_WIDTH = clog2(levelPeriod - 1)
  private val LEVEL_COUNTER_MAX = levelPeriod - 1
  private val LEVEL_COUNTER_MIDDLE = (levelPeriod - 1) / 2
  private val RECV_COUNTER_MAX = dataBits - 1

  val io = IO(new Bundle {
    val m_wr = Decoupled(UInt(dataBits.W))
    val rx = Input(Bool())
  })

  val idle :: head :: recv :: parity :: stop :: Nil = Enum(5)

  val rx1 = RegNext(io.rx)
  val rxBuffer1 = RegNext(rx1)
  val rxBuffer2 = RegNext(rxBuffer1)
  val recvBuffer = RegInit(0.U(dataBits.W))
  val dataBuffer = RegInit(0.U(dataBits.W))
  val dataEnable = RegInit(0.U(1.W))
  val levelCounter = RegInit(0.U(LEVEL_COUNTER_WIDTH.W))
  val recvCounter = RegInit(0.U(3.W))
  val parityBuffer: UInt = if (parityBit == 1) {
    RegInit(1.U(1.W))
  } else if (parityBit == 2) {
    RegInit(0.U(1.W))
  } else {
    RegInit(0.U(1.W))
  }
  val stateMain = RegInit(idle)
  val stateMainBuffer = RegNext(stateMain)

  switch(stateMain) {
    is(idle) {
      when(!rxBuffer1 & rxBuffer2) {
        stateMain := head
      }
    }
    is(head) {
      when(levelCounter === LEVEL_COUNTER_MAX.U) {
        stateMain := recv
      }
    }
    is(recv) {
      when(recvCounter === RECV_COUNTER_MAX.U & levelCounter === LEVEL_COUNTER_MIDDLE.U) {
        if (parityBit == 0) {
          stateMain := stop
        } else {
          stateMain := parity
        }
      }
    }
    is(parity) {
      when(levelCounter === LEVEL_COUNTER_MIDDLE.U) {
        stateMain := stop
      }
    }
    is(stop) {
      when(rxBuffer1) {
        stateMain := idle
      }
    }
  }

  when(stateMain === recv) {
    when(levelCounter === LEVEL_COUNTER_MIDDLE.U) {
      recvBuffer := Cat(recvBuffer(dataBits - 2, 0), rxBuffer1)
    }
  }

  when(stateMain === recv) {
    when(levelCounter === LEVEL_COUNTER_MAX.U) {
      recvCounter := recvCounter + 1.U
    }
  }.elsewhen(stateMain === idle) {
    recvCounter := 0.U
  }

  when(stateMain =/= idle & stateMain =/= stop) {
    when(levelCounter === LEVEL_COUNTER_MAX.U) {
      levelCounter := 0.U
    }.otherwise {
      levelCounter := levelCounter + 1.U
    }
  }.otherwise {
    levelCounter := 0.U
  }

  if (parityBit != 0) {
    when(stateMain === idle) {
      if (parityBit == 2) {
        parityBuffer := 0.U
      } else {
        parityBuffer := 1.U
      }
    }.elsewhen(stateMain === recv | stateMain === parity) {
      when(levelCounter === LEVEL_COUNTER_MIDDLE.U) {
        parityBuffer := parityBuffer ^ rxBuffer1
      }
    }
  }

  when(stateMain === stop) {
    if (parityBit == 0) {
      dataBuffer := recvBuffer
    } else {
      when(parityBuffer === 0.U) {
        dataBuffer := recvBuffer
      }
    }
  }

  when(stateMain === stop & stateMainBuffer =/= stop) {
    if (parityBit == 0) {
      dataEnable := 1.U
    } else {
      when(parityBuffer === 0.U) {
        dataEnable := 1.U
      }
    }
  }.otherwise {
    when(io.m_wr.valid & io.m_wr.ready) {
      dataEnable := 0.U
    }
  }

  io.m_wr.valid := dataEnable
  io.m_wr.bits := dataBuffer
}
