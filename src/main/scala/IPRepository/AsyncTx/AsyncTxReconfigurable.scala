package IPRepository.AsyncTx

import Interfaces.Immediate
import chisel3._
import chisel3.util._

// 15 - 00 counter
// 17 - 16 bits
// 19 - 18 parity
// 21 - 20 stop

class AsyncTxReconfigurable extends Module {

  val io = IO(new Bundle {
    val s_wr_setting = Flipped(Immediate(UInt(32.W)))
    val s_wr_data = Flipped(Decoupled(UInt(8.W)))
    val tx = Output(Bool())
  })

  val idle :: head :: send :: parity :: stop1 :: stop2 :: stop3 :: Nil = Enum(7)
  val levelCounter = RegInit(0.U(16.W))
  val dataCounter = RegInit(0.U(3.W))
  val setting = RegInit(0.U(32.W))
  val dataBuffer = RegInit(0.U(8.W))
  val parityBuffer = RegInit(0.U(1.W))
  val txBuffer = RegInit(0.U(1.W))
  val dataComplete = RegInit(false.B)
  val stateMain = RegInit(idle)
  val stateMainDelay1 = RegNext(stateMain)

  when (io.s_wr_setting.valid) {
    setting := io.s_wr_setting.bits
  }

  switch(stateMain) {
    is(idle) {
      when (io.s_wr_data.valid) {
        stateMain := head
      }
    }
    is(head) {
      when (levelCounter === setting(15, 0)) {
        stateMain := send
      }
    }
    is(send) {
      when ((dataCounter === 5.U + setting(17, 16)) & (levelCounter === setting(15, 0))) {
        when (setting(18)) {
          stateMain := parity
        } .otherwise {
          stateMain := stop1
        }
      }
    }
    is(parity) {
      when (levelCounter === setting(15, 0)) {
        stateMain := stop1
      }
    }
    is(stop1) {
      when (levelCounter === setting(15, 0)) {
        when (setting(20)) {
          stateMain := stop2
        } .otherwise {
          stateMain := idle
        }
      }
    }
    is(stop2) {
      when (levelCounter(14, 0) === setting(14, 0)) {
        when (setting(21)) {
          stateMain := stop3
        } .otherwise {
          stateMain := idle
        }
      }
    }
    is(stop3) {
      when (levelCounter === setting(15, 0)) {
        stateMain := idle
      }
    }
  }

  when (stateMain =/= idle) {
    when (levelCounter === setting(15, 0)) {
      levelCounter := 0.U
    } .otherwise {
      levelCounter := levelCounter + 1.U
    }
  }

  when (stateMain === idle) {
    dataCounter := 0.U
  } .elsewhen(stateMain === send) {
    when (levelCounter === setting(15, 0)) {
      dataCounter := dataCounter + 1.U
    }
  }

  when (stateMain === idle) {
    parityBuffer := setting(19)
  } .elsewhen (stateMain === send) {
    when (levelCounter === setting(15, 0)) {
      parityBuffer := parityBuffer ^ dataBuffer(7)
    }
  }

  when (stateMain === head) {
    txBuffer := 0.U
  } .elsewhen(stateMain === send) {
    txBuffer := dataBuffer(7)
  } .elsewhen(stateMain === parity) {
    txBuffer := parityBuffer
  } .otherwise {
    txBuffer := 1.U
  }
  txBuffer := io.tx

  when (stateMain === idle & stateMainDelay1 =/= idle) {
    dataComplete := true.B
  } .otherwise {
    dataComplete := false.B
  }

  io.s_wr_data.ready := dataComplete
}
