package com.briarshore.aoc2022.day03

import println
import readInput
import kotlin.streams.asSequence

fun main() {
    val lowerRange = IntRange('a'.code, 'z'.code)  // 97 - 122 -> 1 - 26 = c - 96
    val upperRange = IntRange('A'.code, 'Z'.code) // 65 - 90 -> 27 - 52 =  c - 38

    fun translateToPriority(item: Int): Int {
        return when {
            lowerRange.contains(item) -> item - 96
            upperRange.contains(item) -> item - 38
            else -> {
                throw Exception("Invalid Character value")
            }
        }
    }

    fun sharedFold(input: List<String>): MutableList<MutableList<Set<Char>>> =
        input.foldIndexed(mutableListOf()) { index: Int, acc: MutableList<MutableList<Set<Char>>>, s: String ->
            if (index % 3 == 0) {
                acc.add(mutableListOf())
            }
            acc.last().add(s.toSet())
            acc
        }

    fun mapToCharSet(contents: String): Set<Char> = contents.toSet()

    fun mapToIntSet(contents: String): Set<Int> = contents
        .chars()
        .map(::translateToPriority)
        .asSequence()
        .toHashSet()

    fun part1(contents: List<String>): Int {
        return contents.map { Pair(it.slice(IntRange(0, it.length / 2 - 1)), it.slice(IntRange(it.length / 2, it.length - 1))) }
            .map { Pair(mapToCharSet(it.first), mapToCharSet(it.second)) }
//            .map { Pair(mapToIntSet(it.first), mapToIntSet(it.second)) }
//            .filter { it.first.size == it.second.size }
            .sumOf { translateToPriority(it.first.intersect(it.second).first().code) }
    }

    fun part2(contents: List<String>): Int {
        return sharedFold(contents)
            .map { it.fold(mutableSetOf()) { acc: Set<Char>, contents: Set<Char> ->
                if (acc.isEmpty()) {
                    contents
                } else {
                    acc.intersect(contents)
                }
            }}
            .map { it.first() }
            .map { it.code }
            .sumOf(::translateToPriority)
    }

    val sampleInput = readInput("d3p1-sample")
    check(part1(sampleInput) == 157)
    check(part2(sampleInput) == 70)

    val input = readInput("d3p1-input")
    val part1 = part1(input)
    "part1 $part1".println()

    val part2 = part2(input)
    "part2 $part2".println()

}
