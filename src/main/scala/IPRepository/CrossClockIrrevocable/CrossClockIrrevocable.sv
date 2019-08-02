interface tb_if(
    input bit io_src_clock,
    input bit io_src_reset,
    input bit io_dst_clock,
    input bit io_dst_reset
);

    logic [7:0] io_src_bits;
    logic       io_src_valid;
    logic       io_src_ready;
    logic [7:0] io_dst_bits;
    logic       io_dst_valid;
    logic       io_dst_ready;

    clocking m @(posedge io_src_clock);
        output io_src_bits;
        output io_src_valid;
        input  io_src_ready;
    endclocking

    clocking s @(posedge io_dst_clock);
        input  io_dst_bits;
        input  io_dst_valid;
        output io_dst_ready;
    endclocking

endinterface

module top;

    bit io_src_clock;
    bit io_src_reset;
    bit io_dst_clock;
    bit io_dst_reset;

    tb_if vif(io_src_clock, io_src_reset, io_dst_clock, io_dst_reset);

    CrossClockIrrevocable u_CrossClockIrrevocable (
        .io_src_ready    (vif.io_src_ready),
        .io_src_valid    (vif.io_src_valid),
        .io_src_bits     (vif.io_src_bits),
        .io_dst_ready    (vif.io_dst_ready),
        .io_dst_valid    (vif.io_dst_valid),
        .io_dst_bits     (vif.io_dst_bits),
        .io_src_clock    (vif.io_src_clock),
        .io_src_reset    (vif.io_src_reset),
        .io_dst_clock    (vif.io_dst_clock),
        .io_dst_reset    (vif.io_dst_reset)
    );

    initial begin
        fork
            begin
                vif.m.io_src_bits <= 8'b0;
                vif.m.io_src_valid <= 1'b0;
                repeat(15) @(vif.m);

                // poke value 1
                vif.m.io_src_valid <= 1'b1;
                vif.m.io_src_bits <= 8'b1;
                do
                    @(vif.m);
                while (! (vif.io_src_valid & vif.m.io_src_ready));

                // poke value 2
                vif.m.io_src_valid <= 1'b1;
                vif.m.io_src_bits <= 8'd2;
                do
                    @(vif.m);
                while (! (vif.io_src_valid & vif.m.io_src_ready));

                // stop simulation
                vif.m.io_src_valid <= 1'b0;
                vif.m.io_src_bits <= 8'd0;
                @(vif.m);
                $display("master side complete");
            end
            begin
                vif.s.io_dst_ready <= 1'b0;
                repeat(15) @(vif.s);

                // recv value 1
                while (! vif.s.io_dst_valid) begin
                    @(vif.s);
                end
                vif.s.io_dst_ready <= 1'b1;
                @(vif.s);
                vif.s.io_dst_ready <= 1'b0;
                @(vif.s);

                // recv value 2
                vif.s.io_dst_ready <= 1'b1;
                do
                    @(vif.s);
                while (! (vif.s.io_dst_valid & vif.io_dst_ready));

                // stop simulation
                vif.s.io_dst_ready <= 1'b0;
                @(vif.s);
                $display("slave side complete");
            end
        join

        // stop the simulation
        repeat(5) @(vif.s);
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
