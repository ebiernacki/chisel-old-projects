package AdderCircuits

import chisel3._
import chisel3.util._

class bitSubset() extends Module { 
    val io = IO(new Bundle{
        // val input     = Input(UInt(8.W))
        
        val output   = Output(UInt(5.W))
    })// val subset = Wire(Vec(8, UInt(10.W)))
    // var tBools = Vec(5, Bool())

   // Define temp as a Wire
    val temp = Wire(Vec(5, UInt(5.W)))

    val aB = Wire(Vec(5, Bool()))

    for(_ <- temp){
        
    }

    io.output := temp
    
} 