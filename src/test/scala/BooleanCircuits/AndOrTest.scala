/*
* AndOrTest.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
*/

package BooleanCircuits

import chisel3._
import chisel3.experimental.BundleLiterals._
import chiseltest._
import scala.util.Random
import org.scalatest.flatspec.AnyFlatSpec
import scala.math._

class AndOrTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "AndOr Module"

	def randomTest(width: Int) = {
		//pick random value with range of 0 to 2^width - 1
		val a = BigInt(width, Random)
		val b = BigInt(width, Random) 
		val c = BigInt(width, Random)
		val expected = (a & b) | c  //replaceable

		//singular test thats generated in for loop below
		it should s"calculate results for: width = $width, a = $a, b = $b, c = $c, expected = $expected" in {
			test(new AndOr(width)) { dut =>
				dut.io.a.poke(a.U(width.W))
				dut.io.b.poke(b.U(width.W))
				dut.io.c.poke(c.U(width.W))

				dut.io.q.expect(expected.U)
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

  println("Done!")
}



