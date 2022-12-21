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
fun main() {
    data class Move(val count: Int, val from: Int, val to: Int) {
        fun apply(stacks: MutableList<ArrayDeque<Char>>) {
            repeat(count) { _ -> perform(stacks, from, to) }
        }

        private fun perform(stacks: MutableList<ArrayDeque<Char>>, from: Int, to: Int) {
            stacks[to - 1].addFirst(stacks[from - 1].removeFirst())
        }
    }

    fun crane9000Process(stacks: MutableList<ArrayDeque<Char>>, move: Move) {
        move.apply(stacks)
    }

    val instructionRegex = Regex("move (?<count>\\d+) from (?<from>\\d+) to (?<to>\\d+)")

    fun part1(input: List<String>): String {
        var stacks: MutableList<ArrayDeque<Char>>? = null
        input.asSequence().map {
            if (it.contains('[') || it.contains(']')) {
                for (i in 1..it.length step 4) {
                    val crate = it[i]
                    if (crate != ' ') {
                        val stacksIndex = i / 4
                        if (stacks == null) {
                            stacks = MutableList(it.length / 4 + 1) { _ -> ArrayDeque() }
                        }
                        stacks!![stacksIndex].addLast(crate)
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
            .map { Move(it["count"]!!.value.toInt(), it["from"]!!.value.toInt(), it["to"]!!.value.toInt()) }
            .toList()
            .forEach { crane9000Process(stacks!!, it) }
        return stacks!!.map { it.first() }.joinToString("")
    }

    fun part2(input: List<String>): String {
        TODO()
    }

    val sampleInput = readInput("d5p1-sample")
    check(part1(sampleInput) == "CMZ")

    val input = readInput("d5p1-input")
    val part1 = part1(input)
    "part1 $part1".println()

//    check(part2(sampleInput) = "MCD")
}
