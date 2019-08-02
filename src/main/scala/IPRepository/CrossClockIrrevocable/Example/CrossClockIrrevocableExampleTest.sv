module top_tb;

    logic io_src_clock;
    logic io_src_reset;
    logic io_dst_clock;
    logic io_dst_reset;
    logic io_src_count;
    logic io_src_valid;
    logic io_src_ready;
    logic io_dst_count;
    logic io_dst_valid;
    logic io_dst_ready;

    CrossClockIrrevocableExample CrossClockIrrevocableExample_dut (
        .io_src_clock    (io_src_clock),
        .io_src_reset    (io_src_reset),
        .io_dst_clock    (io_dst_clock),
        .io_dst_reset    (io_dst_reset),
        .io_src_count    (io_src_count),
        .io_src_valid    (io_src_valid),
        .io_src_ready    (io_src_ready),
        .io_dst_count    (io_dst_count),
        .io_dst_valid    (io_dst_valid),
        .io_dst_ready    (io_dst_ready)
    );

    initial begin
        repeat(1000) @(posedge io_dst_clock);
        $stop();
    end

    initial begin
        io_src_clock = 0;
        forever begin
            #5 io_src_clock = ~io_src_clock;
        end
    end

    initial begin
        io_dst_clock = 0;
        forever begin
            #8 io_dst_clock = ~io_dst_clock;
        end
    end

    initial begin
        io_src_reset = 1;
        repeat(10) @(posedge io_src_clock);
        io_src_reset = 0;
    end

    initial begin
        io_dst_reset = 1;
        repeat(10) @(posedge io_dst_clock);
        io_dst_reset = 0;
    end

endmodule
