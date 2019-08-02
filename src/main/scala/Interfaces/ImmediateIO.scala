package Interfaces

import chisel3._

class ImmediateIO[T <: chisel3.Data](gen: T) extends Bundle {
  val valid = Output(Bool())
  val bits = Output(gen)
}

