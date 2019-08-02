//  Module: top
//
module top;

    bit io_rd_clock;
    bit io_rd_reset;
    bit io_wr_clock;
    bit io_wr_reset;

    async_fifo_if vif(io_wr_clock, io_wr_reset, io_rd_clock, io_rd_reset);

    AsyncFIFODecoupled dut (
        .io_wr_ready     (vif.io_wr_ready),
        .io_wr_valid     (vif.io_wr_valid),
        .io_wr_bits      (vif.io_wr_bits),
        .io_wr_enable    (vif.io_wr_enable),
        .io_rd_ready     (vif.io_rd_ready),
        .io_rd_valid     (vif.io_rd_valid),
        .io_rd_bits      (vif.io_rd_bits),
        .io_rd_enable    (vif.io_rd_enable),
        .io_rd_reset     (vif.io_rd_reset),
        .io_wr_reset     (vif.io_wr_reset),
        .io_wr_clock     (vif.io_wr_clock),
        .io_rd_clock     (vif.io_rd_clock)
    );

    initial begin
        // reset design
        fork
            begin
                vif.m.io_wr_valid <= 1'b0;
                vif.m.io_wr_bits <= 1'b0;
                vif.m.io_wr_enable <= 1'b0;
                repeat(15) @(vif.m);
            end
            begin
                vif.s.io_rd_ready <= 1'b0;
                vif.s.io_rd_enable <= 1'b0;
                repeat(15) @(vif.s);
            end
        join


        /*
         * The fifo is empty right now.
         * The write ready signal and read valid signal should be deasserted when
         * the write enable and read enable signals are deasserted.
         */
        io_wr_ready_not_enable: assert (vif.m.io_wr_ready == 1'b0)
        else $error("Assertion io_wr_ready_not_enable failed!");
        io_rd_valid_not_enable: assert (vif.s.io_rd_valid == 1'b0)
        else $error("Assertion io_rd_valid_not_enable failed!");


        /*
         * Write data into the fifo until the fifo is full.
         */
        // enable write port
        repeat(5) @(vif.m);
        vif.m.io_wr_enable <= 1'b1;
        @(vif.m);

        // write into fifo
        for (int i = 0; i < 1027; i++) begin
            vif.m.io_wr_valid <= 1'b1;
            vif.m.io_wr_bits <= i;
            @(vif.m);

            // the fifo should not be full
            io_wr_ready_2: assert (vif.m.io_wr_ready == 1'b1)
            else $error("Assertion io_wr_ready failed!");
        end
        vif.m.io_wr_valid <= 1'b0;
        vif.m.io_wr_bits <= 1'b0;

        // fifo should be full
        @(vif.m);
        io_wr_ready_3: assert (vif.m.io_wr_ready == 1'b0)
        else $error("Assertion io_wr_ready failed!");
        vif.m.io_wr_enable <= 1'b0;
        @(vif.m);


        /*
         * The fifo is full right now.
         * However, when read enable and write enable signals are deasserted,
         * the write ready and read valid signals should still be deasserted.
         */
        io_wr_ready_fifo_full: assert (vif.m.io_wr_ready == 1'b0)
        else $error("Assertion io_wr_ready_fifo_full failed!");
        io_rd_valid_fifo_full: assert (vif.s.io_rd_valid == 1'b0)
        else $error("Assertion io_rd_valid_fifo_full failed!");


        /*
         * Read data from fifo until the fifo is empty.
         */
        // enable read port
        repeat(5) @(vif.s);
        vif.s.io_rd_enable <= 1'b1;
        repeat(2) @(vif.s);
        for (int i = 0; i < 1027; i++) begin
            vif.s.io_rd_ready <= 1'b1;
            @(vif.s);

            // the fifo should not be empty
            io_rd_valid_2: assert (vif.s.io_rd_valid == 1'b1)
            else $error("Assertion io_rd_valid failed!");
        end
        vif.s.io_rd_ready <= 1'b0;

        // the fifo should be empty
        @(vif.s);
        io_rd_valid_3: assert (vif.s.io_rd_valid == 1'b0)
        else $error("Assertion io_rd_valid failed!");
        vif.s.io_rd_enable <= 1'b0;
        @(vif.s);


        /*
         * write when write enable signal is deasserted.
         * the data should not be write into the fifo.
         */
        // try to write into fifo
        repeat(5) @(vif.m);
        fork
            begin
                vif.m.io_wr_valid <= 1'b1;
                vif.m.io_wr_bits <= 32'b1;
                @(vif.m);
                vif.m.io_wr_valid <= 1'b0;
                vif.m.io_wr_bits <= 32'b0;
                @(vif.m);
            end
            begin
                vif.s.io_rd_enable <= 1'b1;
                @(vif.s);
            end
        join

        // the fifo should be empty
        repeat(10) @(vif.s);
        io_read_valid_write_not_enabled: assert (vif.s.io_rd_valid == 1'b0)
        else $error("Assertion io_read_valid_write_not_enable failed!");
        vif.s.io_rd_enable <= 1'b0;
        @(vif.s);


        /*
         * Read when read enable is deasserted.
         * The data should not be read successfully.
         */
        // write into fifo
        repeat(5) @(vif.m);
        vif.m.io_wr_enable <= 1'b1;
        repeat(1) @(vif.m);
        vif.m.io_wr_valid <= 1'b1;
        vif.m.io_wr_bits <= 32'b1;
        @(vif.m);
        vif.m.io_wr_enable <= 1'b0;
        vif.m.io_wr_valid <= 1'b0;
        vif.m.io_wr_bits <= 32'b0;
        @(vif.m);

        // try to read from fifo
        repeat(10) @(vif.s);
        vif.s.io_rd_ready <= 1'b1;
        @(vif.s);
        vif.s.io_rd_ready <= 1'b0;
        @(vif.s);

        // the data should still inside the fifo
        vif.s.io_rd_enable <= 1'b1;
        @(vif.s);
        io_read_valid_read_enabled: assert (vif.s.io_rd_valid == 1'b0)
        else $error("Assertion io_read_valid_read_enabled failed!");

        // stop simulation
        repeat(5) @(vif.m);
        $stop();
    end

    initial begin
        io_rd_clock = 0;
        forever begin
            #5 io_rd_clock = ~io_rd_clock;
        end
    end

    initial begin
        io_wr_clock = 0;
        forever begin
            #8 io_wr_clock = ~io_wr_clock;
        end
    end

    initial begin
        io_rd_reset = 1;
        repeat(10) @(posedge io_rd_clock);
        io_rd_reset = 0;
    end

    initial begin
        io_wr_reset = 1;
        repeat(10) @(posedge io_wr_clock);
        io_wr_reset = 0;
    end


endmodule: top


//  Interface: async_fifo_if
//
interface async_fifo_if (
    input bit io_wr_clock,
    input bit io_wr_reset,
    input bit io_rd_clock,
    input bit io_rd_reset
);

    logic        io_wr_ready;
    logic        io_wr_valid;
    logic [31:0] io_wr_bits;
    logic        io_wr_enable;
    logic        io_rd_ready;
    logic        io_rd_valid;
    logic [31:0] io_rd_bits;
    logic        io_rd_enable;

    clocking m @(posedge io_wr_clock);
        input  io_wr_ready;
        output io_wr_valid;
        output io_wr_bits;
        output io_wr_enable;
    endclocking

    clocking s @(posedge io_rd_clock);
        output io_rd_ready;
        input  io_rd_valid;
        input  io_rd_bits;
        output io_rd_enable;
    endclocking

endinterface: async_fifo_if
