package Support

object MyMath {

  def clog2(source: Int): Int = {
    Math.ceil(Math.log(source) / Math.log(2)).toInt
  }

  def toGrey(source: Int): Int = {
    source + (source << 1)
  }
}