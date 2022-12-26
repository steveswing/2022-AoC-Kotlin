package com.briarshore.aoc2022.day10

import com.briarshore.aoc2022.day10.Instruction.*
import println
import readInput
import java.util.Optional

enum class Instruction(val cycles: Int) {
    NOOP(1), ADDX(2)
}

data class Operation(val instruction: Instruction, val argument: Optional<Int>)

fun String.toInstructions(): Operation {
    return if (contains(" ")) {
        val (instr: String, len: String) = split(" ")
        Operation(valueOf(instr.uppercase()), Optional.ofNullable(len.toInt()))
    } else {
        Operation(valueOf(this.uppercase()), Optional.empty())
    }
}
// 15-11+6-3+5-1-8+13+4-1+5-1+5-1+5-1+5-1-35
// .........##........##...................
// .....................###................
// ..........................#.............
// .......................#................
// .....................###......##........
// .....................#..#...............
//
//
//
//
//
//
//


fun main() {
    var x = 1
    var currentCycle = 0
    var signalStrength = 0
    val signalHistory = mutableListOf<Int>()
    val crtBuffer = mutableListOf<String>()
    var crt = 0

    fun tick(operation: Operation) {
        val (instruction, v) = operation
        (0 until instruction.cycles).map {
            currentCycle++
            if (currentCycle % 40 == 20) {
                signalStrength = x * currentCycle
                signalHistory += signalStrength
            }
        }

        v.ifPresent { x += it }
    }

    fun render(operation: Operation) {
        val (instruction, v) = operation
        (0 until instruction.cycles).map {
            val sprite = x - 1..x + 1
            if (crt > 0 && crt % 40 == 0) {
                println()
                crtBuffer += ""
            }
            val c = if (crt % 40 in sprite) '#' else '.'
            print(c)
            crtBuffer[crtBuffer.lastIndex] = crtBuffer[crtBuffer.lastIndex] + c
            crt++
        }
        v.ifPresent { x += it }
    }

    fun part1(lines: List<String>): Int {
        x = 1
        currentCycle = 0
        signalStrength = 0
        signalHistory.clear()
        lines.map { it.toInstructions() }.forEach { tick(it) }

        return x
    }

    fun part2(lines: List<String>) {
        x = 1
        currentCycle = 0
        crtBuffer.clear()
        crtBuffer += ""
        crt = 0
        println()
        lines.map { it.toInstructions() }.forEach { render(it) }

        println()
        println()
//        crtBuffer.forEach { it.println() }
        //  ##..##..##..##..##..##..##..##..##..##..
        //  ###...###...###...###...###...###...###.
        //  ####....####....####....####....####....
        //  #####.....#####.....#####.....#####.....
        //  ######......######......######......####
        //  #######.......#######.......#######.....
    }

    val smallSampleInput = listOf(
        "noop",
        "addx 3",
        "addx -5",
    )

    val part1SmallSample = part1(smallSampleInput)
    "part1 $part1SmallSample".println()
    check(part1SmallSample == -1)

    val sampleInput = readInput("d10p1-sample")
    val samplePart1 = part1(sampleInput)
    "part1 $samplePart1".println()
    check(samplePart1 == 17)
    val signalSampleHistoryTotal = signalHistory.sum()
    "small sample signalHistoryTotal $signalSampleHistoryTotal".println()
    check(signalSampleHistoryTotal == 13140)

    val input = readInput("d10-input")
    val part1 = part1(input)
    "part1 $part1".println()
    val signalHistoryTotal = signalHistory.sum()
    "signalHistoryTotal $signalHistoryTotal".println()
    check(signalHistoryTotal == 13480)

    part2(input) // EGJBGCFK

    val expected = """
        ####..##....##.###...##...##..####.#..#.
        #....#..#....#.#..#.#..#.#..#.#....#.#..
        ###..#.......#.###..#....#....###..##...
        #....#.##....#.#..#.#.##.#....#....#.#..
        #....#..#.#..#.#..#.#..#.#..#.#....#.#..
        ####..###..##..###...###..##..#....#..#.
    """.trimIndent().lines()
    check(crtBuffer == expected)
}
