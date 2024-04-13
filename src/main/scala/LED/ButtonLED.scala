/*
* ButtonLED.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
* 
* Content:
* Connects a button to an LED, if the button is pressed then the LED is on
*/


package LED

import chisel3._
import chisel3.util._

class ButtonLED extends Module {
  val io = IO(new Bundle {
    val button = Input(Bool())
    val led = Output(Bool())
  })

  val ledState = RegInit(false.B)

  // Check if the button is pressed
  when(io.button) {
    ledState := true.B
  }.otherwise {
    ledState := false.B
  }

  // Connect the LED to the LED state
  io.led := ledState
}



/**
* An object extending App to generate the Verilog code.
*/
// object ButtonLEDMain extends App {
//     (new chisel3.stage.ChiselStage).emitVerilog(new ButtonLED, Array("--target-dir", "generated"))
// }