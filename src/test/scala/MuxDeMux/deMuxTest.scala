/*
* deMuxTest.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
*/

package MuxDeMux

import chisel3._
import chisel3.experimental.BundleLiterals._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import scala.util.Random


class deMuxTest extends AnyFlatSpec with ChiselScalatestTester {

    // "deMux2to1" should s"work" in {
    //     test(new deMux(10)){ dut => 
    //         dut.io.sel.poke(true.B)
    //         dut.io.in.poke(765.U)
    //                               //output when sel =
    //         dut.io.d1.expect(0.U)    // false/0
    //         dut.io.d2.expect(765.U)  // true/1
    //     }
    // }

    def randomTest(width: Int) = {
        val in  = BigInt(width, Random)
        val inAsUint = in.U(width.W)
        //val in2 = BigInt(width, Random).U(width.W)
        val select = Random.nextInt(2)

        var d1exp = 0.U
        var d2exp = 0.U
        if(select == 0){
            d1exp = inAsUint
            d2exp = 0.U
        }
        else if(select == 1){
            d1exp = 0.U
            d2exp = inAsUint
        }

        //singular test thats generated in for loop below
        it should s"deMux1:2: width = $width, sel = $select, in = $in" in {
            test(new deMux(width)) { dut =>
            dut.io.sel.poke(select)
            
            dut.io.in.poke(inAsUint)

            dut.io.d1.expect(d1exp)
            dut.io.d2.expect(d2exp)
            
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

    // "deMux1to4" should s"work" in {
    //     test(new deMux1to4(10)){ dut => 
    //         dut.io.s0.poke(0.U)
    //         dut.io.s1.poke(1.U)
    //         dut.io.in.poke(765.U)
    //                                 //  s1 , s0
    //         dut.io.d1.expect(0.U)    // 0  ,  0
    //         dut.io.d2.expect(0.U)  // 0  ,  1
    //         dut.io.d3.expect(765.U)  // 1  ,  0
    //         dut.io.d4.expect(0.U)  // 1  ,  1
    //     }
    // }

    def randomTest4to1(width: Int) = {
        val in  = BigInt(width, Random)
        val inAsUint = in.U(width.W)
        val s0 = Random.nextInt(2)
        val s1 = Random.nextInt(2)

        var d1exp = 0.U
        var d2exp = 0.U
        var d3exp = 0.U
        var d4exp = 0.U
        if(s0 == 0 & s1 == 0){
            d1exp = inAsUint
        }
        else if(s0 == 1 & s1 == 0){
            d2exp = inAsUint
        }
        else if(s0 == 0 & s1 == 1){
            d3exp = inAsUint
        }
        else if(s0 == 1 & s1 == 1){
            d4exp = inAsUint
        }

        //singular test thats generated in for loop below
        it should s"deMux1:4: width = $width, s0 = $s0, s1 = $s1, in = $in" in {
            test(new deMux1to4(width)) { dut =>
            dut.io.s0.poke(s0)
            dut.io.s1.poke(s1)
            
            dut.io.in.poke(inAsUint)

            dut.io.d1.expect(d1exp)
            dut.io.d2.expect(d2exp)
            dut.io.d3.expect(d3exp)
            dut.io.d4.expect(d4exp)
            
            }
        }
    }

    //generate 6 tests for all (6) widths 
    for (width <- randomWidths) {
        randomTest4to1(width)
    }
}