module CrossClockBuffer( // @[:@3.2]
  input   io_src, // @[:@6.4]
  output  io_dst, // @[:@6.4]
  input   io_dst_clock // @[:@6.4]
);
  reg  innerBuffer_0; // @[CrossClockBuffer.scala 20:26:@8.4]
  reg [31:0] _RAND_0;
  reg  innerBuffer_1; // @[CrossClockBuffer.scala 20:26:@8.4]
  reg [31:0] _RAND_1;
  assign io_dst = innerBuffer_1; // @[CrossClockBuffer.scala 23:12:@10.4]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE
  integer initvar;
  initial begin
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      #0.002 begin end
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  innerBuffer_0 = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  innerBuffer_1 = _RAND_1[0:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge io_dst_clock) begin
    innerBuffer_0 <= io_src;
    innerBuffer_1 <= innerBuffer_0;
  end
endmodule
module CrossClockIrrevocable( // @[:@23.2]
  input        clock, // @[:@24.4]
  input        reset, // @[:@25.4]
  output       io_src_ready, // @[:@26.4]
  input        io_src_valid, // @[:@26.4]
  input  [7:0] io_src_bits, // @[:@26.4]
  input        io_dst_ready, // @[:@26.4]
  output       io_dst_valid, // @[:@26.4]
  output [7:0] io_dst_bits, // @[:@26.4]
  input        io_src_clock, // @[:@26.4]
  input        io_src_reset, // @[:@26.4]
  input        io_dst_clock, // @[:@26.4]
  input        io_dst_reset // @[:@26.4]
);
  wire  ackCrossClockBuffer_io_src; // @[CrossClockIrrevocable.scala 27:35:@32.4]
  wire  ackCrossClockBuffer_io_dst; // @[CrossClockIrrevocable.scala 27:35:@32.4]
  wire  ackCrossClockBuffer_io_dst_clock; // @[CrossClockIrrevocable.scala 27:35:@32.4]
  wire  reqCrossClockBuffer_io_src; // @[CrossClockIrrevocable.scala 34:35:@39.4]
  wire  reqCrossClockBuffer_io_dst; // @[CrossClockIrrevocable.scala 34:35:@39.4]
  wire  reqCrossClockBuffer_io_dst_clock; // @[CrossClockIrrevocable.scala 34:35:@39.4]
  reg  running; // @[CrossClockIrrevocable.scala 41:26:@46.4]
  reg [31:0] _RAND_0;
  reg  req; // @[CrossClockIrrevocable.scala 42:22:@47.4]
  reg [31:0] _RAND_1;
  reg  ackInSrcDelay1; // @[CrossClockIrrevocable.scala 43:33:@48.4]
  reg [31:0] _RAND_2;
  reg  srcReady; // @[CrossClockIrrevocable.scala 44:27:@50.4]
  reg [31:0] _RAND_3;
  wire  _T_39; // @[CrossClockIrrevocable.scala 46:26:@51.4]
  wire  _T_40; // @[CrossClockIrrevocable.scala 46:24:@52.4]
  wire  _T_42; // @[CrossClockIrrevocable.scala 48:30:@57.6]
  wire  _GEN_0; // @[CrossClockIrrevocable.scala 48:46:@58.6]
  wire  _GEN_1; // @[CrossClockIrrevocable.scala 46:36:@53.4]
  wire  ackInSrc; // @[CrossClockIrrevocable.scala 23:28:@30.4 CrossClockIrrevocable.scala 31:12:@38.4]
  wire  _T_45; // @[CrossClockIrrevocable.scala 52:11:@61.4]
  wire  _T_46; // @[CrossClockIrrevocable.scala 52:21:@62.4]
  wire  _GEN_3; // @[CrossClockIrrevocable.scala 61:27:@75.6]
  wire  _GEN_4; // @[CrossClockIrrevocable.scala 59:36:@71.4]
  reg  ack; // @[CrossClockIrrevocable.scala 70:22:@80.4]
  reg [31:0] _RAND_4;
  reg  reqInDstDelay1; // @[CrossClockIrrevocable.scala 71:33:@81.4]
  reg [31:0] _RAND_5;
  reg  dstValid; // @[CrossClockIrrevocable.scala 72:27:@83.4]
  reg [31:0] _RAND_6;
  wire  _T_59; // @[CrossClockIrrevocable.scala 74:24:@84.4]
  wire  reqInDst; // @[CrossClockIrrevocable.scala 24:28:@31.4 CrossClockIrrevocable.scala 38:12:@45.4]
  wire  _T_62; // @[CrossClockIrrevocable.scala 76:17:@89.6]
  wire  _T_63; // @[CrossClockIrrevocable.scala 76:27:@90.6]
  wire  _GEN_5; // @[CrossClockIrrevocable.scala 76:45:@91.6]
  wire  _GEN_6; // @[CrossClockIrrevocable.scala 74:40:@85.4]
  wire  _T_66; // @[CrossClockIrrevocable.scala 81:22:@94.4]
  wire  _T_67; // @[CrossClockIrrevocable.scala 81:20:@95.4]
  wire  _GEN_7; // @[CrossClockIrrevocable.scala 84:46:@101.6]
  wire  _GEN_8; // @[CrossClockIrrevocable.scala 81:39:@96.4]
  CrossClockBuffer ackCrossClockBuffer ( // @[CrossClockIrrevocable.scala 27:35:@32.4]
    .io_src(ackCrossClockBuffer_io_src),
    .io_dst(ackCrossClockBuffer_io_dst),
    .io_dst_clock(ackCrossClockBuffer_io_dst_clock)
  );
  CrossClockBuffer reqCrossClockBuffer ( // @[CrossClockIrrevocable.scala 34:35:@39.4]
    .io_src(reqCrossClockBuffer_io_src),
    .io_dst(reqCrossClockBuffer_io_dst),
    .io_dst_clock(reqCrossClockBuffer_io_dst_clock)
  );
  assign _T_39 = running == 1'h0; // @[CrossClockIrrevocable.scala 46:26:@51.4]
  assign _T_40 = io_src_valid & _T_39; // @[CrossClockIrrevocable.scala 46:24:@52.4]
  assign _T_42 = io_src_valid & io_src_ready; // @[CrossClockIrrevocable.scala 48:30:@57.6]
  assign _GEN_0 = _T_42 ? 1'h0 : running; // @[CrossClockIrrevocable.scala 48:46:@58.6]
  assign _GEN_1 = _T_40 ? 1'h1 : _GEN_0; // @[CrossClockIrrevocable.scala 46:36:@53.4]
  assign ackInSrc = ackCrossClockBuffer_io_dst; // @[CrossClockIrrevocable.scala 23:28:@30.4 CrossClockIrrevocable.scala 31:12:@38.4]
  assign _T_45 = ackInSrc == 1'h0; // @[CrossClockIrrevocable.scala 52:11:@61.4]
  assign _T_46 = _T_45 & ackInSrcDelay1; // @[CrossClockIrrevocable.scala 52:21:@62.4]
  assign _GEN_3 = ackInSrc ? 1'h0 : req; // @[CrossClockIrrevocable.scala 61:27:@75.6]
  assign _GEN_4 = _T_40 ? 1'h1 : _GEN_3; // @[CrossClockIrrevocable.scala 59:36:@71.4]
  assign _T_59 = io_dst_valid & io_dst_ready; // @[CrossClockIrrevocable.scala 74:24:@84.4]
  assign reqInDst = reqCrossClockBuffer_io_dst; // @[CrossClockIrrevocable.scala 24:28:@31.4 CrossClockIrrevocable.scala 38:12:@45.4]
  assign _T_62 = reqInDst == 1'h0; // @[CrossClockIrrevocable.scala 76:17:@89.6]
  assign _T_63 = _T_62 & reqInDstDelay1; // @[CrossClockIrrevocable.scala 76:27:@90.6]
  assign _GEN_5 = _T_63 ? 1'h0 : ack; // @[CrossClockIrrevocable.scala 76:45:@91.6]
  assign _GEN_6 = _T_59 ? 1'h1 : _GEN_5; // @[CrossClockIrrevocable.scala 74:40:@85.4]
  assign _T_66 = reqInDstDelay1 == 1'h0; // @[CrossClockIrrevocable.scala 81:22:@94.4]
  assign _T_67 = reqInDst & _T_66; // @[CrossClockIrrevocable.scala 81:20:@95.4]
  assign _GEN_7 = _T_59 ? 1'h0 : dstValid; // @[CrossClockIrrevocable.scala 84:46:@101.6]
  assign _GEN_8 = _T_67 ? 1'h1 : _GEN_7; // @[CrossClockIrrevocable.scala 81:39:@96.4]
  assign io_src_ready = srcReady; // @[CrossClockIrrevocable.scala 66:18:@79.4]
  assign io_dst_valid = dstValid; // @[CrossClockIrrevocable.scala 89:18:@105.4]
  assign io_dst_bits = io_src_bits; // @[CrossClockIrrevocable.scala 90:17:@106.4]
  assign ackCrossClockBuffer_io_src = ack; // @[CrossClockIrrevocable.scala 28:30:@35.4]
  assign ackCrossClockBuffer_io_dst_clock = io_src_clock; // @[CrossClockIrrevocable.scala 29:36:@36.4]
  assign reqCrossClockBuffer_io_src = req; // @[CrossClockIrrevocable.scala 35:30:@42.4]
  assign reqCrossClockBuffer_io_dst_clock = io_dst_clock; // @[CrossClockIrrevocable.scala 36:36:@43.4]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE
  integer initvar;
  initial begin
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      #0.002 begin end
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  running = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  req = _RAND_1[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  ackInSrcDelay1 = _RAND_2[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  srcReady = _RAND_3[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{`RANDOM}};
  ack = _RAND_4[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{`RANDOM}};
  reqInDstDelay1 = _RAND_5[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_6 = {1{`RANDOM}};
  dstValid = _RAND_6[0:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge io_src_clock) begin
    if (io_src_reset) begin
      running <= 1'h0;
    end else begin
      if (_T_40) begin
        running <= 1'h1;
      end else begin
        if (_T_42) begin
          running <= 1'h0;
        end
      end
    end
    if (io_src_reset) begin
      req <= 1'h0;
    end else begin
      if (_T_40) begin
        req <= 1'h1;
      end else begin
        if (ackInSrc) begin
          req <= 1'h0;
        end
      end
    end
    ackInSrcDelay1 <= ackCrossClockBuffer_io_dst;
    if (io_src_reset) begin
      srcReady <= 1'h0;
    end else begin
      srcReady <= _T_46;
    end
  end
  always @(posedge io_dst_clock) begin
    if (io_dst_reset) begin
      ack <= 1'h0;
    end else begin
      if (_T_59) begin
        ack <= 1'h1;
      end else begin
        if (_T_63) begin
          ack <= 1'h0;
        end
      end
    end
    reqInDstDelay1 <= reqCrossClockBuffer_io_dst;
    if (io_dst_reset) begin
      dstValid <= 1'h0;
    end else begin
      if (_T_67) begin
        dstValid <= 1'h1;
      end else begin
        if (_T_59) begin
          dstValid <= 1'h0;
        end
      end
    end
  end
endmodule
