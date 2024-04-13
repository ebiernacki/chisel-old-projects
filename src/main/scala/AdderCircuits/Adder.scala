/*
* Adder.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
* 
* Contents:
* half adder, full adder(made of halfadders), adder(ripple carry, made from half and full adders)
* 
* verilog is for ripple carry adder of 5 bits
* 
* Corresponding Test: AdderTest.scala
*/

package AdderCircuits

import chisel3._
import chisel3.util._

//full ripple-carry adder w/ initial carry of 0
class Adder(width: Int) extends Module { 
    val io = IO(new Bundle{
        val a     = Input(UInt(width.W))
        val b     = Input(UInt(width.W))
        val sum   = Output(UInt((width).W))
    })

    //vector for output
    val vect = VecInit(Seq.fill(width)(0.U(1.W))) 

    //create half adder for first bit
    val hAdd1 = Module(new HalfAdder)
    hAdd1.io.a := io.a{0}
    hAdd1.io.b := io.b{0}

    val c0 = hAdd1.io.carry
    vect(0) := hAdd1.io.sum

    var currentCarry = c0
    for(i <- 1 until width){
        val fAdd = Module(new FullAdder)
        fAdd.io.a := io.a{i}
        fAdd.io.b := io.b{i}
        fAdd.io.c_in := currentCarry
        vect(i) := fAdd.io.sum
        currentCarry = fAdd.io.c_out
    }

    //last full adder carry is ignored


    io.sum := vect.reverse.reduce(_ ## _)
}


class HalfAdder() extends Module { 
    val io = IO(new Bundle{
        val a     = Input(UInt(1.W))
        val b     = Input(UInt(1.W))
        val sum   = Output(UInt(1.W))
        val carry = Output(UInt(1.W))
    })

    io.sum := io.a ^ io.b
    io.carry := io.a & io.b
}

class FullAdder() extends Module { 
    val io = IO(new Bundle{
        val a     = Input(UInt(1.W))
        val b     = Input(UInt(1.W))
        val c_in  = Input(UInt(1.W))
        val sum   = Output(UInt(1.W))
        val c_out = Output(UInt(1.W))
    })

    val ha1 = Module(new HalfAdder)
    ha1.io.a := io.a
    ha1.io.b := io.b

    val ha2 = Module(new HalfAdder)
    ha2.io.a := io.c_in
    ha2.io.b := ha1.io.sum

    io.sum := ha2.io.sum
    io.c_out := ha1.io.carry | ha2.io.carry

}





//NOTE THIS ONLY CREATES THE FINAL VERILOG FOR A FULL ADDER
// object AdderMain extends App {
//     (new chisel3.stage.ChiselStage).emitVerilog(new Adder(5), Array("--target-dir", "generated"))
// }
