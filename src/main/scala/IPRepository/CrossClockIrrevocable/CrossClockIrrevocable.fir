;buildInfoPackage: chisel3, version: 3.1.8, scalaVersion: 2.11.12, sbtVersion: 1.1.1, builtAtString: 2019-07-08 17:44:42.884, builtAtMillis: 1562607882884
circuit CrossClockIrrevocable : 
  module CrossClockBuffer : 
    input clock : Clock
    input reset : UInt<1>
    output io : {flip src : UInt<1>, dst : UInt<1>, flip dst_clock : Clock, flip dst_reset : UInt<1>}
    
    reg innerBuffer : UInt<1>[2], io.dst_clock @[CrossClockBuffer.scala 20:26]
    innerBuffer[0] <= io.src @[CrossClockBuffer.scala 22:20]
    io.dst <= innerBuffer[1] @[CrossClockBuffer.scala 23:12]
    innerBuffer[1] <= innerBuffer[0] @[CrossClockBuffer.scala 26:22]
    
  module CrossClockBuffer_1 : 
    input clock : Clock
    input reset : UInt<1>
    output io : {flip src : UInt<1>, dst : UInt<1>, flip dst_clock : Clock, flip dst_reset : UInt<1>}
    
    reg innerBuffer : UInt<1>[2], io.dst_clock @[CrossClockBuffer.scala 20:26]
    innerBuffer[0] <= io.src @[CrossClockBuffer.scala 22:20]
    io.dst <= innerBuffer[1] @[CrossClockBuffer.scala 23:12]
    innerBuffer[1] <= innerBuffer[0] @[CrossClockBuffer.scala 26:22]
    
  module CrossClockIrrevocable : 
    input clock : Clock
    input reset : UInt<1>
    output io : {flip src : {flip ready : UInt<1>, valid : UInt<1>, bits : UInt<8>}, dst : {flip ready : UInt<1>, valid : UInt<1>, bits : UInt<8>}, flip src_clock : Clock, flip src_reset : UInt<1>, flip dst_clock : Clock, flip dst_reset : UInt<1>}
    
    wire ackWire : UInt<1> @[CrossClockIrrevocable.scala 21:27]
    wire reqWire : UInt<1> @[CrossClockIrrevocable.scala 22:27]
    wire ackInSrc : UInt<1> @[CrossClockIrrevocable.scala 23:28]
    wire reqInDst : UInt<1> @[CrossClockIrrevocable.scala 24:28]
    inst ackCrossClockBuffer of CrossClockBuffer @[CrossClockIrrevocable.scala 27:35]
    ackCrossClockBuffer.clock <= clock
    ackCrossClockBuffer.reset <= reset
    ackCrossClockBuffer.io.src <= ackWire @[CrossClockIrrevocable.scala 28:30]
    ackCrossClockBuffer.io.dst_clock <= io.src_clock @[CrossClockIrrevocable.scala 29:36]
    ackCrossClockBuffer.io.dst_reset <= io.src_reset @[CrossClockIrrevocable.scala 30:36]
    ackInSrc <= ackCrossClockBuffer.io.dst @[CrossClockIrrevocable.scala 31:12]
    inst reqCrossClockBuffer of CrossClockBuffer_1 @[CrossClockIrrevocable.scala 34:35]
    reqCrossClockBuffer.clock <= clock
    reqCrossClockBuffer.reset <= reset
    reqCrossClockBuffer.io.src <= reqWire @[CrossClockIrrevocable.scala 35:30]
    reqCrossClockBuffer.io.dst_clock <= io.dst_clock @[CrossClockIrrevocable.scala 36:36]
    reqCrossClockBuffer.io.dst_reset <= io.dst_reset @[CrossClockIrrevocable.scala 37:36]
    reqInDst <= reqCrossClockBuffer.io.dst @[CrossClockIrrevocable.scala 38:12]
    reg running : UInt<1>, io.src_clock with : (reset => (io.src_reset, UInt<1>("h00"))) @[CrossClockIrrevocable.scala 41:26]
    reg req : UInt<1>, io.src_clock with : (reset => (io.src_reset, UInt<1>("h00"))) @[CrossClockIrrevocable.scala 42:22]
    reg ackInSrcDelay1 : UInt<1>, io.src_clock @[CrossClockIrrevocable.scala 43:33]
    ackInSrcDelay1 <= ackInSrc @[CrossClockIrrevocable.scala 43:33]
    reg srcReady : UInt<1>, io.src_clock with : (reset => (io.src_reset, UInt<1>("h00"))) @[CrossClockIrrevocable.scala 44:27]
    node _T_39 = eq(running, UInt<1>("h00")) @[CrossClockIrrevocable.scala 46:26]
    node _T_40 = and(io.src.valid, _T_39) @[CrossClockIrrevocable.scala 46:24]
    when _T_40 : @[CrossClockIrrevocable.scala 46:36]
      running <= UInt<1>("h01") @[CrossClockIrrevocable.scala 47:15]
      skip @[CrossClockIrrevocable.scala 46:36]
    else : @[CrossClockIrrevocable.scala 48:46]
      node _T_42 = and(io.src.valid, io.src.ready) @[CrossClockIrrevocable.scala 48:30]
      when _T_42 : @[CrossClockIrrevocable.scala 48:46]
        running <= UInt<1>("h00") @[CrossClockIrrevocable.scala 49:15]
        skip @[CrossClockIrrevocable.scala 48:46]
    node _T_45 = eq(ackInSrc, UInt<1>("h00")) @[CrossClockIrrevocable.scala 52:11]
    node _T_46 = and(_T_45, ackInSrcDelay1) @[CrossClockIrrevocable.scala 52:21]
    when _T_46 : @[CrossClockIrrevocable.scala 52:39]
      srcReady <= UInt<1>("h01") @[CrossClockIrrevocable.scala 54:16]
      skip @[CrossClockIrrevocable.scala 52:39]
    else : @[CrossClockIrrevocable.scala 55:18]
      srcReady <= UInt<1>("h00") @[CrossClockIrrevocable.scala 56:16]
      skip @[CrossClockIrrevocable.scala 55:18]
    node _T_50 = eq(running, UInt<1>("h00")) @[CrossClockIrrevocable.scala 59:26]
    node _T_51 = and(io.src.valid, _T_50) @[CrossClockIrrevocable.scala 59:24]
    when _T_51 : @[CrossClockIrrevocable.scala 59:36]
      req <= UInt<1>("h01") @[CrossClockIrrevocable.scala 60:11]
      skip @[CrossClockIrrevocable.scala 59:36]
    else : @[CrossClockIrrevocable.scala 61:27]
      when ackInSrc : @[CrossClockIrrevocable.scala 61:27]
        req <= UInt<1>("h00") @[CrossClockIrrevocable.scala 62:11]
        skip @[CrossClockIrrevocable.scala 61:27]
    reqWire <= req @[CrossClockIrrevocable.scala 65:13]
    io.src.ready <= srcReady @[CrossClockIrrevocable.scala 66:18]
    reg ack : UInt<1>, io.dst_clock with : (reset => (io.dst_reset, UInt<1>("h00"))) @[CrossClockIrrevocable.scala 70:22]
    reg reqInDstDelay1 : UInt<1>, io.dst_clock @[CrossClockIrrevocable.scala 71:33]
    reqInDstDelay1 <= reqInDst @[CrossClockIrrevocable.scala 71:33]
    reg dstValid : UInt<1>, io.dst_clock with : (reset => (io.dst_reset, UInt<1>("h00"))) @[CrossClockIrrevocable.scala 72:27]
    node _T_59 = and(io.dst.valid, io.dst.ready) @[CrossClockIrrevocable.scala 74:24]
    when _T_59 : @[CrossClockIrrevocable.scala 74:40]
      ack <= UInt<1>("h01") @[CrossClockIrrevocable.scala 75:11]
      skip @[CrossClockIrrevocable.scala 74:40]
    else : @[CrossClockIrrevocable.scala 76:45]
      node _T_62 = eq(reqInDst, UInt<1>("h00")) @[CrossClockIrrevocable.scala 76:17]
      node _T_63 = and(_T_62, reqInDstDelay1) @[CrossClockIrrevocable.scala 76:27]
      when _T_63 : @[CrossClockIrrevocable.scala 76:45]
        ack <= UInt<1>("h00") @[CrossClockIrrevocable.scala 78:11]
        skip @[CrossClockIrrevocable.scala 76:45]
    node _T_66 = eq(reqInDstDelay1, UInt<1>("h00")) @[CrossClockIrrevocable.scala 81:22]
    node _T_67 = and(reqInDst, _T_66) @[CrossClockIrrevocable.scala 81:20]
    when _T_67 : @[CrossClockIrrevocable.scala 81:39]
      dstValid <= UInt<1>("h01") @[CrossClockIrrevocable.scala 83:16]
      skip @[CrossClockIrrevocable.scala 81:39]
    else : @[CrossClockIrrevocable.scala 84:46]
      node _T_69 = and(io.dst.valid, io.dst.ready) @[CrossClockIrrevocable.scala 84:30]
      when _T_69 : @[CrossClockIrrevocable.scala 84:46]
        dstValid <= UInt<1>("h00") @[CrossClockIrrevocable.scala 85:16]
        skip @[CrossClockIrrevocable.scala 84:46]
    ackWire <= ack @[CrossClockIrrevocable.scala 88:13]
    io.dst.valid <= dstValid @[CrossClockIrrevocable.scala 89:18]
    io.dst.bits <= io.src.bits @[CrossClockIrrevocable.scala 90:17]
    
