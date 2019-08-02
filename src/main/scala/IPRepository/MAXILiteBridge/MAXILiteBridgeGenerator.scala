package IPRepository.MAXILiteBridge

object MAXILiteBridgeGenerator extends App {

  println("Generating the MAXILiteBridge hardware")
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new MAXILiteBridge(4))
}
