package SevenSegDisplay

import chisel3._
import chisel3.util._

class buttonSpeed extends Module {
    val io = IO(new Bundle {
        val button          = Input(Bool()) 
        val gameSpeed       = Output(UInt(27.W)) 
    })

    val speedReg = Counter(3)
    val gameSpeed =  RegInit(75000000.U(27.W))

    // Detect button press (rising edge)
    val buttonPressed = io.button && !RegNext(io.button)

    // Increment the count on button press
    when(buttonPressed) { 
        speedReg.inc()
    }
    //base speed
    when(speedReg.value === 0.U){
        gameSpeed := 50000000.U
    }
    //low speed
    when(speedReg.value === 1.U){
        gameSpeed := 25000000.U
    }
    //medium speed
    when(speedReg.value === 2.U){
        gameSpeed := 12500000.U
    }
    //fastest speed
    when(speedReg.value === 3.U){
        gameSpeed := 5000000.U
    }

    io.gameSpeed := gameSpeed
}