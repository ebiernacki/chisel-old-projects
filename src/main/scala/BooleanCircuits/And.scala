/*
* And.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
* 
* Contents:
* Use right most switches as inputs and LED on the left shows output of the AND of those switches
* 
* Corresponding Test: 
*/


package BooleanCircuits

import chisel3._

import chisel3.stage._
import circt.stage._


object AndMain extends App {
    class And extends Module {
        val io = IO(new Bundle{
            val a = Input(UInt(1.W))
            val b = Input(UInt(1.W))

            val z = Output(UInt(1.W))
        })
        
        io.z := io.a & io.b
    }
    
    new ChiselStage().execute(args, Seq(CIRCTTargetAnnotation(CIRCTTarget.Verilog)))
   
}

// args, Seq(
        //     circt.stage.CIRCTTargetAnnotation(circt.stage.CIRCTTarget.Verilog),
        //     ChiselGeneratorAnnotation(() => new And)
            
        // )
// circt.stage.FirtoolOption("--lowering-options=disallowPackedArrays,disallowLocalVariables"),
            // circt.stage.FirtoolOption("--split-verilog"),
            // circt.stage.FirtoolOption("--dedup"),
            // circt.stage.FirtoolOption("--strip-debug-info"),
            // circt.stage.FirtoolOption("--extract-test-code"),
            // circt.stage.FirtoolOption("-o=test/")
/**
* An object extending App to generate the Verilog code.
*/
// object AndMain extends App {
//     (new chisel3.stage.ChiselStage).emitVerilog(new And, Array("--target-dir", "generated"))
// }