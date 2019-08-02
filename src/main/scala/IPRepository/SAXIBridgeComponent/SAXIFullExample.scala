package IPRepository.SAXIBridgeComponent

import Interfaces.AxiFull
import chisel3._

class SAXIFullExample extends Module {

  val io = IO(new Bundle {
    val s_axi = Flipped(new AxiFull(6, 4, 0, 0))
  })

  // memory
  val memory = Mem(16, UInt(32.W))

  // write part
  io.s_axi.awready := WriteAddrReady(
    awvalid = io.s_axi.awvalid,
    bvalid = io.s_axi.bvalid,
    bready = io.s_axi.bready
  )
  io.s_axi.wready := WriteDataReady(
    awvalid = io.s_axi.awvalid,
    awready = io.s_axi.awready,
    wvalid = io.s_axi.wvalid,
    wlast = io.s_axi.wlast
  )
  io.s_axi.bresp := 0.U
  io.s_axi.bvalid := WriteRespValid(
    wvalid = io.s_axi.wvalid,
    wready = io.s_axi.wready,
    wlast = io.s_axi.wlast,
    bready = io.s_axi.bready
  )

  val writeAddrBuffer = RegInit(0.U(4.W))

  when(io.s_axi.awready & io.s_axi.awready) {
    writeAddrBuffer := io.s_axi.awaddr(5, 2)
  }.elsewhen(io.s_axi.wvalid & io.s_axi.wready) {
    writeAddrBuffer := writeAddrBuffer + 1.U
  }

  when(io.s_axi.wvalid & io.s_axi.wready) {
    memory.write(writeAddrBuffer, io.s_axi.wdata)
  }

  // read part
  val readCounterExp = ReadCounterExp(
    arvalid = io.s_axi.arvalid,
    arready = io.s_axi.arready,
    arlen = io.s_axi.arlen
  )

  val readCounterAct = ReadCounterAct(
    rvalid = io.s_axi.rvalid,
    rready = io.s_axi.rready,
    rlast = io.s_axi.rlast
  )

  io.s_axi.arready := ReadAddrReady(
    arvalid = io.s_axi.arvalid,
    rvalid = io.s_axi.rvalid,
    rready = io.s_axi.rready,
    rlast = io.s_axi.rlast
  )
  io.s_axi.rlast := ReadLast(
    arvalid = io.s_axi.arvalid,
    arready = io.s_axi.arready,
    arlen = io.s_axi.arlen,
    rvalid = io.s_axi.rvalid,
    rready = io.s_axi.rready,
    rExp = readCounterExp,
    rAct = readCounterAct
  )

  val readAddrBuffer = RegInit(0.U(4.W))
  val readAddrBufferValid = RegInit(false.B)
  val readDataBuffer = RegInit(0.U(32.W))
  val readDataBufferValid = RegInit(false.B)
  val readEnable: Bool = Wire(Bool())

  readEnable := (!io.s_axi.rvalid | io.s_axi.rready) & !io.s_axi.arready

  when(io.s_axi.arvalid & io.s_axi.arready) {
    readAddrBuffer := io.s_axi.araddr(5, 2)
  }.elsewhen(readEnable) {
    readAddrBuffer := readAddrBuffer + 1.U
  }

  when (io.s_axi.arvalid & io.s_axi.arready) {
    readAddrBufferValid := true.B
  } .elsewhen(io.s_axi.rvalid & io.s_axi.rready & io.s_axi.rlast) {
    readAddrBufferValid := false.B
  }

  when (readEnable) {
    readDataBuffer := memory.read(readAddrBuffer)
  }

  when (readAddrBufferValid & !(io.s_axi.rvalid & io.s_axi.rready & io.s_axi.rlast)) {
    readDataBufferValid := true.B
  } .otherwise {
    readDataBufferValid := false.B
  }

  io.s_axi.rvalid := readDataBufferValid
  io.s_axi.rdata := readDataBuffer
  io.s_axi.rresp := 0.U
}


object SAXIFullExample extends App {

  println("Generating the SAXIFullExample hardware")
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new SAXIFullExample())
}
