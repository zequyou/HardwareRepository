package Interfaces

import chisel3._

class Uart extends Bundle {
  val tx = Output(Bool())
  val rx = Input(Bool())
}
