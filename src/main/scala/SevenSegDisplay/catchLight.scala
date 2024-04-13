package SevenSegDisplay

import chisel3._
import chisel3.util._
import Helper._


class catchLight extends Module {
    val io = IO(new Bundle {
        val board_sw        = Input(UInt(16.W))
        val board_led       = Output(UInt(16.W))
        val board_button    = Input(Bool()) 
        val board_segments  = Output(UInt(7.W))
        val board_anode     = Output(UInt(4.W))
    })

    // Register to hold the score value
    val scoreReg = RegInit(0.U(14.W)) //14bits for max score of 9999 
    
    //the following code sets up the FSM to handle the game state

    //left and right states to drive the LED
    val goLeft :: goRight :: Nil = Enum(2)
    val stateReg = RegInit(goLeft)

    //Register to hold the bit that is shifted (the led that is on) left and right
    val ledReg = RegInit(1.U(16.W))

    //counter to hold the LED until it is time to shift and go left or right  
    val shiftCounter = RegInit(0.U(32.W))
    shiftCounter := shiftCounter + 1.U

    //the following code is for selecting the speed of the game
    //initialize module to control speed of the game based on center button press
    val buttSpeed = Module(new buttonSpeed)
    buttSpeed.io.button := io.board_button
    val gameSpeed = buttSpeed.io.gameSpeed
    
    //reset speed just in case speed change breaks (incrementing to a count that will never happen)
    when(gameSpeed =/= RegNext(gameSpeed)) {
        shiftCounter := 0.U
    }

    // Update FSM state and registers only with changing game speed
    when(shiftCounter === gameSpeed) { //for testing: 10 for running: gamespeed
        shiftCounter := 0.U

        //change direction logic (one state before edge to prevent accidental overflow)
        when(ledReg(14) === 1.U) {
            stateReg := goRight
        }.elsewhen(ledReg(1) === 1.U) {
            stateReg := goLeft
        }
        
        //shift led left or right
        when(stateReg === goLeft) {
            ledReg := ledReg << 1
        }.otherwise {
            ledReg := ledReg >> 1
        }

        // If switch matches the lit LED, stop send the led back to the 0 spot(right)
        val win = io.board_sw === ledReg && !reset.asBool

        when(win){
            //inc game score
            scoreReg := scoreReg + 1.U
            ledReg := 1.U
            stateReg := goLeft //for edge case when led is going right, then ledReg := 1.U, but state is still going right(off the board)
        }
    }

    //the following code handles displaying the score value on 7seg display

    //use the score module to get bcd and character display values 
    val score = Module(new score)
    score.io.scoreValue := scoreReg

    //actually connects the character values to the characters and handles the refresh rate for "animation"
    val display = Module(new topLevelDisplay)
    display.io.refreshRate := 400000.U //4ms 
    display.io.char0_seg := score.io.char0_seg
    display.io.char1_seg := score.io.char1_seg
    display.io.char2_seg := score.io.char2_seg
    display.io.char3_seg := score.io.char3_seg

    io.board_anode := display.io.board_anode
    io.board_segments := display.io.board_segments

    //finally connect LEDs so the value is shown on board
    io.board_led := ledReg
    
}

// object catchLightMain extends App {
//     (new chisel3.stage.ChiselStage).emitVerilog(new catchLight, Array("--target-dir", "generated"))
// }