package IPRepository.SAXILiteBridge

object SAXILiteBridgeGenerator extends App {

  println("Generating the SAXILiteBridge hardware")
  chisel3.Driver.execute(Array("--target-dir", "generated"),
    () => new SAXILiteBridge(4, 4, Map("FLOW_CTRL" -> false)))

}
