module VGADriver( // @[:@3.2]
  input         clock, // @[:@4.4]
  input         reset, // @[:@5.4]
  output        io_src_color_ready, // @[:@6.4]
  input         io_src_color_valid, // @[:@6.4]
  input  [23:0] io_src_color_bits, // @[:@6.4]
  output        io_dst_color_valid, // @[:@6.4]
  output [23:0] io_dst_color_bits, // @[:@6.4]
  output        io_error, // @[:@6.4]
  output        io_hsync, // @[:@6.4]
  output        io_vsync // @[:@6.4]
);
  reg [10:0] hCounter; // @[VGADriver.scala 61:25:@8.4]
  reg [31:0] _RAND_0;
  reg [9:0] vCounter; // @[VGADriver.scala 62:25:@9.4]
  reg [31:0] _RAND_1;
  wire  _T_32; // @[VGADriver.scala 64:18:@10.4]
  wire [11:0] _T_35; // @[VGADriver.scala 67:26:@15.6]
  wire [10:0] _T_36; // @[VGADriver.scala 67:26:@16.6]
  wire [10:0] _GEN_0; // @[VGADriver.scala 64:39:@11.4]
  wire  _T_40; // @[VGADriver.scala 71:20:@21.6]
  wire [10:0] _T_43; // @[VGADriver.scala 74:28:@26.8]
  wire [9:0] _T_44; // @[VGADriver.scala 74:28:@27.8]
  wire [9:0] _GEN_1; // @[VGADriver.scala 71:41:@22.6]
  wire [9:0] _GEN_2; // @[VGADriver.scala 70:39:@20.4]
  reg  vSync; // @[VGADriver.scala 79:22:@31.4]
  reg [31:0] _RAND_2;
  reg  hSync; // @[VGADriver.scala 80:22:@32.4]
  reg [31:0] _RAND_3;
  reg  hSyncWillBegin; // @[VGADriver.scala 81:31:@33.4]
  reg [31:0] _RAND_4;
  reg  hSyncWillEnd; // @[VGADriver.scala 83:29:@35.4]
  reg [31:0] _RAND_5;
  wire  _T_58; // @[VGADriver.scala 86:18:@37.4]
  wire  _T_66; // @[VGADriver.scala 98:18:@51.4]
  wire  _GEN_7; // @[VGADriver.scala 112:31:@69.6]
  wire  _GEN_8; // @[VGADriver.scala 110:23:@65.4]
  wire  _GEN_9; // @[VGADriver.scala 119:33:@77.8]
  wire  _GEN_10; // @[VGADriver.scala 117:25:@73.6]
  wire  _GEN_11; // @[VGADriver.scala 116:25:@72.4]
  reg  hWillBeActive; // @[VGADriver.scala 128:30:@83.4]
  reg [31:0] _RAND_6;
  reg  vWillBeActive; // @[VGADriver.scala 129:30:@84.4]
  reg [31:0] _RAND_7;
  reg  hWillBeInactive; // @[VGADriver.scala 130:32:@85.4]
  reg [31:0] _RAND_8;
  reg  vWillBeInactive; // @[VGADriver.scala 131:32:@86.4]
  reg [31:0] _RAND_9;
  reg  videoActive; // @[VGADriver.scala 132:28:@87.4]
  reg [31:0] _RAND_10;
  wire  _T_88; // @[VGADriver.scala 134:18:@88.4]
  wire  _T_92; // @[VGADriver.scala 140:18:@95.4]
  wire  _T_96; // @[VGADriver.scala 146:18:@102.4]
  wire  _T_100; // @[VGADriver.scala 152:18:@109.4]
  wire  _T_103; // @[VGADriver.scala 158:23:@116.4]
  wire  _T_105; // @[VGADriver.scala 160:31:@121.6]
  wire  _GEN_16; // @[VGADriver.scala 160:50:@122.6]
  wire  _GEN_17; // @[VGADriver.scala 158:40:@117.4]
  reg  error; // @[VGADriver.scala 169:22:@128.4]
  reg [31:0] _RAND_11;
  wire  _T_110; // @[VGADriver.scala 171:30:@129.4]
  wire  _T_111; // @[VGADriver.scala 171:28:@130.4]
  wire  _GEN_18; // @[VGADriver.scala 171:51:@131.4]
  assign _T_32 = hCounter == 11'h671; // @[VGADriver.scala 64:18:@10.4]
  assign _T_35 = hCounter + 11'h1; // @[VGADriver.scala 67:26:@15.6]
  assign _T_36 = hCounter + 11'h1; // @[VGADriver.scala 67:26:@16.6]
  assign _GEN_0 = _T_32 ? 11'h0 : _T_36; // @[VGADriver.scala 64:39:@11.4]
  assign _T_40 = vCounter == 10'h2ed; // @[VGADriver.scala 71:20:@21.6]
  assign _T_43 = vCounter + 10'h1; // @[VGADriver.scala 74:28:@26.8]
  assign _T_44 = vCounter + 10'h1; // @[VGADriver.scala 74:28:@27.8]
  assign _GEN_1 = _T_40 ? 10'h0 : _T_44; // @[VGADriver.scala 71:41:@22.6]
  assign _GEN_2 = _T_32 ? _GEN_1 : vCounter; // @[VGADriver.scala 70:39:@20.4]
  assign _T_58 = hCounter == 11'h670; // @[VGADriver.scala 86:18:@37.4]
  assign _T_66 = hCounter == 11'h6c; // @[VGADriver.scala 98:18:@51.4]
  assign _GEN_7 = hSyncWillBegin ? 1'h0 : hSync; // @[VGADriver.scala 112:31:@69.6]
  assign _GEN_8 = hSyncWillEnd ? 1'h1 : _GEN_7; // @[VGADriver.scala 110:23:@65.4]
  assign _GEN_9 = hSyncWillBegin ? 1'h0 : vSync; // @[VGADriver.scala 119:33:@77.8]
  assign _GEN_10 = hSyncWillEnd ? 1'h1 : _GEN_9; // @[VGADriver.scala 117:25:@73.6]
  assign _GEN_11 = hSyncWillBegin ? _GEN_10 : vSync; // @[VGADriver.scala 116:25:@72.4]
  assign _T_88 = hCounter == 11'hda; // @[VGADriver.scala 134:18:@88.4]
  assign _T_92 = vCounter == 10'h18; // @[VGADriver.scala 140:18:@95.4]
  assign _T_96 = hCounter == 11'h5dc; // @[VGADriver.scala 146:18:@102.4]
  assign _T_100 = vCounter == 10'h2e8; // @[VGADriver.scala 152:18:@109.4]
  assign _T_103 = hWillBeActive & vWillBeActive; // @[VGADriver.scala 158:23:@116.4]
  assign _T_105 = hWillBeInactive & vWillBeInactive; // @[VGADriver.scala 160:31:@121.6]
  assign _GEN_16 = _T_105 ? 1'h0 : videoActive; // @[VGADriver.scala 160:50:@122.6]
  assign _GEN_17 = _T_103 ? 1'h1 : _GEN_16; // @[VGADriver.scala 158:40:@117.4]
  assign _T_110 = io_src_color_valid == 1'h0; // @[VGADriver.scala 171:30:@129.4]
  assign _T_111 = io_src_color_ready & _T_110; // @[VGADriver.scala 171:28:@130.4]
  assign _GEN_18 = _T_111 ? 1'h1 : error; // @[VGADriver.scala 171:51:@131.4]
  assign io_src_color_ready = videoActive; // @[VGADriver.scala 166:22:@127.4]
  assign io_dst_color_valid = videoActive; // @[VGADriver.scala 165:22:@126.4]
  assign io_dst_color_bits = io_src_color_bits; // @[VGADriver.scala 164:21:@125.4]
  assign io_error = error; // @[VGADriver.scala 175:12:@134.4]
  assign io_hsync = hSync; // @[VGADriver.scala 124:12:@81.4]
  assign io_vsync = vSync; // @[VGADriver.scala 125:12:@82.4]
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
  hCounter = _RAND_0[10:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  vCounter = _RAND_1[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  vSync = _RAND_2[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  hSync = _RAND_3[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{`RANDOM}};
  hSyncWillBegin = _RAND_4[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{`RANDOM}};
  hSyncWillEnd = _RAND_5[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_6 = {1{`RANDOM}};
  hWillBeActive = _RAND_6[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_7 = {1{`RANDOM}};
  vWillBeActive = _RAND_7[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_8 = {1{`RANDOM}};
  hWillBeInactive = _RAND_8[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_9 = {1{`RANDOM}};
  vWillBeInactive = _RAND_9[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_10 = {1{`RANDOM}};
  videoActive = _RAND_10[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_11 = {1{`RANDOM}};
  error = _RAND_11[0:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge clock) begin
    if (reset) begin
      hCounter <= 11'h0;
    end else begin
      if (_T_32) begin
        hCounter <= 11'h0;
      end else begin
        hCounter <= _T_36;
      end
    end
    if (reset) begin
      vCounter <= 10'h0;
    end else begin
      if (_T_32) begin
        if (_T_40) begin
          vCounter <= 10'h0;
        end else begin
          vCounter <= _T_44;
        end
      end
    end
    if (reset) begin
      vSync <= 1'h0;
    end else begin
      if (hSyncWillBegin) begin
        if (hSyncWillEnd) begin
          vSync <= 1'h1;
        end else begin
          if (hSyncWillBegin) begin
            vSync <= 1'h0;
          end
        end
      end
    end
    if (reset) begin
      hSync <= 1'h0;
    end else begin
      if (hSyncWillEnd) begin
        hSync <= 1'h1;
      end else begin
        if (hSyncWillBegin) begin
          hSync <= 1'h0;
        end
      end
    end
    if (reset) begin
      hSyncWillBegin <= 1'h0;
    end else begin
      hSyncWillBegin <= _T_58;
    end
    if (reset) begin
      hSyncWillEnd <= 1'h0;
    end else begin
      hSyncWillEnd <= _T_66;
    end
    if (reset) begin
      hWillBeActive <= 1'h0;
    end else begin
      hWillBeActive <= _T_88;
    end
    if (reset) begin
      vWillBeActive <= 1'h0;
    end else begin
      vWillBeActive <= _T_92;
    end
    if (reset) begin
      hWillBeInactive <= 1'h0;
    end else begin
      hWillBeInactive <= _T_96;
    end
    if (reset) begin
      vWillBeInactive <= 1'h0;
    end else begin
      vWillBeInactive <= _T_100;
    end
    if (reset) begin
      videoActive <= 1'h0;
    end else begin
      if (_T_103) begin
        videoActive <= 1'h1;
      end else begin
        if (_T_105) begin
          videoActive <= 1'h0;
        end
      end
    end
    if (reset) begin
      error <= 1'h0;
    end else begin
      if (_T_111) begin
        error <= 1'h1;
      end
    end
  end
endmodule
