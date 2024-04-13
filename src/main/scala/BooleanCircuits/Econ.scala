package BooleanCircuits

import chisel3._

class Econ extends Module {
    val io = IO(new Bundle{
        val input1 = Input(UInt(1.W))
        val input2 = Input(UInt(1.W))
        val input3 = Input(UInt(1.W))

        val output = Output(UInt(1.W))
    })
    
    val andModule = Module(new AndMain.And())

    andModule.io.a := io.input1
    andModule.io.b := io.input2

    val orModule = new Or()
    orModule.io.a := andModule.io.z
    orModule.io.b := io.input3


    io.output := orModule.io.z

}
