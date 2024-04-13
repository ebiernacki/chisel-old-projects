package SevenSegDisplay

import chisel3._
import chisel3.util._

/*
*
* Module that handle the refresh rate of each 11bit character value
* outputs the "hot" character to be shown
* 
*/

class refreshHandler extends Module{
    val io = IO(new Bundle{
        val rate           = Input(UInt(32.W))

        //take in each of the 4 characters 11 bit segment values + anodes
        val char0          = Input(UInt(7.W))
        val char1          = Input(UInt(7.W))
        val char2          = Input(UInt(7.W))
        val char3          = Input(UInt(7.W))

        //output: the 7bit and 4bit values that are "hot" at that time
        val hot_char_seg   = Output(UInt(7.W))
        val hot_char_an    = Output(UInt(4.W))
    })
    //module to name and create anode values for characters
    val anodeSet = Module(new anodes)

    //enum of states (positions)
    val pos0 :: pos1 :: pos2 :: pos3 :: Nil = Enum(4) //Note: Chisel only allows for constant state values, so we cant use io.char0 - io.char3 as the states 

    //Set initial state and register to hold state (also one to hold the hot char, which is output)
    val posReg = RegInit(pos0)
    val hot_char = RegInit(0.U)
    

    //refresh counter to switch states(position)  
    val refresh_counter = RegInit(0.U(32.W))   //was 19 bits for 400000, now 32 for all varying speeds 
    refresh_counter := refresh_counter + 1.U 

    val rateHistoryReg = RegInit(0.U(32.W))
    when(io.rate =/= rateHistoryReg) {
        rateHistoryReg := io.rate
        refresh_counter := 0.U
    }

    //refresh every 4ms (per diligent reference) (change rate for student exercise)
    when(refresh_counter === io.rate){  
        refresh_counter := 0.U

        //switch hot char
        switch(posReg){
            is(pos0){
                //negation is done for the board values(0 is on, 1 is on)
                hot_char := ~Cat(io.char0(6), io.char0(5), io.char0(4), io.char0(3), io.char0(2), io.char0(1), io.char0(0), anodeSet.io.an0) ///0001
                posReg := pos1
            }
            is(pos1){
                hot_char := ~Cat(io.char1(6), io.char1(5), io.char1(4), io.char1(3), io.char1(2), io.char1(1), io.char1(0), anodeSet.io.an1)
                posReg := pos2
            }
            is(pos2){
                hot_char := ~Cat(io.char2(6), io.char2(5), io.char2(4), io.char2(3), io.char2(2), io.char2(1), io.char2(0), anodeSet.io.an2)
                posReg := pos3               
            }
            is(pos3){
                hot_char := ~Cat(io.char3(6), io.char3(5), io.char3(4), io.char3(3), io.char3(2), io.char3(1), io.char3(0), anodeSet.io.an3)
                posReg := pos0
            }
        }
    }

    io.hot_char_seg := hot_char(10, 4)
    io.hot_char_an  := hot_char(3, 0)
}