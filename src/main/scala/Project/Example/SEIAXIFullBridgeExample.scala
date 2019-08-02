package Project.Example

import IPRepository.MAXIFullBridge.MAXIFullBridge
import IPRepository.SAXILiteBridge.SAXILiteBridge1
import Interfaces.{AxiFull, AxiLite}
import Support.MyPath
import chisel3._
import chisel3.util._

class SEIAXIFullBridgeExample extends Module {

  val io = IO(new Bundle {
    val m_axi = new AxiFull(32, 32, 0, 0)
    val s_axi = Flipped(new AxiLite(5, 32))
  })

  val masterBridge = Module(new MAXIFullBridge(4))
  val slaveBridge = Module(new SAXILiteBridge1(5, 4))

  masterBridge.io.m_axi <> io.m_axi
  slaveBridge.io.s_axi <> io.s_axi

  val rd_idle :: rd_running :: rd_complete :: Nil = Enum(3)
  val wr_idle :: wr_running :: wr_complete :: Nil = Enum(3)

  val rdLength = RegInit(0.U(8.W))
  val rdAddress = RegInit(0.U(32.W))
  val rdTrigger = RegInit(false.B)
  val rdState = RegInit(rd_idle)
  val wrLength = RegInit(0.U(8.W))
  val wrAddress = RegInit(0.U(32.W))
  val wrTrigger = RegInit(false.B)
  val wrState = RegInit(wr_idle)
  val wrCounter = RegInit(0.U(9.W))
  val regRdBuffer = RegInit(0.U(32.W))

  // register read and write
  when (slaveBridge.io.s_axi_simplified.wr.valid) {
    when (slaveBridge.io.s_axi_simplified.wr.bits.addr === 0.U) {
      rdLength := slaveBridge.io.s_axi_simplified.wr.bits.data
    } .elsewhen(slaveBridge.io.s_axi_simplified.wr.bits.addr === 1.U) {
      rdAddress := slaveBridge.io.s_axi_simplified.wr.bits.data
    } .elsewhen(slaveBridge.io.s_axi_simplified.wr.bits.addr === 2.U) {
      wrLength := slaveBridge.io.s_axi_simplified.wr.bits.data
    } .elsewhen(slaveBridge.io.s_axi_simplified.wr.bits.addr === 3.U) {
      wrAddress := slaveBridge.io.s_axi_simplified.wr.bits.data
    }
  }

  slaveBridge.io.s_axi_simplified.rd.data := regRdBuffer

  when (slaveBridge.io.s_axi_simplified.rd.addr.valid) {
    when (slaveBridge.io.s_axi_simplified.rd.addr.bits === 0.U) {
      regRdBuffer := rdLength
    } .elsewhen(slaveBridge.io.s_axi_simplified.rd.addr.bits === 1.U) {
      regRdBuffer := rdAddress
    } .elsewhen(slaveBridge.io.s_axi_simplified.rd.addr.bits === 2.U) {
      regRdBuffer := wrLength
    } .elsewhen(slaveBridge.io.s_axi_simplified.rd.addr.bits === 3.U) {
      regRdBuffer := wrAddress
    } .elsewhen(slaveBridge.io.s_axi_simplified.rd.addr.bits === 4.U) {
      regRdBuffer := Cat(wrTrigger, rdTrigger)
    }
  }

  // axi read signals
  switch(rdState) {
    is(rd_idle) {
      when (rdTrigger) {
        rdState := rd_running
      }
    }
    is(rd_running) {
      when (masterBridge.io.m_axi_simplified.rcmd.ready) {
        rdState := rd_complete
      }
    }
    is(rd_complete) {
      rdState := rd_idle
    }
  }

  when (slaveBridge.io.s_axi_simplified.wr.valid) {
    when (slaveBridge.io.s_axi_simplified.wr.bits.addr === 4.U) {
      rdTrigger := slaveBridge.io.s_axi_simplified.wr.bits.data(0)
    }
  } .elsewhen(rdState === rd_complete) {
    rdTrigger := 0.U
  }

  masterBridge.io.m_axi_simplified.rcmd.bits.addr := rdAddress
  masterBridge.io.m_axi_simplified.rcmd.bits.len := rdLength
  masterBridge.io.m_axi_simplified.rcmd.bits.burst := 1.U
  masterBridge.io.m_axi_simplified.rcmd.valid := rdState === rd_running
  masterBridge.io.m_axi_simplified.rdata.ready := rdState === rd_running


  // axi write signals
  switch(wrState) {
    is (wr_idle) {
      when (wrTrigger) {
        wrState := wr_running
      }
    }
    is (wr_running) {
      when (masterBridge.io.m_axi_simplified.wcmd.ready) {
        wrState := wr_complete
      }
    }
    is (wr_complete) {
      wrState := wr_idle
    }
  }

  when (slaveBridge.io.s_axi_simplified.wr.valid) {
    when (slaveBridge.io.s_axi_simplified.wr.bits.addr === 4.U) {
      wrTrigger := slaveBridge.io.s_axi_simplified.wr.bits.data(1)
    }
  } .elsewhen(wrState === wr_complete) {
    wrTrigger := 0.U
  }

  when (wrState === wr_idle) {
    wrCounter := 0.U
  } .elsewhen(wrState === wr_running &
    masterBridge.io.m_axi_simplified.wdata.ready &
    masterBridge.io.m_axi_simplified.wdata.valid
  ) {
    wrCounter := wrCounter + 1.U
  }

  masterBridge.io.m_axi_simplified.wcmd.bits.addr := wrAddress
  masterBridge.io.m_axi_simplified.wcmd.bits.burst := 1.U
  masterBridge.io.m_axi_simplified.wcmd.bits.len := wrLength
  masterBridge.io.m_axi_simplified.wcmd.valid := wrState === rd_running

  masterBridge.io.m_axi_simplified.wdata.bits.data := wrCounter
  masterBridge.io.m_axi_simplified.wdata.bits.strb := 15.U
  masterBridge.io.m_axi_simplified.wdata.valid := wrCounter <= wrLength & wrState === wr_running
}

object Generator extends App {

  println("Generating the SEIAXIFullBridgeExample hardware")
  chisel3.Driver.execute(Array("--target-dir", MyPath.getSourceDir(Generator.getClass)), () => new SEIAXIFullBridgeExample())

}