package IPRepository.MAXIFullBridge

object MAXIFullBridgeGenerator extends App {

  println("Generating the MAXIFullBridge hardware")
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new MAXIFullBridge(4))

}

