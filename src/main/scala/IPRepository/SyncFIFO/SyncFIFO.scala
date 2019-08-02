package IPRepository.SyncFIFO

import chisel3._
import Support.MyMath

class SyncFIFO(val depth: Int, val width: Int) extends Module {

  private val COUNTER_WIDTH = MyMath.clog2(depth)

  val io = IO(new Bundle{
    val wr = new Bundle {
      val data = Input(UInt(width.W))

      val valid = Input(Bool())
      val full = Output(Bool())
    }
    val rd = new Bundle {
      val data = Output(UInt(width.W))
      val valid = Input(Bool())
      val empty = Output(Bool())
    }
  })

  // pointer and enable
  val wrPointer = RegInit(0.U(COUNTER_WIDTH.W))
  val rdPointer = RegInit(0.U(COUNTER_WIDTH.W))
  val wrEnable: Bool = Wire(Bool())
  val rdEnable: Bool = Wire(Bool())

  wrEnable := io.wr.valid & !io.wr.full
  rdEnable := io.rd.valid & !io.rd.empty

  when (wrEnable) {
    wrPointer := wrPointer + 1.U
  }
  when (rdEnable) {
    rdPointer := rdPointer + 1.U
  }

  // data counter and full empty flag
  val counter = RegInit(0.U(COUNTER_WIDTH.W))
  val wrFull = RegInit(false.B)
  val rdEmpty = RegInit(true.B)
  val counterWillDecrease: Bool = Wire(Bool())
  val counterWillIncrease: Bool = Wire(Bool())
  val counterWillBeSame: Bool = Wire(Bool())

  counterWillDecrease := !wrEnable & rdEnable
  counterWillIncrease := wrEnable & !rdEnable
  counterWillBeSame := wrEnable === rdEnable

  when (counterWillIncrease) {
    counter := counter + 1.U
  } .elsewhen(counterWillDecrease) {
    counter := counter - 1.U
  }

  when ((counter === depth.U & counterWillBeSame) | (counter === (depth - 1).U & counterWillIncrease)) {
    wrFull := true.B
  } .otherwise {
    wrFull := false.B
  }

  when ((counter === 0.U & counterWillBeSame) | (counter === 1.U & counterWillDecrease)) {
    rdEmpty := true.B
  } .otherwise {
    rdEmpty := false.B
  }

  io.rd.empty := rdEmpty
  io.wr.full := wrFull

  // write and read data
  val memory = Mem(depth, UInt(width.W))
  val rdData = RegInit(0.U(width.W))

  when (wrEnable) {
    memory.write(wrPointer, io.wr.data)
  }

  when (rdEnable) {
    rdData := memory.read(rdPointer)
  }

  io.rd.data := rdData
}

object SyncFIFO extends App {
  Driver.execute(
    Array("-td", Support.MyPath.getVerilogDir(SyncFIFO.getClass)),
    () => new SyncFIFO(1024, 32)
  )
}