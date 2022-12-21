package com.briarshore.aoc2022.day04

import println
import readInput

fun main() {
    fun asRange(rangeParts: List<String>): IntRange {
        return IntRange(rangeParts.first().toInt(), rangeParts.last().toInt())
    }

    fun prepareInput(input: List<String>) = input
        .map { it.split(",") }
        .map { Pair(asRange(it.first().split("-")), asRange(it.last().split("-"))) }

    fun part1(input: List<String>): Int {
        return prepareInput(input)
            .count {
                (it.first.contains(it.second.first) && it.first.contains(it.second.last))
                        || (it.second.contains(it.first.first) && it.second.contains(it.first.last))
            }
    }

    fun part2(input: List<String>): Int {
        return prepareInput(input)
            .count {
                it.first.contains(it.second.first)
                        || it.first.contains(it.second.last)
                        || it.second.contains(it.first.first)
                        || it.second.contains(it.first.last)
            }
    }

    val sampleInput = readInput("d4p1-sample")
    check(part1(sampleInput) == 2)
    check(part2(sampleInput) == 4)

    val input = readInput("d4p1-input")
    val part1 = part1(input)
    "part1 $part1".println()
    val part2 = part2(input)
    "part2 $part2".println()
}
