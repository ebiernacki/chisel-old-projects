/*
* Mux.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
* 
* Contents:
* 2:1 Mux, and then 4:1 Mux from 2:1 Muxes
* 
* Corresponding Test: 
*/



package MuxDeMux

import chisel3._
import chisel3.util._

class Mux(width: Int) extends Module { //2:1
	val io = IO(new Bundle {
		val sel        = Input(UInt(1.W))
		val d1         = Input(UInt(width.W))
		val d2         = Input(UInt(width.W))
		val out        = Output(UInt(width.W))
	})

	val notSel = ~io.sel

	val vect = VecInit(Seq.fill(width)(0.U(1.W)))

	for(i <- 0 until width){
		var t1 = io.d1{i} & notSel 
		var t2 = io.d2{i} & io.sel 
		var b1 = t1 | t2
		vect(i) := b1
	}

	io.out := vect.reverse.reduce(_ ## _) //reverse bits and then concatenate 
}
 

class Mux4to1(width: Int) extends Module { //4:1
    val io = IO(new Bundle {
        val s0         = Input(Bool())
        val s1         = Input(Bool())
        val d1         = Input(UInt(width.W))
        val d2         = Input(UInt(width.W))
        val d3         = Input(UInt(width.W))
        val d4         = Input(UInt(width.W))
        val out        = Output(UInt(width.W))
    })

    val m1 = Module(new Mux(width))
    val m2 = Module(new Mux(width))

    m1.io.sel := io.s0
    m2.io.sel := io.s0

    m1.io.d1 := io.d1
    m1.io.d2 := io.d2

    m2.io.d1 := io.d3
    m2.io.d2 := io.d4

    val m3 = Module(new Mux(width))
    m3.io.sel := io.s1

    m3.io.d1 := m1.io.out
    m3.io.d2 := m2.io.out

    io.out := m3.io.out
}