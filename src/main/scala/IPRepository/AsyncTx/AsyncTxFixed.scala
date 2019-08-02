package IPRepository.AsyncTx

import chisel3._
import chisel3.util._
import Support.MyMath._

class AsyncTxFixed(val parityBit: Int, val stopBits: Int, val dataBits: Int, val levelPeriod: Int) extends Module {

  assert(parityBit >= 0)
  assert(parityBit <= 2)
  assert(stopBits >= 0)
  assert(stopBits <= 2)
  assert(dataBits >= 5)
  assert(dataBits <= 8)

  private val LEVEL_COUNTER_WIDTH = clog2(levelPeriod - 1)

  val io = IO(new Bundle {
    val s_wr = Flipped(Decoupled(UInt(dataBits.W)))
    val tx = Output(Bool())
  })

  val idle :: head :: send :: parity :: stop1 :: stop2 :: stop3 :: Nil = Enum(7)
  val levelCounter = RegInit(0.U(LEVEL_COUNTER_WIDTH.W))
  val dataCounter = RegInit(0.U(3.W))
  val dataBuffer = RegInit(0.U(dataBits.W))
  val txBuffer = RegInit(1.U(1.W))
  val parityBuffer: UInt = if (parityBit == 1) {
    RegInit(1.U(1.W))
  } else if (parityBit == 2) {
    RegInit(0.U(1.W))
  } else {
    RegInit(0.U(1.W))
  }
  val stateMain = RegInit(idle)

  switch(stateMain) {
    is(idle) {
      when(io.s_wr.valid) {
        stateMain := head
      }
    }
    is(head) {
      when(levelCounter === (levelPeriod - 1).U) {
        stateMain := send
      }
    }
    is(send) {
      when(levelCounter === (levelPeriod - 1).U & dataCounter === (dataBits - 1).U) {
        if (parityBit > 0) {
          stateMain := parity
        } else {
          stateMain := stop1
        }
      }
    }
    is(parity) {
      when(levelCounter === (levelPeriod - 1).U) {
        stateMain := stop1
      }
    }
    is(stop1) {
      when(levelCounter === (levelPeriod - 1).U) {
        if (stopBits > 0) {
          stateMain := stop2
        } else {
          stateMain := idle
        }
      }
    }
    is(stop2) {
      when(levelCounter === ((levelPeriod - 1) / 2).U) {
        if (stopBits > 1) {
          stateMain := stop3
        } else {
          stateMain := idle
        }
      }
    }
    is(stop3) {
      when(levelCounter === (levelPeriod - 1).U) {
        stateMain := idle
      }
    }
  }

  when (stateMain =/= idle) {
    when (levelCounter === (levelPeriod - 1).U) {
      levelCounter := 0.U
    } .otherwise {
      levelCounter := levelCounter + 1.U
    }
  }

  when (stateMain === idle) {
    dataCounter := 0.U
  } .elsewhen(stateMain === send) {
    when (levelCounter === (levelPeriod - 1).U) {
      dataCounter := dataCounter + 1.U
    }
  }

  if (parityBit == 1) {
    when (stateMain === idle) {
      parityBuffer := 1.U
    } .elsewhen (stateMain === send) {
      when (levelCounter === (levelPeriod - 1).U) {
        parityBuffer := parityBuffer ^ dataBuffer(dataBits - 1)
      }
    }
  } else if (parityBit == 2) {
    when (stateMain === idle) {
      parityBuffer := 0.U
    } .elsewhen (stateMain === send) {
      when (levelCounter === (levelPeriod - 1).U) {
        parityBuffer := parityBuffer ^ dataBuffer(dataBits - 1)
      }
    }
  }

  when (stateMain === idle) {
    when (io.s_wr.valid) {
      dataBuffer := io.s_wr.bits
    }
  } .elsewhen(stateMain === send) {
    when (levelCounter === (levelPeriod - 1).U) {
      dataBuffer := Cat(dataBuffer(dataBits - 1, 0), 0.U)
    }
  }

  if (parityBit == 0) {
    when (stateMain === send) {
      txBuffer := dataBuffer(dataBits - 1)
    } .elsewhen(stateMain === head) {
      txBuffer := 0.U
    } otherwise {
      txBuffer := 1.U
    }
  } else {
    when (stateMain === send) {
      txBuffer := dataBuffer(dataBits - 1)
    } .elsewhen(stateMain === head) {
      txBuffer := 0.U
    } .elsewhen(stateMain === parity) {
      txBuffer := parityBuffer
    } .otherwise {
      txBuffer := 1.U
    }
  }
  io.tx := txBuffer

  if (stopBits == 0) {
    io.s_wr.ready := stateMain === stop1 & levelCounter === (levelPeriod - 1).U
  } else if (stopBits == 1) {
    io.s_wr.ready := stateMain === stop2 & levelCounter === ((levelPeriod - 1) / 2).U
  } else {
    io.s_wr.ready := stateMain === stop3 & levelCounter === (levelPeriod - 1).U
  }
}
