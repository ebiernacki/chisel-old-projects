package I2S_Sound

import chisel3._
import chisel3.util._

/*
From diligent:
To set up a simple 44.1 kHz audio passthrough, three control signals need to be generated by the host system board.
    1. A master clock (MCLK) at a frequency of approximately 22.579 MHz. 
    2. A serial clock (SCLK), which fully toggles once per 8 MCLK periods.
    3. A Left/Right Word Select signal, which fully toggles once per 64 SCLK periods.
    The Pmod I2S2s Master/Slave select jumper (JP1) should be placed into the Slave (SLV) position.
    Each of these control signals should be provided to the appropriate pin on both the top and bottom rows of the Pmod I2S2.
    The ADOUT_SDIN pin should be driven by the ADIN_SDOUT signal.
*/
class I2STransmitter extends Module {
    val io = IO(new Bundle {
        val sw    = Input(UInt(1.W))
        val sclk  = Output(Clock())
        val ws    = Output(Bool())
        val sdOut = Output(Bool())
    })

    //funcitons from chisel: https://www.chisel-lang.org/docs/explanations/sequential-circuits
    //check a boolean if it is a rising edge or falling edge
    def risingEdge(x: Bool) = x && !RegNext(x)
    def fallingEdge(x: Bool): Bool = !x && RegNext(x)

    def counter(max: UInt) = {
        val x = RegInit(0.asUInt(max.getWidth.W))
        x := Mux(x === max, 0.U, x + 1.U)
        x
    }

    def pulse(n: UInt) = counter(n - 1.U) === 0.U

    def toggle(p: Bool) = {
        val x = RegInit(false.B)
        x := Mux(p, !x, x)
        x
    }

    //toggle sclk every 8 internal clock ticks
    val sclkVal = toggle(pulse(4.U))
    io.sclk:= sclkVal.asClock

    // Create a new value that changes every 64 sclkVal changes
    val wsVal = toggle(pulse(256.U))

    io.ws := wsVal
    
    
    val sineWaveTable = VecInit(
        0.S(24.W), 1566.S(24.W), 3120.S(24.W), 4648.S(24.W), 
        6140.S(24.W), 7582.S(24.W), 8962.S(24.W), 10271.S(24.W),
        11497.S(24.W), 12630.S(24.W), 13662.S(24.W), 14584.S(24.W), 
        15388.S(24.W), 16068.S(24.W), 16619.S(24.W), 17036.S(24.W), 
        17316.S(24.W), 17457.S(24.W), 17457.S(24.W), 17316.S(24.W), 
        17036.S(24.W), 16619.S(24.W), 16068.S(24.W), 15388.S(24.W), 
        14584.S(24.W), 13662.S(24.W), 12630.S(24.W), 11497.S(24.W), 
        10271.S(24.W), 8962.S(24.W), 7582.S(24.W), 6140.S(24.W), 
        4648.S(24.W), 3120.S(24.W), 1566.S(24.W), 0.S(24.W), 
        -1566.S(24.W), -3120.S(24.W), -4648.S(24.W), -6140.S(24.W), 
        -7582.S(24.W), -8962.S(24.W), -10271.S(24.W), -11497.S(24.W),
        -12630.S(24.W), -13662.S(24.W), -14584.S(24.W), -15388.S(24.W),
        -16068.S(24.W), -16619.S(24.W), -17036.S(24.W), -17316.S(24.W),
        -17457.S(24.W), -17457.S(24.W), -17316.S(24.W), -17036.S(24.W),
        -16619.S(24.W), -16068.S(24.W), -15388.S(24.W), -14584.S(24.W),
        -13662.S(24.W), -12630.S(24.W), -11497.S(24.W), -10271.S(24.W),
        -8962.S(24.W), -7582.S(24.W), -6140.S(24.W), -4648.S(24.W), 
        -3120.S(24.W), -1566.S(24.W))

    // FIXME!! To play different tones
    // val a = VecInit(
        
    // )
    //passthrough mode, change data as it comes in (pitch up and down, chop), reverb (cone filter)
    //play notes via switches
    // val activeTable = Mux(io.sw.asBool, a, c)

    //what word in sineTable are we on?
    val wordCounter = RegInit(0.U(6.W))

    //What bit of the word are we on?
    val bitCounter = RegInit(0.U)

    //Are we currently putting data out on the pin?
    val tx = RegInit(false.B)

    //the value of the data going out
    val dout = RegInit(0.U)
    io.sdOut := dout

    //when we switch to Left Audio(ws), and we are at the first bit,
    //set the bit counter to 24 and set tx true
    when(risingEdge(wsVal) && bitCounter === 0.U){    
        bitCounter := 24.U
        tx := true.B
    }

    //when we are tx and on the falling edge of sclk 
    when(tx && fallingEdge(sclkVal)){    
       //bit counter--
        bitCounter := bitCounter - 1.U

        //put the data out: NOTEL this could be its own module, pull data out of table and put it as output?
        dout := sineWaveTable(wordCounter)(bitCounter-1.U)

        //check if we are at the end of a word, if we are stop transmitting and just fill the rest with 0s
        when(bitCounter === 0.U){
            dout := 0.U
            bitCounter := 0.U
            tx := false.B
            //move on the the next word
            wordCounter := wordCounter + 1.U
        }
    }
    //when we reach the end of the sineWaveTable, loop back to the begining and repeat
    when(wordCounter === sineWaveTable.length.asUInt){
        wordCounter := 0.U
    }
    
}

object I2STransmitterDriver extends App {
    emitVerilog(new I2STransmitter, Array("--target-dir", "generated"))
}

