package BooleanCircuits

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class OrTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "Or Module"

    it should s"Test Or Logic for 1 case" in {
        test(new Or()) { dut =>
            dut.io.a.poke(0.U)
            dut.io.b.poke(0.U)

            // println(dut.io.z.peek.litValue())
            dut.io.z.expect(0.U)
        }
    }

    it should s"Test Or Logic for all cases" in {
        test(new Or()) { dut =>
            println("a | b | z")
            println("---------")
            for (in1 <- 0 until 2) { 
                for (in2 <- 0 until 2) {

                    dut.io.a.poke(in1.U)
                    dut.io.b.poke(in2.U)
                    dut.clock.step()

                    var z = dut.io.z.peek.litValue
                    println(s"$in1 | $in2 | $z") //fancy print formatting
                }
            }
            println("All tests complete")
        }
    }
    
}

