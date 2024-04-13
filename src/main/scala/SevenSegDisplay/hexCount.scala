package SevenSegDisplay

import chisel3._
import chisel3.util._



class hexCount extends Module {
    val io = IO(new Bundle {
        val out = Output(UInt(16.W))
    })
    val clkReg = RegInit(0.U(27.W))
    val counterReg = RegInit(0.U(16.W))
    
    clkReg := clkReg + 1.U

    when(clkReg === 25000000.U){//100000000
        clkReg := 0.U
        when(counterReg === 65535.U) {
            counterReg := 0.U
        }.otherwise {
            counterReg := counterReg + 1.U
        }
    }.otherwise{
        clkReg := clkReg + 1.U
    }

    io.out := counterReg
    
}

class hexCountTop extends Module {
    val io = IO(new Bundle {
        val board_segments  = Output(UInt(7.W))
        val board_anode     = Output(UInt(4.W))
    })
    //create hexcounter

    val hexCounter = Module(new hexCount)

    //use score 
    val segLookup = Module(new segLookup)
    segLookup.io.in := hexCounter.io.out 

    val display = Module(new topLevelDisplay)
    display.io.refreshRate := 400000.U //4ms 
    display.io.char0_seg := segLookup.io.SegVec(0)
    display.io.char1_seg := segLookup.io.SegVec(1)
    display.io.char2_seg := segLookup.io.SegVec(2)
    display.io.char3_seg := segLookup.io.SegVec(3)

    io.board_anode := display.io.board_anode
    io.board_segments := display.io.board_segments
    
}

// object hexCountMain extends App {
//     (new chisel3.stage.ChiselStage).emitVerilog(new hexCountTop, Array("--target-dir", "generated"))
// }