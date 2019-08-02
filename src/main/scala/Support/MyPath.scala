package Support

object MyPath {

  def getSourceDir(myClass: Class[_]): String = {
    println(getCompliedDir(myClass))
    getCompliedDir(myClass).replaceFirst("/target/scala-\\d*.\\d*/classes", "/src/main/scala")
  }

  def getCompliedDir(myClass: Class[_]): String = {
    myClass.getResource("").getPath
  }

  def getVerilogDir(myClass: Class[_]): String = {
    getSourceDir(myClass).replaceFirst("/src/main/scala", "/src/main/verilog")
  }

  def main(args: Array[String]): Unit = {
    println(getSourceDir(MyPath.getClass))
  }
}