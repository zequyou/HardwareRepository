package Interfaces

object Immediate {
  def apply[T <: chisel3.Data](gen: T): ImmediateIO[T] = new ImmediateIO(gen)
}
