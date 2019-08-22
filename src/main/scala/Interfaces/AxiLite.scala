package Interfaces

object AXILite {
  def apply(addrWidth: Int, dataSize: Int): AXILiteIO = {
    new AXILiteIO(addrWidth, dataSize)
  }
}
