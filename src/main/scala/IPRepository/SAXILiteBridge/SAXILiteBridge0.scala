
package IPRepository.SAXILiteBridge

import Interfaces.{AxiLite, AxiLiteSimplified}
import chisel3._


class SAXILiteBridge0(addrWidth: Int, dataSize: Int) extends Module {

  val ADDR_WIDTH: Int = addrWidth - (Math.log(dataSize) / Math.log(2)).toInt
  val DATA_WIDTH: Int = dataSize * 8

  val io = IO(new Bundle {
    val s_axi = Flipped(new AxiLite(addrWidth, dataSize))
    val s_axi_simplified = new AxiLiteSimplified(ADDR_WIDTH, dataSize)
  })

  io.s_axi_simplified.wr.bits.strb := io.s_axi.wstrb

  // write start here
  val writeAddrComplete = RegInit(false.B)
  val writeAddrAcceptable = RegInit(false.B)
  val writeDataComplete = RegInit(false.B)
  val writeDataAcceptable = RegInit(false.B)
  val writeAddrBuffer = RegInit(0.U(ADDR_WIDTH.W))
  val writeDataBuffer = RegInit(0.U(DATA_WIDTH.W))

  when(io.s_axi.bvalid & io.s_axi.bready) {
    writeAddrComplete := false.B
    writeAddrAcceptable := true.B
  }.elsewhen(io.s_axi.awvalid & io.s_axi.awready) {
    writeAddrComplete := true.B
    writeAddrAcceptable := false.B
  }

  when(io.s_axi.bvalid & io.s_axi.bready) {
    writeDataComplete := false.B
    writeDataAcceptable := true.B
  }.elsewhen(io.s_axi.wvalid & io.s_axi.wready) {
    writeDataComplete := true.B
    writeDataAcceptable := false.B
  }

  when (io.s_axi.wvalid & io.s_axi.wready) {
    writeDataBuffer := io.s_axi.wdata
  }

  when(io.s_axi.awvalid & io.s_axi.awready) {
    writeAddrBuffer := io.s_axi.awaddr(addrWidth - 1, addrWidth - ADDR_WIDTH)
  }

  io.s_axi.awready := writeAddrAcceptable
  io.s_axi.wready := writeDataAcceptable
  io.s_axi.bvalid := io.s_axi_simplified.wr.valid & io.s_axi_simplified.wr.ready
  io.s_axi.bresp := 0.U(2.W)

  io.s_axi_simplified.wr.bits.addr := writeAddrBuffer
  io.s_axi_simplified.wr.bits.data := writeDataBuffer
  io.s_axi_simplified.wr.valid := writeAddrComplete & writeDataComplete


  // read start here
  val readAddrAcceptable = RegInit(true.B)
  val readDataComplete = RegInit(false.B)
  val readAddrBuffer = RegInit(0.U(ADDR_WIDTH.W))
  val readDataBuffer = RegInit(0.U((dataSize * 8).W))
  val readRequest = RegInit(false.B)

  when(io.s_axi.rvalid & io.s_axi.rready) {
    readAddrAcceptable := true.B
  }.elsewhen(io.s_axi.arvalid & io.s_axi.arready) {
    readAddrAcceptable := false.B
  }

  when(io.s_axi.rvalid & io.s_axi.rready) {
    readDataComplete := false.B
  }.elsewhen(io.s_axi_simplified.rd.addr.valid & io.s_axi_simplified.rd.addr.ready) {
    readDataComplete := true.B
  }

  when(io.s_axi.arvalid & io.s_axi.arready) {
    readAddrBuffer := io.s_axi.araddr(addrWidth - 1, addrWidth - ADDR_WIDTH)
  }

  when (io.s_axi_simplified.rd.addr.valid & io.s_axi_simplified.rd.addr.ready) {
    readDataBuffer := io.s_axi_simplified.rd.data
  }

  when (io.s_axi_simplified.rd.addr.valid & io.s_axi_simplified.rd.addr.ready) {
    readRequest := false.B
  } .elsewhen(io.s_axi.arvalid) {
    readRequest := true.B
  }

  io.s_axi.arready := readAddrAcceptable
  io.s_axi.rdata := Mux(readDataComplete, readDataBuffer, io.s_axi_simplified.rd.data)
  io.s_axi.rvalid := Mux(readDataComplete, true.B, io.s_axi_simplified.rd.addr.valid)
  io.s_axi.rresp := 0.U

  io.s_axi_simplified.rd.addr.bits := Mux(readRequest, readAddrBuffer, io.s_axi.araddr(addrWidth - 1, addrWidth - ADDR_WIDTH))
  io.s_axi_simplified.rd.addr.valid := Mux(readRequest, true.B, io.s_axi.arvalid & io.s_axi.arready)

}
