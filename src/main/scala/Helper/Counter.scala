package Helper

/*
 * 
 * A simple counter example with configurable bit width and with a test bench.
 * 
 */

import chisel3._

/**
 * A simple, configurable counter that wraps around.
 */
class Counter(size: Int) extends Module {
  val io = IO(new Bundle {
    val out = Output(UInt(size.W))
  })

  val r1 = RegInit(0.U(size.W))
  r1 := r1 + 1.U

  io.out := r1
}

// object CounterMain extends App {
//     (new chisel3.stage.ChiselStage).emitVerilog(new Counter(3), Array("--target-dir", "generated"))
// }