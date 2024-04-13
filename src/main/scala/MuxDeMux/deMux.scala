/*
* deMux.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
* 
* Contents:
* 1:2 deMux, 1:4 deMux made of 1:2 deMuxes
* 
* Corresponding Test: 
*/


package MuxDeMux

import chisel3._
import chisel3.util._

class deMux(width: Int) extends Module { //1to2 
    //for multibit result(and in) could use bundle of bools to represent bits????
    val io = IO(new Bundle {
        val sel        = Input(Bool())
        val in         = Input(UInt(width.W))
        val d1         = Output(UInt(width.W))
        val d2         = Output(UInt(width.W))
    })

    val notSel = ~io.sel

    val vectd1 = VecInit(Seq.fill(width)(0.U(1.W)))
    val vectd2 = VecInit(Seq.fill(width)(0.U(1.W)))

    for(i <- 0 until width){
        vectd1(i) := notSel & io.in{i}
        vectd2(i) := io.sel & io.in{i}
    }

    io.d1 := vectd1.reverse.reduce(_ ## _)
    io.d2 := vectd2.reverse.reduce(_ ## _)
}


class deMux1to4(width: Int) extends Module { //1to4
    val io = IO(new Bundle {
        val s0         = Input(UInt(1.W))
        val s1         = Input(UInt(1.W))
        val in         = Input(UInt(width.W))
        val d1         = Output(UInt(width.W))
        val d2         = Output(UInt(width.W))
        val d3         = Output(UInt(width.W))
        val d4         = Output(UInt(width.W))
    })

    val dm1 = Module(new deMux(width))

    dm1.io.sel := io.s1
    dm1.io.in := io.in

    val dm2 = Module(new deMux(width))
    val dm3 = Module(new deMux(width))

    dm2.io.sel := io.s0
    dm3.io.sel := io.s0

    dm2.io.in := dm1.io.d1
    dm3.io.in := dm1.io.d2

    io.d1 := dm2.io.d1
    io.d2 := dm2.io.d2

    io.d3 := dm3.io.d1
    io.d4 := dm3.io.d2
}
