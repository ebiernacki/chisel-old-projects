package SevenSegDisplay

import chisel3._
import chisel3.util._
/*
*
* Top level display module for setting any LED segment on any character position
* 
*/

class topLevelDisplay extends Module{
    val io = IO(new Bundle{
        //to add: the list input??
        //        how to use this as display module in other modules....
        val char0_seg       = Input(UInt(7.W))
        val char1_seg       = Input(UInt(7.W))
        val char2_seg       = Input(UInt(7.W))
        val char3_seg       = Input(UInt(7.W))

        val refreshRate     = Input(UInt(32.W))
        //Board pins input and output
        val board_segments  = Output(UInt(7.W))
        val board_anode     = Output(UInt(4.W))
    })

    /*
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
    */
    
    //use function to set values of the 4 characters segments using the setIO function 
    

    //take those 7 bit values and connect them to the refreshHandler (which will attach them with an anode(character position on the display))
    val refresher = Module(new refreshHandler)
    refresher.io.rate := io.refreshRate//400000.U 
    //cycle the "hot" (on) value between these 4
    refresher.io.char0 := io.char0_seg
    refresher.io.char1 := io.char1_seg
    refresher.io.char2 := io.char2_seg
    refresher.io.char3 := io.char3_seg

    //output of refresher is the hot value(changing every 4ms (or switch speed))
    io.board_segments  := refresher.io.hot_char_seg
    io.board_anode     := refresher.io.hot_char_an
}

// object topLevelDisplayMain extends App {
//     (new chisel3.stage.ChiselStage).emitVerilog(new topLevelDisplay, Array("--target-dir", "generated"))
// }