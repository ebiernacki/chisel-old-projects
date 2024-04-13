/*
* MuxTest.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
*/


package MuxDeMux

import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import scala.util.Random


class MuxTest extends AnyFlatSpec with ChiselScalatestTester { 

	//   "Mux2to1" should s"work for multibit" in {
	//     test(new Mux(10)){ dut => 
	//         dut.io.sel.poke(1.U)//true, d2
	//         println(dut.io.sel.peek())
	//         dut.io.d1.poke(666.U)
	//         dut.io.d2.poke(999.U)

	//         dut.io.out.expect(999.U)
	//     }
	//  }

	def randomTest(width: Int) = {
		val in1 = BigInt(width, Random) //not oneline for printing formats
		val in1asUint = in1.U(width.W)  //to UInt for chisel
		val in2 = BigInt(width, Random)
		val in2asUint = in2.U(width.W)
		val select = Random.nextInt(2)

		val expected = if(select == 1) in2asUint else in1asUint //replaceable: If c, then x; else y 

		//singular test thats generated in for loop below
		it should s"Mux2:1: width = $width, sel = $select, in1 = $in1, in2 = $in2, expected = $expected" in {
			test(new Mux(width)) { dut =>
				dut.io.sel.poke(select)
				dut.io.d1.poke(in1asUint)
				dut.io.d2.poke(in2asUint)
				
				dut.io.out.expect(expected)
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

	// "Mux4to1" should s"work for multibit" in {
	//   test(new Mux4to1(10)){ dut => 
	//       dut.io.s0.poke(0.U)
	//       dut.io.s1.poke(0.U)  //s1,  s0
	//       dut.io.d1.poke(765.U) //0 ,  0
	//       dut.io.d2.poke(398.U) //0 ,  1
	//       dut.io.d3.poke(512.U) //1 ,  0
	//       dut.io.d4.poke(934.U) //1 ,  1

	//       dut.io.out.expect(765.U)
	//   }
	// }

	def randomTest4to1(width: Int) = {
		val in1 = BigInt(width, Random)
		val in2 = BigInt(width, Random)
		val in3 = BigInt(width, Random)
		val in4 = BigInt(width, Random)
		val in1asUint = in1.U(width.W)
		val in2asUint = in2.U(width.W)
		val in3asUint = in3.U(width.W)
		val in4asUint = in4.U(width.W)
		val s0 = Random.nextInt(2)
		val s1 = Random.nextInt(2)


		var expected = in1asUint
		
		if (s0 == 0 & s1 == 0){
			expected = in1asUint
		}
		else if (s0 == 1 & s1 == 0){
			expected = in2asUint
		}
		else if (s0 == 0 & s1 == 1){
			expected = in3asUint
		}
		else if (s0 == 1 & s1 == 1){
			expected = in4asUint
		}

		//singular test thats generated in for loop below
		it should s"Mux4:1: width = $width, s0 = $s0, s1 = $s1, in1 = $in1, in2 = $in2, in3 = $in3, in4 = $in4, expected = $expected" in {
			test(new Mux4to1(width)) { dut =>
				dut.io.s0.poke(s0)
				dut.io.s1.poke(s1)
				dut.io.d1.poke(in1asUint)
				dut.io.d2.poke(in2asUint)
				dut.io.d3.poke(in3asUint)
				dut.io.d4.poke(in4asUint)
				
				dut.io.out.expect(expected)
			}
		}
	}

	//generate 6 tests for all (6) widths 
	for (width <- randomWidths) {
		randomTest4to1(width)
	}
	

	// println("Done!")
}


