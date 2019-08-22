package Interfaces


object AXIFull {
  def apply(addrWidth: Int, dataSize: Int): AXIFullIO = {
    new AXIFullIO(addrWidth, dataSize)
  }

  def apply(addrWidth: Int, dataSize: Int, IDWidth: Int, userWidth: Int): AXIFullIO = {
    new AXIFullIO(addrWidth, dataSize, IDWidth, userWidth)
  }
}

