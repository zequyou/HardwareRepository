module AsyncFIFO2( // @[:@3.2]
  input        io_wr_valid, // @[:@6.4]
  input  [9:0] io_wr_bits, // @[:@6.4]
  output       io_wr_not_full, // @[:@6.4]
  input        io_rd_valid, // @[:@6.4]
  output [9:0] io_rd_bits, // @[:@6.4]
  output       io_rd_not_empty, // @[:@6.4]
  input        io_rd_reset, // @[:@6.4]
  input        io_wr_reset, // @[:@6.4]
  input        io_wr_clock, // @[:@6.4]
  input        io_rd_clock // @[:@6.4]
);
  reg [9:0] ram [0:127]; // @[AsyncFIFO2.scala 28:16:@8.4]
  reg [31:0] _RAND_0;
  wire [9:0] ram__T_84_data; // @[AsyncFIFO2.scala 28:16:@8.4]
  wire [6:0] ram__T_84_addr; // @[AsyncFIFO2.scala 28:16:@8.4]
  wire [9:0] ram__T_62_data; // @[AsyncFIFO2.scala 28:16:@8.4]
  wire [6:0] ram__T_62_addr; // @[AsyncFIFO2.scala 28:16:@8.4]
  wire  ram__T_62_mask; // @[AsyncFIFO2.scala 28:16:@8.4]
  wire  ram__T_62_en; // @[AsyncFIFO2.scala 28:16:@8.4]
  reg [7:0] wrPtr; // @[AsyncFIFO2.scala 34:24:@11.4]
  reg [31:0] _RAND_1;
  reg [7:0] wrGray; // @[AsyncFIFO2.scala 36:25:@13.4]
  reg [31:0] _RAND_2;
  reg [9:0] rdGrayToWr; // @[AsyncFIFO2.scala 38:29:@15.4]
  reg [31:0] _RAND_3;
  reg [9:0] rdGrayInWr; // @[AsyncFIFO2.scala 39:29:@17.4]
  reg [31:0] _RAND_4;
  reg  wrNotFull; // @[AsyncFIFO2.scala 40:28:@19.4]
  reg [31:0] _RAND_5;
  wire  _T_43; // @[AsyncFIFO2.scala 42:22:@20.4]
  wire [8:0] _T_45; // @[AsyncFIFO2.scala 43:26:@22.6]
  wire [7:0] _T_46; // @[AsyncFIFO2.scala 43:26:@23.6]
  wire [7:0] wrPtrNext; // @[AsyncFIFO2.scala 42:40:@21.4]
  wire [6:0] _T_47; // @[AsyncFIFO2.scala 47:40:@29.4]
  wire [7:0] _GEN_17; // @[AsyncFIFO2.scala 47:29:@30.4]
  wire [7:0] wrGrayNext; // @[AsyncFIFO2.scala 47:29:@30.4]
  wire [1:0] _T_49; // @[AsyncFIFO2.scala 52:20:@34.4]
  wire [1:0] _T_50; // @[AsyncFIFO2.scala 52:67:@35.4]
  wire [1:0] _T_51; // @[AsyncFIFO2.scala 52:56:@36.4]
  wire  _T_52; // @[AsyncFIFO2.scala 52:51:@37.4]
  wire [5:0] _T_53; // @[AsyncFIFO2.scala 53:20:@38.4]
  wire [5:0] _T_54; // @[AsyncFIFO2.scala 53:53:@39.4]
  wire  _T_55; // @[AsyncFIFO2.scala 53:39:@40.4]
  wire  _T_56; // @[AsyncFIFO2.scala 52:109:@41.4]
  wire  _T_58; // @[AsyncFIFO2.scala 51:18:@42.4]
  reg [7:0] rdPtr; // @[AsyncFIFO2.scala 65:24:@53.4]
  reg [31:0] _RAND_6;
  reg [7:0] rdGray; // @[AsyncFIFO2.scala 67:25:@55.4]
  reg [31:0] _RAND_7;
  reg [9:0] wrGrayToRd; // @[AsyncFIFO2.scala 69:29:@57.4]
  reg [31:0] _RAND_8;
  reg [9:0] wrGrayInRd; // @[AsyncFIFO2.scala 70:29:@59.4]
  reg [31:0] _RAND_9;
  reg  rdNotEmpty; // @[AsyncFIFO2.scala 71:29:@61.4]
  reg [31:0] _RAND_10;
  reg [9:0] rdData; // @[AsyncFIFO2.scala 72:25:@62.4]
  reg [31:0] _RAND_11;
  wire  _T_75; // @[AsyncFIFO2.scala 74:22:@63.4]
  wire [8:0] _T_77; // @[AsyncFIFO2.scala 75:26:@65.6]
  wire [7:0] _T_78; // @[AsyncFIFO2.scala 75:26:@66.6]
  wire [7:0] rdPtrNext; // @[AsyncFIFO2.scala 74:41:@64.4]
  wire [6:0] _T_79; // @[AsyncFIFO2.scala 79:40:@72.4]
  wire [7:0] _GEN_18; // @[AsyncFIFO2.scala 79:29:@73.4]
  wire [7:0] rdGrayNext; // @[AsyncFIFO2.scala 79:29:@73.4]
  wire [9:0] _GEN_19; // @[AsyncFIFO2.scala 83:30:@77.4]
  wire  _T_81; // @[AsyncFIFO2.scala 83:30:@77.4]
  wire [9:0] _GEN_10; // @[AsyncFIFO2.scala 84:23:@80.4]
  assign ram__T_84_addr = rdPtr[6:0];
  assign ram__T_84_data = ram[ram__T_84_addr]; // @[AsyncFIFO2.scala 28:16:@8.4]
  assign ram__T_62_data = io_wr_bits;
  assign ram__T_62_addr = wrPtr[6:0];
  assign ram__T_62_mask = 1'h1;
  assign ram__T_62_en = io_wr_valid & io_wr_not_full;
  assign _T_43 = io_wr_valid & io_wr_not_full; // @[AsyncFIFO2.scala 42:22:@20.4]
  assign _T_45 = wrPtr + 8'h1; // @[AsyncFIFO2.scala 43:26:@22.6]
  assign _T_46 = wrPtr + 8'h1; // @[AsyncFIFO2.scala 43:26:@23.6]
  assign wrPtrNext = _T_43 ? _T_46 : wrPtr; // @[AsyncFIFO2.scala 42:40:@21.4]
  assign _T_47 = wrPtrNext[7:1]; // @[AsyncFIFO2.scala 47:40:@29.4]
  assign _GEN_17 = {{1'd0}, _T_47}; // @[AsyncFIFO2.scala 47:29:@30.4]
  assign wrGrayNext = wrPtrNext ^ _GEN_17; // @[AsyncFIFO2.scala 47:29:@30.4]
  assign _T_49 = wrGrayNext[7:6]; // @[AsyncFIFO2.scala 52:20:@34.4]
  assign _T_50 = rdGrayInWr[7:6]; // @[AsyncFIFO2.scala 52:67:@35.4]
  assign _T_51 = ~ _T_50; // @[AsyncFIFO2.scala 52:56:@36.4]
  assign _T_52 = _T_49 == _T_51; // @[AsyncFIFO2.scala 52:51:@37.4]
  assign _T_53 = wrGrayNext[5:0]; // @[AsyncFIFO2.scala 53:20:@38.4]
  assign _T_54 = rdGrayInWr[5:0]; // @[AsyncFIFO2.scala 53:53:@39.4]
  assign _T_55 = _T_53 == _T_54; // @[AsyncFIFO2.scala 53:39:@40.4]
  assign _T_56 = _T_52 & _T_55; // @[AsyncFIFO2.scala 52:109:@41.4]
  assign _T_58 = _T_56 == 1'h0; // @[AsyncFIFO2.scala 51:18:@42.4]
  assign _T_75 = io_rd_valid & io_rd_not_empty; // @[AsyncFIFO2.scala 74:22:@63.4]
  assign _T_77 = rdPtr + 8'h1; // @[AsyncFIFO2.scala 75:26:@65.6]
  assign _T_78 = rdPtr + 8'h1; // @[AsyncFIFO2.scala 75:26:@66.6]
  assign rdPtrNext = _T_75 ? _T_78 : rdPtr; // @[AsyncFIFO2.scala 74:41:@64.4]
  assign _T_79 = rdPtrNext[7:1]; // @[AsyncFIFO2.scala 79:40:@72.4]
  assign _GEN_18 = {{1'd0}, _T_79}; // @[AsyncFIFO2.scala 79:29:@73.4]
  assign rdGrayNext = rdPtrNext ^ _GEN_18; // @[AsyncFIFO2.scala 79:29:@73.4]
  assign _GEN_19 = {{2'd0}, rdGrayNext}; // @[AsyncFIFO2.scala 83:30:@77.4]
  assign _T_81 = _GEN_19 != wrGrayInRd; // @[AsyncFIFO2.scala 83:30:@77.4]
  assign _GEN_10 = io_rd_valid ? ram__T_84_data : rdData; // @[AsyncFIFO2.scala 84:23:@80.4]
  assign io_wr_not_full = wrNotFull; // @[AsyncFIFO2.scala 61:20:@52.4]
  assign io_rd_bits = rdData; // @[AsyncFIFO2.scala 89:16:@86.4]
  assign io_rd_not_empty = rdNotEmpty; // @[AsyncFIFO2.scala 90:21:@87.4]
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
  _RAND_0 = {1{`RANDOM}};
  `ifdef RANDOMIZE_MEM_INIT
  for (initvar = 0; initvar < 128; initvar = initvar+1)
    ram[initvar] = _RAND_0[9:0];
  `endif // RANDOMIZE_MEM_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  wrPtr = _RAND_1[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  wrGray = _RAND_2[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  rdGrayToWr = _RAND_3[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{`RANDOM}};
  rdGrayInWr = _RAND_4[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{`RANDOM}};
  wrNotFull = _RAND_5[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_6 = {1{`RANDOM}};
  rdPtr = _RAND_6[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_7 = {1{`RANDOM}};
  rdGray = _RAND_7[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_8 = {1{`RANDOM}};
  wrGrayToRd = _RAND_8[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_9 = {1{`RANDOM}};
  wrGrayInRd = _RAND_9[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_10 = {1{`RANDOM}};
  rdNotEmpty = _RAND_10[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_11 = {1{`RANDOM}};
  rdData = _RAND_11[9:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge io_wr_clock) begin
    if(ram__T_62_en & ram__T_62_mask) begin
      ram[ram__T_62_addr] <= ram__T_62_data; // @[AsyncFIFO2.scala 28:16:@8.4]
    end
    if (io_wr_reset) begin
      wrPtr <= 8'h0;
    end else begin
      if (_T_43) begin
        wrPtr <= _T_46;
      end
    end
    if (io_wr_reset) begin
      wrGray <= 8'h0;
    end else begin
      wrGray <= wrGrayNext;
    end
    rdGrayToWr <= {{2'd0}, rdGray};
    rdGrayInWr <= rdGrayToWr;
    if (io_wr_reset) begin
      wrNotFull <= 1'h0;
    end else begin
      wrNotFull <= _T_58;
    end
  end
  always @(posedge io_rd_clock) begin
    if (io_rd_reset) begin
      rdPtr <= 8'h0;
    end else begin
      if (_T_75) begin
        rdPtr <= _T_78;
      end
    end
    if (io_rd_reset) begin
      rdGray <= 8'h0;
    end else begin
      rdGray <= rdGrayNext;
    end
    wrGrayToRd <= {{2'd0}, wrGray};
    wrGrayInRd <= wrGrayToRd;
    if (io_rd_reset) begin
      rdNotEmpty <= 1'h0;
    end else begin
      rdNotEmpty <= _T_81;
    end
    if (io_rd_reset) begin
      rdData <= 10'h0;
    end else begin
      if (io_rd_valid) begin
        rdData <= ram__T_84_data;
      end
    end
  end
endmodule
module AsyncFIFODecoupled( // @[:@89.2]
  output       io_wr_ready, // @[:@92.4]
  input        io_wr_valid, // @[:@92.4]
  input  [9:0] io_wr_bits, // @[:@92.4]
  input        io_rd_ready, // @[:@92.4]
  output       io_rd_valid, // @[:@92.4]
  output [9:0] io_rd_bits, // @[:@92.4]
  input        io_rd_reset, // @[:@92.4]
  input        io_wr_reset, // @[:@92.4]
  input        io_wr_clock, // @[:@92.4]
  input        io_rd_clock // @[:@92.4]
);
  wire  asyncFIFO_io_wr_valid; // @[AsyncFIFODecoupled.scala 23:25:@94.4]
  wire [9:0] asyncFIFO_io_wr_bits; // @[AsyncFIFODecoupled.scala 23:25:@94.4]
  wire  asyncFIFO_io_wr_not_full; // @[AsyncFIFODecoupled.scala 23:25:@94.4]
  wire  asyncFIFO_io_rd_valid; // @[AsyncFIFODecoupled.scala 23:25:@94.4]
  wire [9:0] asyncFIFO_io_rd_bits; // @[AsyncFIFODecoupled.scala 23:25:@94.4]
  wire  asyncFIFO_io_rd_not_empty; // @[AsyncFIFODecoupled.scala 23:25:@94.4]
  wire  asyncFIFO_io_rd_reset; // @[AsyncFIFODecoupled.scala 23:25:@94.4]
  wire  asyncFIFO_io_wr_reset; // @[AsyncFIFODecoupled.scala 23:25:@94.4]
  wire  asyncFIFO_io_wr_clock; // @[AsyncFIFODecoupled.scala 23:25:@94.4]
  wire  asyncFIFO_io_rd_clock; // @[AsyncFIFODecoupled.scala 23:25:@94.4]
  reg  readFirstValid; // @[AsyncFIFODecoupled.scala 39:33:@106.4]
  reg [31:0] _RAND_0;
  reg [9:0] readSecondData; // @[AsyncFIFODecoupled.scala 40:33:@107.4]
  reg [31:0] _RAND_1;
  reg  readSecondValid; // @[AsyncFIFODecoupled.scala 41:34:@108.4]
  reg [31:0] _RAND_2;
  reg [9:0] readThirdData; // @[AsyncFIFODecoupled.scala 42:32:@109.4]
  reg [31:0] _RAND_3;
  reg  readThirdValid; // @[AsyncFIFODecoupled.scala 43:33:@110.4]
  reg [31:0] _RAND_4;
  reg  readValid; // @[AsyncFIFODecoupled.scala 44:28:@111.4]
  reg [31:0] _RAND_5;
  wire  _T_52; // @[AsyncFIFODecoupled.scala 52:41:@118.4]
  wire  _T_54; // @[AsyncFIFODecoupled.scala 52:62:@119.4]
  wire  _T_55; // @[AsyncFIFODecoupled.scala 52:93:@120.4]
  wire  _T_56; // @[AsyncFIFODecoupled.scala 52:78:@121.4]
  wire  willUpdateThird; // @[AsyncFIFODecoupled.scala 52:59:@122.4]
  wire  _T_58; // @[AsyncFIFODecoupled.scala 53:59:@124.4]
  wire  willUpdateSecond; // @[AsyncFIFODecoupled.scala 53:40:@125.4]
  wire  _T_60; // @[AsyncFIFODecoupled.scala 54:40:@127.4]
  wire  _T_62; // @[AsyncFIFODecoupled.scala 54:23:@128.4]
  wire  readFIFOEnable; // @[AsyncFIFODecoupled.scala 54:59:@129.4]
  wire  _GEN_0; // @[AsyncFIFODecoupled.scala 61:46:@144.8]
  wire  _GEN_1; // @[AsyncFIFODecoupled.scala 59:69:@139.6]
  wire  _GEN_3; // @[AsyncFIFODecoupled.scala 68:43:@153.6]
  wire  _GEN_4; // @[AsyncFIFODecoupled.scala 66:27:@148.4]
  wire  _GEN_5; // @[AsyncFIFODecoupled.scala 74:33:@160.6]
  wire  _GEN_6; // @[AsyncFIFODecoupled.scala 72:28:@156.4]
  wire  _T_80; // @[AsyncFIFODecoupled.scala 80:33:@167.6]
  wire  _GEN_7; // @[AsyncFIFODecoupled.scala 80:52:@168.6]
  wire  _GEN_8; // @[AsyncFIFODecoupled.scala 78:26:@163.4]
  wire [9:0] readFirstData; // @[AsyncFIFODecoupled.scala 38:29:@105.4 AsyncFIFODecoupled.scala 51:19:@117.4]
  wire [9:0] _GEN_9; // @[AsyncFIFODecoupled.scala 85:29:@172.6]
  wire [9:0] _GEN_10; // @[AsyncFIFODecoupled.scala 84:27:@171.4]
  wire [9:0] _GEN_11; // @[AsyncFIFODecoupled.scala 92:28:@179.4]
  AsyncFIFO2 asyncFIFO ( // @[AsyncFIFODecoupled.scala 23:25:@94.4]
    .io_wr_valid(asyncFIFO_io_wr_valid),
    .io_wr_bits(asyncFIFO_io_wr_bits),
    .io_wr_not_full(asyncFIFO_io_wr_not_full),
    .io_rd_valid(asyncFIFO_io_rd_valid),
    .io_rd_bits(asyncFIFO_io_rd_bits),
    .io_rd_not_empty(asyncFIFO_io_rd_not_empty),
    .io_rd_reset(asyncFIFO_io_rd_reset),
    .io_wr_reset(asyncFIFO_io_wr_reset),
    .io_wr_clock(asyncFIFO_io_wr_clock),
    .io_rd_clock(asyncFIFO_io_rd_clock)
  );
  assign _T_52 = readSecondValid | readFirstValid; // @[AsyncFIFODecoupled.scala 52:41:@118.4]
  assign _T_54 = readThirdValid == 1'h0; // @[AsyncFIFODecoupled.scala 52:62:@119.4]
  assign _T_55 = io_rd_ready & io_rd_valid; // @[AsyncFIFODecoupled.scala 52:93:@120.4]
  assign _T_56 = _T_54 | _T_55; // @[AsyncFIFODecoupled.scala 52:78:@121.4]
  assign willUpdateThird = _T_52 & _T_56; // @[AsyncFIFODecoupled.scala 52:59:@122.4]
  assign _T_58 = readSecondValid == willUpdateThird; // @[AsyncFIFODecoupled.scala 53:59:@124.4]
  assign willUpdateSecond = readFirstValid & _T_58; // @[AsyncFIFODecoupled.scala 53:40:@125.4]
  assign _T_60 = readFirstValid & readSecondValid; // @[AsyncFIFODecoupled.scala 54:40:@127.4]
  assign _T_62 = _T_60 == 1'h0; // @[AsyncFIFODecoupled.scala 54:23:@128.4]
  assign readFIFOEnable = _T_62 & asyncFIFO_io_rd_not_empty; // @[AsyncFIFODecoupled.scala 54:59:@129.4]
  assign _GEN_0 = _T_55 ? 1'h0 : readValid; // @[AsyncFIFODecoupled.scala 61:46:@144.8]
  assign _GEN_1 = willUpdateThird ? 1'h1 : _GEN_0; // @[AsyncFIFODecoupled.scala 59:69:@139.6]
  assign _GEN_3 = _T_55 ? 1'h0 : readThirdValid; // @[AsyncFIFODecoupled.scala 68:43:@153.6]
  assign _GEN_4 = willUpdateThird ? 1'h1 : _GEN_3; // @[AsyncFIFODecoupled.scala 66:27:@148.4]
  assign _GEN_5 = willUpdateThird ? 1'h0 : readSecondValid; // @[AsyncFIFODecoupled.scala 74:33:@160.6]
  assign _GEN_6 = willUpdateSecond ? 1'h1 : _GEN_5; // @[AsyncFIFODecoupled.scala 72:28:@156.4]
  assign _T_80 = willUpdateSecond | willUpdateThird; // @[AsyncFIFODecoupled.scala 80:33:@167.6]
  assign _GEN_7 = _T_80 ? 1'h0 : readFirstValid; // @[AsyncFIFODecoupled.scala 80:52:@168.6]
  assign _GEN_8 = readFIFOEnable ? 1'h1 : _GEN_7; // @[AsyncFIFODecoupled.scala 78:26:@163.4]
  assign readFirstData = asyncFIFO_io_rd_bits; // @[AsyncFIFODecoupled.scala 38:29:@105.4 AsyncFIFODecoupled.scala 51:19:@117.4]
  assign _GEN_9 = readSecondValid ? readSecondData : readFirstData; // @[AsyncFIFODecoupled.scala 85:29:@172.6]
  assign _GEN_10 = willUpdateThird ? _GEN_9 : readThirdData; // @[AsyncFIFODecoupled.scala 84:27:@171.4]
  assign _GEN_11 = willUpdateSecond ? readFirstData : readSecondData; // @[AsyncFIFODecoupled.scala 92:28:@179.4]
  assign io_wr_ready = asyncFIFO_io_wr_not_full; // @[AsyncFIFODecoupled.scala 35:15:@104.4]
  assign io_rd_valid = readValid; // @[AsyncFIFODecoupled.scala 98:17:@184.4]
  assign io_rd_bits = readThirdData; // @[AsyncFIFODecoupled.scala 99:16:@185.4]
  assign asyncFIFO_io_wr_valid = io_wr_valid; // @[AsyncFIFODecoupled.scala 33:25:@102.4]
  assign asyncFIFO_io_wr_bits = io_wr_bits; // @[AsyncFIFODecoupled.scala 32:24:@101.4]
  assign asyncFIFO_io_rd_valid = _T_62 & asyncFIFO_io_rd_not_empty; // @[AsyncFIFODecoupled.scala 96:27:@182.4]
  assign asyncFIFO_io_rd_reset = io_rd_reset; // @[AsyncFIFODecoupled.scala 29:25:@100.4]
  assign asyncFIFO_io_wr_reset = io_wr_reset; // @[AsyncFIFODecoupled.scala 27:25:@98.4]
  assign asyncFIFO_io_wr_clock = io_wr_clock; // @[AsyncFIFODecoupled.scala 26:25:@97.4]
  assign asyncFIFO_io_rd_clock = io_rd_clock; // @[AsyncFIFODecoupled.scala 28:25:@99.4]
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
  readFirstValid = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  readSecondData = _RAND_1[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  readSecondValid = _RAND_2[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  readThirdData = _RAND_3[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{`RANDOM}};
  readThirdValid = _RAND_4[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{`RANDOM}};
  readValid = _RAND_5[0:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge io_rd_clock) begin
    if (io_rd_reset) begin
      readFirstValid <= 1'h0;
    end else begin
      if (readFIFOEnable) begin
        readFirstValid <= 1'h1;
      end else begin
        if (_T_80) begin
          readFirstValid <= 1'h0;
        end
      end
    end
    if (io_rd_reset) begin
      readSecondData <= 10'h0;
    end else begin
      if (willUpdateSecond) begin
        readSecondData <= readFirstData;
      end
    end
    if (io_rd_reset) begin
      readSecondValid <= 1'h0;
    end else begin
      if (willUpdateSecond) begin
        readSecondValid <= 1'h1;
      end else begin
        if (willUpdateThird) begin
          readSecondValid <= 1'h0;
        end
      end
    end
    if (io_rd_reset) begin
      readThirdData <= 10'h0;
    end else begin
      if (willUpdateThird) begin
        if (readSecondValid) begin
          readThirdData <= readSecondData;
        end else begin
          readThirdData <= readFirstData;
        end
      end
    end
    if (io_rd_reset) begin
      readThirdValid <= 1'h0;
    end else begin
      if (willUpdateThird) begin
        readThirdValid <= 1'h1;
      end else begin
        if (_T_55) begin
          readThirdValid <= 1'h0;
        end
      end
    end
    if (io_rd_reset) begin
      readValid <= 1'h0;
    end else begin
      if (willUpdateThird) begin
        readValid <= 1'h1;
      end else begin
        if (_T_55) begin
          readValid <= 1'h0;
        end
      end
    end
  end
endmodule
module AsyncFIFODecoupledExample( // @[:@187.2]
  input        clock, // @[:@188.4]
  input        reset, // @[:@189.4]
  input        io_wr_clock, // @[:@190.4]
  input        io_wr_reset, // @[:@190.4]
  output       io_wr_valid, // @[:@190.4]
  output       io_wr_ready, // @[:@190.4]
  output [9:0] io_wr_bits, // @[:@190.4]
  input        io_rd_clock, // @[:@190.4]
  input        io_rd_reset, // @[:@190.4]
  output       io_rd_valid, // @[:@190.4]
  output       io_rd_ready, // @[:@190.4]
  output [9:0] io_rd_bits // @[:@190.4]
);
  wire  fifo_io_wr_ready; // @[AsyncFIFODecoupledExample.scala 24:20:@192.4]
  wire  fifo_io_wr_valid; // @[AsyncFIFODecoupledExample.scala 24:20:@192.4]
  wire [9:0] fifo_io_wr_bits; // @[AsyncFIFODecoupledExample.scala 24:20:@192.4]
  wire  fifo_io_rd_ready; // @[AsyncFIFODecoupledExample.scala 24:20:@192.4]
  wire  fifo_io_rd_valid; // @[AsyncFIFODecoupledExample.scala 24:20:@192.4]
  wire [9:0] fifo_io_rd_bits; // @[AsyncFIFODecoupledExample.scala 24:20:@192.4]
  wire  fifo_io_rd_reset; // @[AsyncFIFODecoupledExample.scala 24:20:@192.4]
  wire  fifo_io_wr_reset; // @[AsyncFIFODecoupledExample.scala 24:20:@192.4]
  wire  fifo_io_wr_clock; // @[AsyncFIFODecoupledExample.scala 24:20:@192.4]
  wire  fifo_io_rd_clock; // @[AsyncFIFODecoupledExample.scala 24:20:@192.4]
  reg [9:0] counter; // @[AsyncFIFODecoupledExample.scala 35:26:@201.4]
  reg [31:0] _RAND_0;
  reg [7:0] delay; // @[AsyncFIFODecoupledExample.scala 36:24:@202.4]
  reg [31:0] _RAND_1;
  reg [1:0] state; // @[AsyncFIFODecoupledExample.scala 37:24:@203.4]
  reg [31:0] _RAND_2;
  reg  writeValid; // @[AsyncFIFODecoupledExample.scala 38:29:@204.4]
  reg [31:0] _RAND_3;
  wire  _T_34; // @[Conditional.scala 37:30:@205.4]
  wire  _T_35; // @[Conditional.scala 37:30:@210.6]
  wire  _T_37; // @[AsyncFIFODecoupledExample.scala 45:14:@212.8]
  wire [1:0] _GEN_0; // @[AsyncFIFODecoupledExample.scala 45:33:@213.8]
  wire  _T_38; // @[Conditional.scala 37:30:@218.8]
  wire  _T_40; // @[AsyncFIFODecoupledExample.scala 50:20:@220.10]
  wire [1:0] _GEN_1; // @[AsyncFIFODecoupledExample.scala 50:31:@221.10]
  wire [1:0] _GEN_2; // @[Conditional.scala 39:67:@219.8]
  wire [1:0] _GEN_3; // @[Conditional.scala 39:67:@211.6]
  wire [1:0] _GEN_4; // @[Conditional.scala 40:58:@206.4]
  wire  _T_41; // @[AsyncFIFODecoupledExample.scala 56:16:@225.4]
  wire [8:0] _T_43; // @[AsyncFIFODecoupledExample.scala 57:22:@227.6]
  wire [7:0] _T_44; // @[AsyncFIFODecoupledExample.scala 57:22:@228.6]
  wire [7:0] _GEN_5; // @[AsyncFIFODecoupledExample.scala 56:26:@226.4]
  wire  _T_46; // @[AsyncFIFODecoupledExample.scala 62:27:@234.4]
  wire [10:0] _T_48; // @[AsyncFIFODecoupledExample.scala 63:26:@236.6]
  wire [9:0] _T_49; // @[AsyncFIFODecoupledExample.scala 63:26:@237.6]
  wire [9:0] _GEN_6; // @[AsyncFIFODecoupledExample.scala 62:47:@235.4]
  wire  _T_50; // @[AsyncFIFODecoupledExample.scala 66:16:@240.4]
  reg [7:0] delay_1; // @[AsyncFIFODecoupledExample.scala 83:24:@252.4]
  reg [31:0] _RAND_4;
  reg [1:0] state_1; // @[AsyncFIFODecoupledExample.scala 84:24:@253.4]
  reg [31:0] _RAND_5;
  reg  readReady; // @[AsyncFIFODecoupledExample.scala 85:28:@254.4]
  reg [31:0] _RAND_6;
  wire  _T_58; // @[Conditional.scala 37:30:@255.4]
  wire  _T_59; // @[Conditional.scala 37:30:@260.6]
  wire  _T_61; // @[AsyncFIFODecoupledExample.scala 92:15:@262.8]
  wire [1:0] _GEN_8; // @[AsyncFIFODecoupledExample.scala 92:35:@263.8]
  wire  _T_62; // @[Conditional.scala 37:30:@268.8]
  wire  _T_64; // @[AsyncFIFODecoupledExample.scala 97:21:@270.10]
  wire [1:0] _GEN_9; // @[AsyncFIFODecoupledExample.scala 97:32:@271.10]
  wire [1:0] _GEN_10; // @[Conditional.scala 39:67:@269.8]
  wire [1:0] _GEN_11; // @[Conditional.scala 39:67:@261.6]
  wire [1:0] _GEN_12; // @[Conditional.scala 40:58:@256.4]
  wire  _T_65; // @[AsyncFIFODecoupledExample.scala 103:16:@275.4]
  wire [8:0] _T_67; // @[AsyncFIFODecoupledExample.scala 104:22:@277.6]
  wire [7:0] _T_68; // @[AsyncFIFODecoupledExample.scala 104:22:@278.6]
  wire [7:0] _GEN_13; // @[AsyncFIFODecoupledExample.scala 103:26:@276.4]
  wire  _T_70; // @[AsyncFIFODecoupledExample.scala 109:17:@284.4]
  AsyncFIFODecoupled fifo ( // @[AsyncFIFODecoupledExample.scala 24:20:@192.4]
    .io_wr_ready(fifo_io_wr_ready),
    .io_wr_valid(fifo_io_wr_valid),
    .io_wr_bits(fifo_io_wr_bits),
    .io_rd_ready(fifo_io_rd_ready),
    .io_rd_valid(fifo_io_rd_valid),
    .io_rd_bits(fifo_io_rd_bits),
    .io_rd_reset(fifo_io_rd_reset),
    .io_wr_reset(fifo_io_wr_reset),
    .io_wr_clock(fifo_io_wr_clock),
    .io_rd_clock(fifo_io_rd_clock)
  );
  assign _T_34 = 2'h0 == state; // @[Conditional.scala 37:30:@205.4]
  assign _T_35 = 2'h1 == state; // @[Conditional.scala 37:30:@210.6]
  assign _T_37 = fifo_io_wr_ready == 1'h0; // @[AsyncFIFODecoupledExample.scala 45:14:@212.8]
  assign _GEN_0 = _T_37 ? 2'h2 : state; // @[AsyncFIFODecoupledExample.scala 45:33:@213.8]
  assign _T_38 = 2'h2 == state; // @[Conditional.scala 37:30:@218.8]
  assign _T_40 = delay == 8'hff; // @[AsyncFIFODecoupledExample.scala 50:20:@220.10]
  assign _GEN_1 = _T_40 ? 2'h1 : state; // @[AsyncFIFODecoupledExample.scala 50:31:@221.10]
  assign _GEN_2 = _T_38 ? _GEN_1 : state; // @[Conditional.scala 39:67:@219.8]
  assign _GEN_3 = _T_35 ? _GEN_0 : _GEN_2; // @[Conditional.scala 39:67:@211.6]
  assign _GEN_4 = _T_34 ? 2'h1 : _GEN_3; // @[Conditional.scala 40:58:@206.4]
  assign _T_41 = state == 2'h2; // @[AsyncFIFODecoupledExample.scala 56:16:@225.4]
  assign _T_43 = delay + 8'h1; // @[AsyncFIFODecoupledExample.scala 57:22:@227.6]
  assign _T_44 = delay + 8'h1; // @[AsyncFIFODecoupledExample.scala 57:22:@228.6]
  assign _GEN_5 = _T_41 ? _T_44 : 8'h0; // @[AsyncFIFODecoupledExample.scala 56:26:@226.4]
  assign _T_46 = fifo_io_wr_valid & fifo_io_wr_ready; // @[AsyncFIFODecoupledExample.scala 62:27:@234.4]
  assign _T_48 = counter + 10'h1; // @[AsyncFIFODecoupledExample.scala 63:26:@236.6]
  assign _T_49 = counter + 10'h1; // @[AsyncFIFODecoupledExample.scala 63:26:@237.6]
  assign _GEN_6 = _T_46 ? _T_49 : counter; // @[AsyncFIFODecoupledExample.scala 62:47:@235.4]
  assign _T_50 = state == 2'h1; // @[AsyncFIFODecoupledExample.scala 66:16:@240.4]
  assign _T_58 = 2'h0 == state_1; // @[Conditional.scala 37:30:@255.4]
  assign _T_59 = 2'h1 == state_1; // @[Conditional.scala 37:30:@260.6]
  assign _T_61 = fifo_io_rd_valid == 1'h0; // @[AsyncFIFODecoupledExample.scala 92:15:@262.8]
  assign _GEN_8 = _T_61 ? 2'h2 : state_1; // @[AsyncFIFODecoupledExample.scala 92:35:@263.8]
  assign _T_62 = 2'h2 == state_1; // @[Conditional.scala 37:30:@268.8]
  assign _T_64 = delay_1 == 8'hff; // @[AsyncFIFODecoupledExample.scala 97:21:@270.10]
  assign _GEN_9 = _T_64 ? 2'h1 : state_1; // @[AsyncFIFODecoupledExample.scala 97:32:@271.10]
  assign _GEN_10 = _T_62 ? _GEN_9 : state_1; // @[Conditional.scala 39:67:@269.8]
  assign _GEN_11 = _T_59 ? _GEN_8 : _GEN_10; // @[Conditional.scala 39:67:@261.6]
  assign _GEN_12 = _T_58 ? 2'h1 : _GEN_11; // @[Conditional.scala 40:58:@256.4]
  assign _T_65 = state_1 == 2'h2; // @[AsyncFIFODecoupledExample.scala 103:16:@275.4]
  assign _T_67 = delay_1 + 8'h1; // @[AsyncFIFODecoupledExample.scala 104:22:@277.6]
  assign _T_68 = delay_1 + 8'h1; // @[AsyncFIFODecoupledExample.scala 104:22:@278.6]
  assign _GEN_13 = _T_65 ? _T_68 : 8'h0; // @[AsyncFIFODecoupledExample.scala 103:26:@276.4]
  assign _T_70 = state_1 == 2'h1; // @[AsyncFIFODecoupledExample.scala 109:17:@284.4]
  assign io_wr_valid = writeValid; // @[AsyncFIFODecoupledExample.scala 76:17:@250.4]
  assign io_wr_ready = fifo_io_wr_ready; // @[AsyncFIFODecoupledExample.scala 77:17:@251.4]
  assign io_wr_bits = fifo_io_wr_bits; // @[AsyncFIFODecoupledExample.scala 75:16:@249.4]
  assign io_rd_valid = fifo_io_rd_valid; // @[AsyncFIFODecoupledExample.scala 118:17:@293.4]
  assign io_rd_ready = fifo_io_rd_ready; // @[AsyncFIFODecoupledExample.scala 119:17:@294.4]
  assign io_rd_bits = fifo_io_rd_bits; // @[AsyncFIFODecoupledExample.scala 117:16:@292.4]
  assign fifo_io_wr_valid = writeValid; // @[AsyncFIFODecoupledExample.scala 72:22:@247.4]
  assign fifo_io_wr_bits = counter; // @[AsyncFIFODecoupledExample.scala 73:21:@248.4]
  assign fifo_io_rd_ready = readReady; // @[AsyncFIFODecoupledExample.scala 115:22:@291.4]
  assign fifo_io_rd_reset = io_rd_reset; // @[AsyncFIFODecoupledExample.scala 30:20:@200.4]
  assign fifo_io_wr_reset = io_wr_reset; // @[AsyncFIFODecoupledExample.scala 27:20:@197.4]
  assign fifo_io_wr_clock = io_wr_clock; // @[AsyncFIFODecoupledExample.scala 26:20:@196.4]
  assign fifo_io_rd_clock = io_rd_clock; // @[AsyncFIFODecoupledExample.scala 29:20:@199.4]
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
  counter = _RAND_0[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  delay = _RAND_1[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  state = _RAND_2[1:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  writeValid = _RAND_3[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{`RANDOM}};
  delay_1 = _RAND_4[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{`RANDOM}};
  state_1 = _RAND_5[1:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_6 = {1{`RANDOM}};
  readReady = _RAND_6[0:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge io_wr_clock) begin
    if (io_wr_reset) begin
      counter <= 10'h0;
    end else begin
      if (_T_46) begin
        counter <= _T_49;
      end
    end
    if (io_wr_reset) begin
      delay <= 8'h0;
    end else begin
      if (_T_41) begin
        delay <= _T_44;
      end else begin
        delay <= 8'h0;
      end
    end
    if (io_wr_reset) begin
      state <= 2'h0;
    end else begin
      if (_T_34) begin
        state <= 2'h1;
      end else begin
        if (_T_35) begin
          if (_T_37) begin
            state <= 2'h2;
          end
        end else begin
          if (_T_38) begin
            if (_T_40) begin
              state <= 2'h1;
            end
          end
        end
      end
    end
    if (io_wr_reset) begin
      writeValid <= 1'h0;
    end else begin
      writeValid <= _T_50;
    end
  end
  always @(posedge io_rd_clock) begin
    if (io_rd_reset) begin
      delay_1 <= 8'h0;
    end else begin
      if (_T_65) begin
        delay_1 <= _T_68;
      end else begin
        delay_1 <= 8'h0;
      end
    end
    if (io_rd_reset) begin
      state_1 <= 2'h0;
    end else begin
      if (_T_58) begin
        state_1 <= 2'h1;
      end else begin
        if (_T_59) begin
          if (_T_61) begin
            state_1 <= 2'h2;
          end
        end else begin
          if (_T_62) begin
            if (_T_64) begin
              state_1 <= 2'h1;
            end
          end
        end
      end
    end
    if (io_rd_reset) begin
      readReady <= 1'h0;
    end else begin
      readReady <= _T_70;
    end
  end
endmodule
