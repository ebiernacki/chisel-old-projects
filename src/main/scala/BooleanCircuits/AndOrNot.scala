/*
* AndOrNot.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
* 
* Content:
* Scala code to have right most switches be inputs
* and left most leds as outputs of logic gate combinations 
* in the order of [and, or, not(sw0)]
* 
* 
* Corresponding Test: 
*/

package BooleanCircuits

import chisel3._
import chisel3.util._

class AndOrNot extends Module{
	val io = IO(new Bundle{
		val sw      = Input(UInt(2.W))

		val led_out = Output(UInt(3.W))
	})

	//set led_out[0] to the and of the input vals
	val andVal = io.sw{0} & io.sw{1} 
	
	//set led_out[1] to the or of the input vals
	val orVal = io.sw{0} | io.sw{1}

	//set led_out[3] to the not of the first input
	val notVal = ~io.sw{0} 


	//stitch together for connection convience
	io.led_out := Cat(orVal, andVal, notVal)

}

/**
 * An object extending App to generate the Verilog code.
 */
// object AndOrNotMain extends App {
// 	(new chisel3.stage.ChiselStage).emitVerilog(new AndOrNot, Array("--target-dir", "generated"))
// }