package com.briarshore.aoc2022.day05

import println
import readInput

//     [D]
// [N] [C]
// [Z] [M] [P]
//  1   2   3
//
// move 1 from 2 to 1
// move 3 from 1 to 3
// move 2 from 2 to 1
// move 1 from 1 to 2
//
enum class CraneModel {
    M9000, M9001
}

fun main() {

    data class Move(val count: Int, val from: Int, val to: Int, val model: CraneModel) {
        fun apply(stacks: MutableList<ArrayDeque<Char>>) {
            when {
                model == CraneModel.M9000 -> {
                    repeat(count) { _ -> stacks[to - 1].addFirst(stacks[from - 1].removeFirst()) }
                }
                model == CraneModel.M9001 -> {
                    val crane = ArrayDeque<Char>()
                    repeat(count) { _ -> crane.addLast(stacks[from - 1].removeFirst()) }
                    repeat(count) { _ -> stacks[to - 1].addFirst(crane.removeLast()) }
                }
            }
        }
    }

    val instructionRegex = Regex("move (?<count>\\d+) from (?<from>\\d+) to (?<to>\\d+)")

    fun sharedPart(input: List<String>, craneModel: CraneModel): String {
        var stacks: MutableList<ArrayDeque<Char>> = mutableListOf()
        input.asSequence().map {
            if (it.contains('[') || it.contains(']')) {
                for (i in 1..it.length step 4) {
                    val crate = it[i]
                    if (crate != ' ') {
                        val stacksIndex = i / 4
                        if (stacks.isEmpty()) {
                            stacks = MutableList(it.length / 4 + 1) { _ -> ArrayDeque() }
                        }
                        stacks[stacksIndex].addLast(crate)
                    }
                }
                ""
            } else if (it.contains(" 1   2   3 ")) {
                ""
            } else if (it.isNotBlank()) {
                it
            } else {
                ""
            }
        }
            .filter { it.isNotBlank() }
            .map { instructionRegex.matchEntire(it) }
            .filterNotNull()
            .map { it.groups }
            .map { Move(it["count"]!!.value.toInt(), it["from"]!!.value.toInt(), it["to"]!!.value.toInt(), craneModel) }
            .toList()
            .forEach { it.apply(stacks) }
        return stacks.map { it.first() }.joinToString("")
    }

    fun part1(input: List<String>): String {
        return sharedPart(input, CraneModel.M9000)
    }

    fun part2(input: List<String>): String {
        return sharedPart(input, CraneModel.M9001)
    }

    val sampleInput = readInput("d5p1-sample")
    check(part1(sampleInput) == "CMZ")

    val input = readInput("d5p1-input")
    val part1 = part1(input)
    check(part1 == "VWLCWGSDQ")
    "part1 $part1".println()

    check(part2(sampleInput) == "MCD")
    val part2 = part2(input)
    "part2 $part2".println()
    check(part2 == "TCGLQSLPW")
}
