// package LED

// import chisel3._
// import chisel3.util._
////////////////////////////////////////////////////////////////////
//
//              DEPRECATED WITH USE OF setIO FUNCTION
//
////////////////////////////////////////////////////////////////////
/*
*
* Module that takes in a tuple with the segment specifics
* outputs the value for that character to be connected to board pin values 
* Remembers content of a single character, can be updated
*  
* tuple: List[(String, UInt)]
*/

// class segments extends Module{
//     val io = IO(new Bundle{
//         val in = Input(UInt(7.W))
//         val character_Value = Output(UInt(7.W))
//     })

//     io.character_Value := io.in 
    
// }