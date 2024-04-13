/*
* Blink.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
* 
* Content:
* Blinks the LED once every second. Set up for the Basys3 100MHz internal clock
*/

package LED

import chisel3._

class Blink extends Module {
	val io = IO(new Bundle {
		val led = Output(UInt(1.W))
	})
	val CNT_MAX = (100000000 - 1).U //for basys3

	val cntReg = RegInit(0.U(32.W))
	val blkReg = RegInit(0.U(1.W))

	cntReg := cntReg + 1.U

	when(cntReg === CNT_MAX) {
		cntReg := 0.U
		blkReg := ~blkReg
	}

	io.led := blkReg
}

/**
* An object extending App to generate the Verilog code.
*/
// object BlinkMain extends App {
//     (new chisel3.stage.ChiselStage).emitVerilog(new Blink, Array("--target-dir", "generated"))
// }