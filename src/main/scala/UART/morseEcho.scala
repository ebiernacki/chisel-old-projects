/*
* morseEcho.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
* 
* Content:
* UART commmunication to take keyboard input and turn it into morse code
* TODO: turn morse string into blinking light
* 
*/


package UART

import chisel3._
import chisel3.util._

class morseEcho(frequency: Int, baudRate: Int) extends Module {
    val io = IO(new Bundle {
        val txd = Output(UInt(1.W))
        val rxd = Input(UInt(1.W))
        val led = Output(UInt(1.W))
    })

    //create tx and rx modules
    val tx = Module(new BufferedTx(frequency, baudRate))
    val rx = Module(new Rx(frequency, baudRate))
    io.txd := tx.io.txd
    rx.io.rxd := io.rxd

    //get the values of the morse code based on recieved character
    val mLookup = Module(new morseLookup)
    mLookup.io.in := rx.io.channel.bits

    val posCounter = RegInit(0.U(2.W)) //keep track of which char in morse code we are at

    val stateCounter = RegInit(0.U(2.W)) //state machine for waiting vs sending info

    //default initialization: dont send anything
    tx.io.channel.bits  := 0.U
    tx.io.channel.valid := false.B

    //State machine
    switch(stateCounter) {
        is(0.U) {
            //wait until something is recived before sending anything
            when(rx.io.channel.valid) {
                posCounter := 0.U
                stateCounter := 1.U
            }
        }
        is(1.U) {
            //Transmit state

            //send the character pointed to by the position
            tx.io.channel.bits := mLookup.io.out(posCounter)
            tx.io.channel.valid := true.B

            when(tx.io.channel.ready) {
                //inc the position
                posCounter := posCounter + 1.U

                //if all 4 chars are transmitted
                when(posCounter === 3.U) {
                    stateCounter := 0.U //Return to the idle state
                }
            }
        }
    }

    //ready when in idle(ready to recieve when not sending)
    rx.io.channel.ready := stateCounter === 0.U

    //ready to send when you are putting bits out on the line
    tx.io.channel.valid := stateCounter === 1.U

    val morseLED = Module(new morseLight)
    morseLED.io.inVec := mLookup.io.out

    io.led := morseLED.io.led

}

//wrapper for the echo
class morseEchoTop(frequency: Int, baudRate: Int) extends Module {
    val io = IO(new Bundle {
        val board_rxd = Input(UInt(1.W))
        val board_txd = Output(UInt(1.W))
        val board_led = Output(UInt(1.W))
    })

    //make the module and connect to board_pins
    val e = Module(new morseEcho(frequency, baudRate))
    e.io.rxd := io.board_rxd
    io.board_txd := e.io.txd

    io.board_led := e.io.led
}

//object to emit verilog
object morseEchoMain extends App {
  emitVerilog(new morseEchoTop(100000000, 115200), Array("--target-dir", "generated"))
}