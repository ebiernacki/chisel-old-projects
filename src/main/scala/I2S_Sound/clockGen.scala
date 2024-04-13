package I2S_Sound

import chisel3._
import chisel3.util._
import chisel3.experimental

class clockGen extends Module {
    val io = IO(new Bundle {
        val mclk  = Output(Clock())
        val sclk  = Output(Clock())
        val ws    = Output(Bool())
    })
    // Define the division factor
    val divisionFactor = 5

    // Counter to divide the clock
    val counterReg = RegInit(0.U(4.W))

    val mclkVal = RegInit(false.B)
    // Toggle the output clock signal
    when(counterReg === (divisionFactor - 1).U) {
        counterReg := 0.U
        mclkVal := ~mclkVal
    }.otherwise {
        counterReg := counterReg + 1.U
    }
    io.mclk := mclkVal.asClock

    //following four functions are from the chisel website: https://www.chisel-lang.org/docs/explanations/sequential-circuits
    def risingEdge(x: Bool) = x && !RegNext(x)
    def fallingEdge(x: Bool): Bool = !x && RegNext(x)


    //broken code:::
    // def counter(max: UInt) = {
    //     val x = RegInit(0.asUInt(max.getWidth.W))

    //     when(risingEdge(mclkVal)){
    //         when(x === max){
    //             x := 0.U
    //         }.otherwise{
    //             x := x + 1.U
    //         }
    //     }
    //     x
    // }

    // def pulse(n: UInt) = counter(n - 1.U) === 0.U

    // def toggle(p: Bool) = {
    //     val x = RegInit(false.B)
    //     x := Mux(p, !x, x)
    //     x
    // }

    // //toggle sclk every 4 internal clock ticks, so there is a full toggle every 8 
    // val sclkVal = toggle(pulse(4.U))

    // //serial clock out: rate to shift bits out onto the data pin
    // io.sclk:= sclkVal.asClock

    // // Create a new value that fully changes every 64 sclkVal changes: or 4*64 = 256 so you change on the internal clock
    // val wsVal = toggle(pulse(256.U))


    // //ws output: Left or Right Audio select
    // io.ws := wsVal


}