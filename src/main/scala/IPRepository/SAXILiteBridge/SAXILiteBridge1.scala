
package IPRepository.SAXILiteBridge

import Interfaces.{AxiLite, AxiLiteSimplified}
import chisel3.{when, _}


class SAXILiteBridge1(addrWidth: Int, dataSize: Int) extends Module {

  val ADDR_WIDTH: Int = addrWidth - (Math.log(dataSize) / Math.log(2)).toInt
  val DATA_WIDTH: Int = dataSize * 8

  val io = IO(new Bundle {
    val s_axi = Flipped(new AxiLite(addrWidth, dataSize))
    val s_axi_simplified = new AxiLiteSimplified(ADDR_WIDTH, dataSize)
  })

  io.s_axi_simplified.wr.bits.strb := io.s_axi.wstrb

  // write start here
  val writeAddrComplete = RegInit(false.B)
  val writeDataComplete = RegInit(false.B)
  val writeAddrAcceptable = RegInit(true.B)
  val writeDataAcceptable = RegInit(true.B)

  val writeAddr = RegInit(0.U(ADDR_WIDTH.W))
  val writeData = RegInit(0.U(DATA_WIDTH.W))
  val writeValid = RegInit(false.B)

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

  when (io.s_axi.awvalid & io.s_axi.awready) {
    if (ADDR_WIDTH != 0) {
      writeAddr := io.s_axi.awaddr(addrWidth - 1, addrWidth - ADDR_WIDTH)
    } else {
      writeAddr := 0.U
    }
  }

  when (io.s_axi.wvalid & io.s_axi.wready) {
    writeData := io.s_axi.wdata
  }

  when(writeValid) {
    writeValid := false.B
  }.elsewhen(io.s_axi.bvalid & io.s_axi.bready) {
    writeValid := true.B
  }

  io.s_axi.awready := writeAddrAcceptable
  io.s_axi.wready := writeDataAcceptable
  io.s_axi.bvalid := writeAddrComplete & writeDataComplete
  io.s_axi.bresp := 0.U(2.W)

  io.s_axi_simplified.wr.bits.addr := writeAddr
  io.s_axi_simplified.wr.bits.data := writeData
  io.s_axi_simplified.wr.valid := writeValid

  // read start here
  val readAddrAcceptable = RegInit(true.B)
  val readDataComplete = RegInit(false.B)
  val readDataEnable = RegInit(false.B)
  val readRequestDelay = RegNext(io.s_axi_simplified.rd.addr.valid)
  val readDataBuffer: UInt = RegInit(0.U((dataSize * 8).W))

  when(io.s_axi.rvalid & io.s_axi.rready) {
    readAddrAcceptable := true.B
  }.elsewhen(io.s_axi.arvalid & io.s_axi.arready) {
    readAddrAcceptable := false.B
  }

  when(io.s_axi.rvalid & io.s_axi.rready) {
    readDataComplete := false.B
  }.elsewhen(readRequestDelay) {
    readDataComplete := true.B
  }

  when(io.s_axi.rvalid & io.s_axi.rready) {
    readDataEnable := false.B
  }.elsewhen(io.s_axi_simplified.rd.addr.valid) {
    readDataEnable := true.B
  }

  when(readRequestDelay) {
    readDataBuffer := io.s_axi_simplified.rd.data
  }

  io.s_axi.arready := readAddrAcceptable
  io.s_axi.rdata := Mux(readDataComplete, readDataBuffer, io.s_axi_simplified.rd.data)
  io.s_axi.rvalid := readDataEnable
  io.s_axi.rresp := 0.U

  if (ADDR_WIDTH != 0) {
    io.s_axi_simplified.rd.addr.bits := io.s_axi.araddr(addrWidth - 1, addrWidth - ADDR_WIDTH)
  } else {
    io.s_axi_simplified.rd.addr.bits := 0.U
  }
  io.s_axi_simplified.rd.addr.valid := io.s_axi.arvalid & io.s_axi.arready
}
