package AdderCircuits


import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import scala.util.Random


class subsetTest extends AnyFlatSpec with ChiselScalatestTester {
    "half adder" should s"add two 1bit inputs" in {
        test(new bitSubset()){ dut => 
            
            dut.io.output.expect(15.U)
            
        }
    } 
}