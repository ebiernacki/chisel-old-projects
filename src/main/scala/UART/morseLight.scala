/*
* morseLight.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
* 
* Content:
* Recieve the vector of morse values from morseLookup
* Output the led value
* 
* Match each value in the string to a '-' or a '.'
* '-' = long flash = 3s
* '.' = short flash = 1s 
*/



package UART

import chisel3._
import chisel3.util._


class morseLight extends Module {
    val io = IO(new Bundle{
        val inVec = Input(Vec(4, UInt(8.W)))
        val led   = Output(UInt(1.W))
    })

    val dotTime   = 100000000.U // 1 second at 100 MHz
    val dashTime  = 300000000.U // 3 seconds at 100 MHz
    val pauseTime = 50000000.U  // 0.5 seconds at 100 MHz

    val counterReg = RegInit(0.U(32.W))

    val ledReg = RegInit(0.U(1.W)) //LED on or off

    val stateReg = RegInit(0.U(2.W)) //state for turning off/on and pausing in between

    val posCounter = RegInit(0.U)

    val lastInVec = RegNext(io.inVec.asUInt) // Register to store the last inVec

    counterReg := counterReg - 1.U

    //ron advice: watch out fo rnested whens, they may never be reached in same clock???
    when(io.inVec(0) === '.'.U){
        ledReg := 1.U
        counterReg := dotTime
        when(counterReg === 0.U){
            ledReg := 0.U
        }
    }.otherwise{
        ledReg := 0.U
    }

    // switch(stateReg) {
    //     is(0.U) {
    //         when(io.inVec(posCounter) === '.'.U) {
    //             ledReg := 1.U
    //             counterReg := dotTime
    //             stateReg := 1.U
    //         } .elsewhen(io.inVec(posCounter) === '-'.U) {
    //             ledReg := 1.U
    //             counterReg := dashTime
    //             stateReg := 1.U
    //         }

    //         counterReg := counterReg - 1.U

    //         when(counterReg === 0.U){
    //             stateReg := 1.U
    //         }
    //     } 
    //     is(1.U) {  
    //         ledReg := 0.U
            
    //         counterReg := pauseTime

    //         counterReg := counterReg - 1.U

    //         when(counterReg === 0.U){
    //             when(posCounter === 3.U) {
    //                 //go to stop state
    //                 stateReg := 2.U
    //             } .otherwise {
    //                 posCounter := posCounter + 1.U
    //                 stateReg := 0.U
    //             }
    //         }
    //     }
    //     is(2.U) {
    //         //have to make as UInt since we cant compare vectors
    //         when(io.inVec.asUInt =/= lastInVec) {
    //             posCounter := 0.U
    //             stateReg := 0.U // Move to state 0 if a new inVec appears
    //         }
    //     }    
    // }
    io.led := ledReg
}


