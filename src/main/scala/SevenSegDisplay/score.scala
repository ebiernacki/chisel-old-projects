package SevenSegDisplay

import chisel3._
import chisel3.util._
import Helper._
/*
* Module that takes in the score value as UInts
* Converts it to bcd
* Matches the bcd to the segments
* outputs the segment values as UInts
*/

class score extends Module {
    val io = IO(new Bundle{
        val scoreValue = Input(UInt(14.W))//max bits of binToBCD = 14, 9999 in binary
        val char0_seg = Output(UInt(7.W))
        val char1_seg = Output(UInt(7.W))
        val char2_seg = Output(UInt(7.W))
        val char3_seg = Output(UInt(7.W))
    })
    //create binToBCD module to parse the score
    val bin2bcd = Module(new binToBCD)
    bin2bcd.io.bin := io.scoreValue

    val segLookup = Module(new segLookup)
    segLookup.io.in := bin2bcd.io.bcd
    val segVector = segLookup.io.SegVec

    //assign the segments to the characters
    io.char0_seg := segVector(0)
    io.char1_seg := segVector(1)
    io.char2_seg := segVector(2)
    io.char3_seg := segVector(3)
}