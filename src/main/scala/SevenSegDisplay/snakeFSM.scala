package SevenSegDisplay

import chisel3._
import chisel3.util._

class snakeTop extends Module {
    val io = IO(new Bundle{
        val board_segments = Output(UInt(7.W))
        val board_anode    = Output(UInt(4.W))
    })

    val snakeFSM = Module(new snakeFSM)

    io.board_segments := snakeFSM.io.hot_char(10, 4)
    io.board_anode    := snakeFSM.io.hot_char(3, 0)

}

class snakeFSM extends Module {
    val io = IO(new Bundle{
        val hot_char   = Output(UInt(11.W))
    })

    val anodeSet = Module(new anodes)

    val s0 :: s1 :: s2 :: s3 :: s4 :: s5 :: s6 :: s7 :: s8 :: s9 :: s10 :: s11 :: Nil = Enum(12) 

    //Set initial state reg
    val stateReg = RegInit(s0)

    val stateCounter = RegInit(0.U(27.W))   //27 bits for 100000000
    stateCounter := stateCounter + 1.U 

    val hot_char = RegInit(0.U)

    when(stateCounter === 50000000.U){
        stateCounter := 0.U

        switch(stateReg){
            is(s0){
                hot_char := ~Cat(0.U,0.U,0.U,0.U,0.U,0.U,1.U,anodeSet.io.an3)
                stateReg := s1 
            }
            is(s1){
                hot_char :=  ~Cat(0.U,0.U,0.U,0.U,0.U,0.U,1.U,anodeSet.io.an2)
                stateReg := s2 
            }
            is(s2){
                hot_char :=  ~Cat(0.U,0.U,0.U,0.U,0.U,0.U,1.U,anodeSet.io.an1)
                stateReg := s3 
            }
            is(s3){
                hot_char :=  ~Cat(0.U,0.U,0.U,0.U,0.U,0.U,1.U,anodeSet.io.an0)
                stateReg := s4 
            }
            is(s4){
                hot_char :=  ~Cat(0.U,0.U,0.U,0.U,0.U,1.U,0.U,anodeSet.io.an0)
                stateReg := s5 
            }
            is(s5){
                hot_char :=  ~Cat(0.U,0.U,0.U,0.U,1.U,0.U,0.U,anodeSet.io.an0)
                stateReg := s6
            }
            is(s6){
                hot_char :=  ~Cat(0.U,0.U,0.U,1.U,0.U,0.U,0.U,anodeSet.io.an0)
                stateReg := s7 
            }
            is(s7){
                hot_char :=  ~Cat(0.U,0.U,0.U,1.U,0.U,0.U,0.U,anodeSet.io.an1)
                stateReg := s8 
            }
            is(s8){
                hot_char :=  ~Cat(0.U,0.U,0.U,1.U,0.U,0.U,0.U,anodeSet.io.an2)
                stateReg := s9 
            }
            is(s9){
                hot_char :=  ~Cat(0.U,0.U,0.U,1.U,0.U,0.U,0.U,anodeSet.io.an3)
                stateReg := s10 
            }
            is(s10){
                hot_char :=  ~Cat(0.U,0.U,1.U,0.U,0.U,0.U,0.U,anodeSet.io.an3)
                stateReg := s11 
            }
            is(s11){
                hot_char :=  ~Cat(0.U,1.U,0.U,0.U,0.U,0.U,0.U,anodeSet.io.an3)
                stateReg := s0 
            }
        }
    }

    io.hot_char := hot_char
}

// object snakeMain extends App {
//     (new chisel3.stage.ChiselStage).emitVerilog(new snakeTop, Array("--target-dir", "generated"))
// }