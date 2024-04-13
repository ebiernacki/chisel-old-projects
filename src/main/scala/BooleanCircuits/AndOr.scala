/*
* AndOr.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
* 
* Contents:
* And of input A and B, or'd with C, output to Q
* Uses ambiguous width for wide use and testing 
* 
* Corresponding Test Case: 
*/



package BooleanCircuits

import chisel3._


class AndOr(width: Int) extends Module {
    val io = IO(new Bundle{
        val a = Input(UInt(width.W))
        val b = Input(UInt(width.W))
        val c = Input(UInt(width.W))

        val q = Output(UInt(width.W))
    })
    io.q := (io.a & io.b) | io.c
}

/*
* No verliog for this, just test cases for testing logic
*/