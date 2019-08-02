module SyncFIFO( // @[:@3.2]
  input         clock, // @[:@4.4]
  input         reset, // @[:@5.4]
  input  [31:0] io_wr_data, // @[:@6.4]
  input         io_wr_valid, // @[:@6.4]
  output        io_wr_full, // @[:@6.4]
  output [31:0] io_rd_data, // @[:@6.4]
  input         io_rd_valid, // @[:@6.4]
  output        io_rd_empty // @[:@6.4]
);
  reg [31:0] memory [0:1023]; // @[SyncFIFO.scala 73:19:@78.4]
  reg [31:0] _RAND_0;
  wire [31:0] memory__T_81_data; // @[SyncFIFO.scala 73:19:@78.4]
  wire [9:0] memory__T_81_addr; // @[SyncFIFO.scala 73:19:@78.4]
  wire [31:0] memory__T_80_data; // @[SyncFIFO.scala 73:19:@78.4]
  wire [9:0] memory__T_80_addr; // @[SyncFIFO.scala 73:19:@78.4]
  wire  memory__T_80_mask; // @[SyncFIFO.scala 73:19:@78.4]
  wire  memory__T_80_en; // @[SyncFIFO.scala 73:19:@78.4]
  reg [9:0] wrPointer; // @[SyncFIFO.scala 24:26:@8.4]
  reg [31:0] _RAND_1;
  reg [9:0] rdPointer; // @[SyncFIFO.scala 25:26:@9.4]
  reg [31:0] _RAND_2;
  wire  _T_24; // @[SyncFIFO.scala 29:29:@12.4]
  wire  wrEnable; // @[SyncFIFO.scala 29:27:@13.4]
  wire  _T_27; // @[SyncFIFO.scala 30:29:@15.4]
  wire  rdEnable; // @[SyncFIFO.scala 30:27:@16.4]
  wire [10:0] _T_30; // @[SyncFIFO.scala 33:28:@19.6]
  wire [9:0] _T_31; // @[SyncFIFO.scala 33:28:@20.6]
  wire [9:0] _GEN_0; // @[SyncFIFO.scala 32:19:@18.4]
  wire [10:0] _T_33; // @[SyncFIFO.scala 36:28:@24.6]
  wire [9:0] _T_34; // @[SyncFIFO.scala 36:28:@25.6]
  wire [9:0] _GEN_1; // @[SyncFIFO.scala 35:19:@23.4]
  reg [9:0] counter; // @[SyncFIFO.scala 40:24:@28.4]
  reg [31:0] _RAND_3;
  reg  wrFull; // @[SyncFIFO.scala 41:23:@29.4]
  reg [31:0] _RAND_4;
  reg  rdEmpty; // @[SyncFIFO.scala 42:24:@30.4]
  reg [31:0] _RAND_5;
  wire  _T_45; // @[SyncFIFO.scala 47:26:@34.4]
  wire  counterWillDecrease; // @[SyncFIFO.scala 47:36:@35.4]
  wire  _T_48; // @[SyncFIFO.scala 48:37:@37.4]
  wire  counterWillIncrease; // @[SyncFIFO.scala 48:35:@38.4]
  wire  counterWillBeSame; // @[SyncFIFO.scala 49:33:@40.4]
  wire [10:0] _T_52; // @[SyncFIFO.scala 52:24:@43.6]
  wire [9:0] _T_53; // @[SyncFIFO.scala 52:24:@44.6]
  wire [10:0] _T_55; // @[SyncFIFO.scala 54:24:@49.8]
  wire [10:0] _T_56; // @[SyncFIFO.scala 54:24:@50.8]
  wire [9:0] _T_57; // @[SyncFIFO.scala 54:24:@51.8]
  wire [9:0] _GEN_2; // @[SyncFIFO.scala 53:36:@48.6]
  wire [9:0] _GEN_3; // @[SyncFIFO.scala 51:30:@42.4]
  wire [10:0] _GEN_21; // @[SyncFIFO.scala 57:18:@54.4]
  wire  _T_59; // @[SyncFIFO.scala 57:18:@54.4]
  wire  _T_60; // @[SyncFIFO.scala 57:30:@55.4]
  wire  _T_62; // @[SyncFIFO.scala 57:62:@56.4]
  wire  _T_63; // @[SyncFIFO.scala 57:80:@57.4]
  wire  _T_64; // @[SyncFIFO.scala 57:51:@58.4]
  wire  _T_68; // @[SyncFIFO.scala 63:18:@65.4]
  wire  _T_69; // @[SyncFIFO.scala 63:26:@66.4]
  wire  _T_71; // @[SyncFIFO.scala 63:58:@67.4]
  wire  _T_72; // @[SyncFIFO.scala 63:66:@68.4]
  wire  _T_73; // @[SyncFIFO.scala 63:47:@69.4]
  reg [31:0] rdData; // @[SyncFIFO.scala 74:23:@79.4]
  reg [31:0] _RAND_6;
  wire [31:0] _GEN_14; // @[SyncFIFO.scala 80:19:@84.4]
  assign memory__T_81_addr = rdPointer;
  assign memory__T_81_data = memory[memory__T_81_addr]; // @[SyncFIFO.scala 73:19:@78.4]
  assign memory__T_80_data = io_wr_data;
  assign memory__T_80_addr = wrPointer;
  assign memory__T_80_mask = 1'h1;
  assign memory__T_80_en = io_wr_valid & _T_24;
  assign _T_24 = io_wr_full == 1'h0; // @[SyncFIFO.scala 29:29:@12.4]
  assign wrEnable = io_wr_valid & _T_24; // @[SyncFIFO.scala 29:27:@13.4]
  assign _T_27 = io_rd_empty == 1'h0; // @[SyncFIFO.scala 30:29:@15.4]
  assign rdEnable = io_rd_valid & _T_27; // @[SyncFIFO.scala 30:27:@16.4]
  assign _T_30 = wrPointer + 10'h1; // @[SyncFIFO.scala 33:28:@19.6]
  assign _T_31 = wrPointer + 10'h1; // @[SyncFIFO.scala 33:28:@20.6]
  assign _GEN_0 = wrEnable ? _T_31 : wrPointer; // @[SyncFIFO.scala 32:19:@18.4]
  assign _T_33 = rdPointer + 10'h1; // @[SyncFIFO.scala 36:28:@24.6]
  assign _T_34 = rdPointer + 10'h1; // @[SyncFIFO.scala 36:28:@25.6]
  assign _GEN_1 = rdEnable ? _T_34 : rdPointer; // @[SyncFIFO.scala 35:19:@23.4]
  assign _T_45 = wrEnable == 1'h0; // @[SyncFIFO.scala 47:26:@34.4]
  assign counterWillDecrease = _T_45 & rdEnable; // @[SyncFIFO.scala 47:36:@35.4]
  assign _T_48 = rdEnable == 1'h0; // @[SyncFIFO.scala 48:37:@37.4]
  assign counterWillIncrease = wrEnable & _T_48; // @[SyncFIFO.scala 48:35:@38.4]
  assign counterWillBeSame = wrEnable == rdEnable; // @[SyncFIFO.scala 49:33:@40.4]
  assign _T_52 = counter + 10'h1; // @[SyncFIFO.scala 52:24:@43.6]
  assign _T_53 = counter + 10'h1; // @[SyncFIFO.scala 52:24:@44.6]
  assign _T_55 = counter - 10'h1; // @[SyncFIFO.scala 54:24:@49.8]
  assign _T_56 = $unsigned(_T_55); // @[SyncFIFO.scala 54:24:@50.8]
  assign _T_57 = _T_56[9:0]; // @[SyncFIFO.scala 54:24:@51.8]
  assign _GEN_2 = counterWillDecrease ? _T_57 : counter; // @[SyncFIFO.scala 53:36:@48.6]
  assign _GEN_3 = counterWillIncrease ? _T_53 : _GEN_2; // @[SyncFIFO.scala 51:30:@42.4]
  assign _GEN_21 = {{1'd0}, counter}; // @[SyncFIFO.scala 57:18:@54.4]
  assign _T_59 = _GEN_21 == 11'h400; // @[SyncFIFO.scala 57:18:@54.4]
  assign _T_60 = _T_59 & counterWillBeSame; // @[SyncFIFO.scala 57:30:@55.4]
  assign _T_62 = counter == 10'h3ff; // @[SyncFIFO.scala 57:62:@56.4]
  assign _T_63 = _T_62 & counterWillIncrease; // @[SyncFIFO.scala 57:80:@57.4]
  assign _T_64 = _T_60 | _T_63; // @[SyncFIFO.scala 57:51:@58.4]
  assign _T_68 = counter == 10'h0; // @[SyncFIFO.scala 63:18:@65.4]
  assign _T_69 = _T_68 & counterWillBeSame; // @[SyncFIFO.scala 63:26:@66.4]
  assign _T_71 = counter == 10'h1; // @[SyncFIFO.scala 63:58:@67.4]
  assign _T_72 = _T_71 & counterWillDecrease; // @[SyncFIFO.scala 63:66:@68.4]
  assign _T_73 = _T_69 | _T_72; // @[SyncFIFO.scala 63:47:@69.4]
  assign _GEN_14 = rdEnable ? memory__T_81_data : rdData; // @[SyncFIFO.scala 80:19:@84.4]
  assign io_wr_full = wrFull; // @[SyncFIFO.scala 70:14:@77.4]
  assign io_rd_data = rdData; // @[SyncFIFO.scala 84:14:@88.4]
  assign io_rd_empty = rdEmpty; // @[SyncFIFO.scala 69:15:@76.4]
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
  for (initvar = 0; initvar < 1024; initvar = initvar+1)
    memory[initvar] = _RAND_0[31:0];
  `endif // RANDOMIZE_MEM_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  wrPointer = _RAND_1[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  rdPointer = _RAND_2[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  counter = _RAND_3[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{`RANDOM}};
  wrFull = _RAND_4[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{`RANDOM}};
  rdEmpty = _RAND_5[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_6 = {1{`RANDOM}};
  rdData = _RAND_6[31:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge clock) begin
    if(memory__T_80_en & memory__T_80_mask) begin
      memory[memory__T_80_addr] <= memory__T_80_data; // @[SyncFIFO.scala 73:19:@78.4]
    end
    if (reset) begin
      wrPointer <= 10'h0;
    end else begin
      if (wrEnable) begin
        wrPointer <= _T_31;
      end
    end
    if (reset) begin
      rdPointer <= 10'h0;
    end else begin
      if (rdEnable) begin
        rdPointer <= _T_34;
      end
    end
    if (reset) begin
      counter <= 10'h0;
    end else begin
      if (counterWillIncrease) begin
        counter <= _T_53;
      end else begin
        if (counterWillDecrease) begin
          counter <= _T_57;
        end
      end
    end
    if (reset) begin
      wrFull <= 1'h0;
    end else begin
      wrFull <= _T_64;
    end
    if (reset) begin
      rdEmpty <= 1'h1;
    end else begin
      rdEmpty <= _T_73;
    end
    if (reset) begin
      rdData <= 32'h0;
    end else begin
      if (rdEnable) begin
        rdData <= memory__T_81_data;
      end
    end
  end
endmodule
