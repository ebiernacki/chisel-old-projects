/*
* AdderTest.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
*/

package AdderCircuits

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import scala.util.Random


class adderTest extends AnyFlatSpec with ChiselScalatestTester {
    "half adder" should s"add two 1bit inputs" in {
        test(new HalfAdder()){ dut => 
            dut.io.a.poke(1.U)
            dut.io.b.poke(1.U)
            dut.io.carry.expect(1.U)
            dut.io.sum.expect(0.U)
        }
    } 
    "full adder" should s"add two 1bit inputs" in {
        test(new FullAdder()){ dut => 
            dut.io.a.poke(1.U)
            dut.io.b.poke(0.U)
            dut.io.c_in.poke(1.U)
            dut.io.sum.expect(0.U)
            dut.io.c_out.expect(1.U)

        }
    } 
    "ripple carry adder" should s"add two 5bit inputs" in {
        test(new Adder(5)){ dut => 
            dut.io.a.poke(14.U)
            dut.io.b.poke(29.U)
            dut.io.sum.expect(11.U) //10 bit output
        }
    } 

    def randomTest(width: Int) = {
        val in1 = BigInt(width, Random) //not oneline for printing formats
        val in1asUint = in1.U(width.W)  //to UInt for chisel
        val in2 = BigInt(width, Random)
        val in2asUint = in2.U(width.W)

        var expected = (in1 + in2).U //replaceable
        var trunc: Boolean = false


        //check if width is too much
        if(expected.getWidth > width){
            trunc = true
            expected = expected(width-1 , 0) //chop off MSB if value has grown over width
        }

        //singular test thats generated in for loop below
        it should s"RippleCarry Add: width = $width, in1 = $in1, in2 = $in2, expected = $expected, truncated: $trunc" in {
            test(new Adder(width)) { dut =>
                dut.io.a.poke(in1asUint)
                dut.io.b.poke(in2asUint)
                dut.io.sum.expect(expected)
            }
        }
    }

    //generate seq of random widths in range  1 -> 64, add 64 to end
    var randomWidths: Seq[Int] = Seq.fill(5)(Random.nextInt(64) + 1)
    randomWidths = randomWidths :+ 64

    //generate 6 tests for all (6) widths 
    for (width <- randomWidths) {
        randomTest(width)
    }
}
