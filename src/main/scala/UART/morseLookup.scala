/*
* morseLookup.scala
* Author: Eric Biernacki(ebiernacki@wustl.edu)
* Date: 1/21/24
* 
* Content:
* Lookup map for all morse code values
* Values can also be decimal ascii values
* Output a vector of the 4 values
* 4 since it is the max string length for a morse value
* If less than 4 send 0.U (ascii null)
* If the char is unrecognized (no matching case): send '****'
* 
* morse code values: https://www.electronics-notes.com/articles/ham_radio/morse_code/characters-table-chart.php
* morse code translator:  https://morsecode.world/international/translator.html   
*/


package UART

import chisel3._
import chisel3.util._

class morseLookup extends Module {
    val io = IO(new Bundle{
        val in = Input(UInt(8.W))
        val out = Output(Vec(4, UInt(8.W)))
    })

    io.out := MuxCase(VecInit(Seq.fill(4)('*'.U)), Array(
        (io.in === 'a'.U) ->  VecInit(Seq(  0.U,   0.U, '.'.U, '-'.U)),  // 'a' => ".-"
        (io.in === 'b'.U) ->  VecInit(Seq('-'.U, '.'.U, '.'.U, '.'.U)),   // 'b' => "-..."
        (io.in === 'c'.U) ->  VecInit(Seq('-'.U, '.'.U, '-'.U, '.'.U)),   // 'c' => "-.-."
        (io.in === 'd'.U) ->  VecInit(Seq(  0.U, '-'.U, '.'.U, '.'.U)),   // 'd' => "-.."
        (io.in === 'e'.U) ->  VecInit(Seq(  0.U,   0.U,   0.U, '.'.U)),   // 'e' => "."
        (io.in === 'f'.U) ->  VecInit(Seq('.'.U, '.'.U, '-'.U, '.'.U)),   // 'f' => "..-."
        (io.in === 'g'.U) ->  VecInit(Seq(  0.U, '-'.U, '-'.U, '.'.U)),   // 'g' => "--."
        (io.in === 'h'.U) ->  VecInit(Seq('.'.U, '.'.U, '.'.U, '.'.U)),   // 'h' => "...."
        (io.in === 'i'.U) ->  VecInit(Seq(  0.U,   0.U, '.'.U, '.'.U)),   // 'i' => ".."
        (io.in === 'j'.U) ->  VecInit(Seq('.'.U, '-'.U, '-'.U, '-'.U)),   // 'j' => ".---"
        (io.in === 'k'.U) ->  VecInit(Seq(  0.U, '-'.U, '.'.U, '-'.U)),   // 'k' => "-.-"
        (io.in === 'l'.U) ->  VecInit(Seq('.'.U, '-'.U, '.'.U, '.'.U)),   // 'l' => ".-.."
        (io.in === 'm'.U) ->  VecInit(Seq(  0.U,   0.U, '-'.U, '-'.U)),   // 'm' => "--"
        (io.in === 'n'.U) ->  VecInit(Seq(  0.U,   0.U, '-'.U, '.'.U)),   // 'n' => "-."
        (io.in === 'o'.U) ->  VecInit(Seq(  0.U, '-'.U, '-'.U, '-'.U)),   // 'o' => "---"
        (io.in === 'p'.U) ->  VecInit(Seq('.'.U, '-'.U, '-'.U, '.'.U)),   // 'p' => ".--."
        (io.in === 'q'.U) ->  VecInit(Seq('-'.U, '-'.U, '.'.U, '-'.U)),   // 'q' => "--.-"
        (io.in === 'r'.U) ->  VecInit(Seq(  0.U, '.'.U, '-'.U, '.'.U)),   // 'r' => ".-."
        (io.in === 's'.U) ->  VecInit(Seq(  0.U, '.'.U, '.'.U, '.'.U)),   // 's' => "..."
        (io.in === 't'.U) ->  VecInit(Seq(  0.U,   0.U,   0.U, '-'.U)),   // 't' => "-"
        (io.in === 'u'.U) ->  VecInit(Seq(  0.U, '.'.U, '.'.U, '-'.U)),   // 'u' => "..-"
        (io.in === 'v'.U) ->  VecInit(Seq('.'.U, '.'.U, '.'.U, '-'.U)),   // 'v' => "...-"
        (io.in === 'w'.U) ->  VecInit(Seq(  0.U, '.'.U, '-'.U, '-'.U)),   // 'w' => ".--"
        (io.in === 'x'.U) ->  VecInit(Seq('-'.U, '.'.U, '.'.U, '-'.U)),   // 'x' => "-..-"
        (io.in === 'y'.U) ->  VecInit(Seq('-'.U, '.'.U, '-'.U, '-'.U)),   // 'y' => "-.--"
        (io.in === 'z'.U) ->  VecInit(Seq('-'.U, '-'.U, '.'.U, '.'.U)),   // 'z' => "--.."
        (io.in === ' '.U) ->  VecInit(Seq(  0.U,   0.U,   0.U,  32.U))   // ' ' => " "
    ))
}

