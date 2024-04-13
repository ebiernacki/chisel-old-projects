package SevenSegDisplay

import chisel3._
import chisel3.util._

class swSpeed extends Module {
    val io = IO(new Bundle {
        val board_sw              = Input(UInt(4.W))
        //val refreshRate = Output(UInt(32.W)) // Assumes 4 switches, so 2 bits are sufficient
        val board_segments  = Output(UInt(7.W))
        val board_anode     = Output(UInt(4.W))
    })
    val refreshRate = RegInit(0.U)

    when(io.board_sw === Cat(1.U, 1.U, 1.U, 1.U)){
        refreshRate := 10000000.U //100ms
    }
    //for testing
    /*
    .elsewhen(io.sw === Cat(0.U, 0.U, 0.U, 1.U)){
        refreshRate := 10.U
    }
    .elsewhen(io.sw === Cat(0.U, 0.U, 1.U, 1.U)){
        refreshRate := 5.U
    }
    */
    .elsewhen(io.board_sw === Cat(1.U, 1.U, 1.U, 0.U)){
        refreshRate := 5000000.U //50ms
    }
    .elsewhen(io.board_sw === Cat(1.U, 1.U, 0.U, 0.U)){
        refreshRate := 2500000.U //25ms
    }  
    .elsewhen(io.board_sw === Cat(1.U, 0.U, 0.U, 0.U)){
        refreshRate := 1000000.U //10ms
    }
    //any other sw selection: stock 4ms
    .otherwise{
        refreshRate := 400000.U //4ms
    }

    //function to set the segments based on the tuple input:  List[tuple("stringName", value),....]
    def setIO(tuple: List[(String, UInt)]): UInt = {
        //map and extract set values
        val tupleMap  = tuple.toMap
        //also this lets you set only the ones you want(others default to 0.U)
        val midWest   = tupleMap.getOrElse("midWest", 0.U)
        val northWest = tupleMap.getOrElse("northWest", 0.U)
        val southWest = tupleMap.getOrElse("southWest", 0.U)
        val south     = tupleMap.getOrElse("south", 0.U)
        val southEast = tupleMap.getOrElse("southEast", 0.U)
        val northEast = tupleMap.getOrElse("northEast", 0.U)
        val north     = tupleMap.getOrElse("north", 0.U)
        
        //return the values concatenated together in the right order
        Cat(midWest, northWest, southWest, south, southEast, northEast, north)
    }
    //to set actual numbers, we can preset the values for them
    val one   = List(("southEast", 1.U),("northEast", 1.U))
    val two   = List(("midWest", 1.U),("southWest", 1.U),("south", 1.U),("northEast", 1.U), ("north", 1.U))
    val three = List(("midWest", 1.U),("south", 1.U),("southEast", 1.U),("northEast", 1.U), ("north", 1.U))
    val four  = List(("midWest", 1.U),("northWest", 1.U),("southEast", 1.U),("northEast", 1.U))

    val display = Module(new topLevelDisplay)
    display.io.refreshRate := refreshRate
    display.io.char0_seg := setIO(four)
    display.io.char1_seg := setIO(three)
    display.io.char2_seg := setIO(two)
    display.io.char3_seg := setIO(one)

    io.board_anode := display.io.board_anode
    io.board_segments := display.io.board_segments

}

// object swSpeedMain extends App {
//     (new chisel3.stage.ChiselStage).emitVerilog(new swSpeed, Array("--target-dir", "generated"))
// }