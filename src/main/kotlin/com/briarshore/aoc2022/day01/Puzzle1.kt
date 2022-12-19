package com.briarshore.aoc2022.day01

import println
import readInput
import kotlin.math.max

fun main() {
    fun part1Simple(input: List<String>): Int {
        var max = 0;
        var total = 0;
        for (s in input) {
            if (s.isNotEmpty()) {
                total += s.toInt()
            } else {
                max = max(max, total)
                total = 0
            }
        }
        return max
    }

    fun part1UsingGroupBy(input: List<String>): Int {
        var elfNbr = 0
        val calorieItemsByElf: Map<String, List<Int>> = input.groupBy({
            if (it.isEmpty()) {
                elfNbr++
            }
            return@groupBy "Elf$elfNbr"
        },
        {
            return@groupBy if (it.isEmpty()) {
                0
            } else {
                it.toInt()
            }
        })

        val calorieTotalsByElf = calorieItemsByElf.mapValues { it.value.sum() }
        return calorieTotalsByElf.values.max()
    }

    fun part1UsingMutatingFold(input: List<String>): Int {
        return input.fold(mutableListOf(0)) { acc: MutableList<Int>, s: String ->
            if (s.isEmpty()) {
                acc.add(0)
            } else {
                val lastIndex = acc.lastIndex
                acc[lastIndex] = acc[lastIndex].plus(s.toInt())
            }
            return@fold acc
        }.max()
    }

    fun sharedFold(input: List<String>) =
        input.fold(mutableListOf(mutableListOf())) { acc: MutableList<MutableList<Int>>, s: String ->
        if (s.isEmpty()) {
            acc.add(mutableListOf())
        } else {
            acc.last().add(s.toInt())
        }
        return@fold acc
    }

    fun part1UsingFold(input: List<String>): Int {
        return sharedFold(input).maxOf { it.sum() }
    }

    fun part2UsingFold(input: List<String>): Int {
        return sharedFold(input).map { it.sum() }
            .sortedDescending()
            .take(3)
            .sum()
    }

//    fun part2(input: List<String>): Int {
//        return input.size
//    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("d1p1-sample-data")
    check(part1Simple(testInput) == 24000)
    check(part1UsingGroupBy(testInput) == 24000)
    check(part1UsingMutatingFold(testInput) == 24000)
    check(part1UsingFold(testInput) == 24000)

    val realInput = readInput("d1p1-input")
    val part1Simple = part1Simple(realInput)
    "part1Simple $part1Simple".println()
    check(part1Simple == 71934)
    val part1UsingGroupBy = part1UsingGroupBy(realInput)
    "part1UsingGroupBy $part1UsingGroupBy".println()
    check(part1UsingGroupBy == 71934)
    val part1UsingMutatingFold = part1UsingMutatingFold(realInput)
    "part1UsingMutatingFold $part1UsingMutatingFold".println()
    check(part1UsingMutatingFold == 71934)
    val part1UsingFold = part1UsingFold(realInput)
    "part1UsingFold $part1UsingFold".println()
    check(part1UsingFold == 71934)

    check(part2UsingFold(testInput) == 45000)

    val part2UsingFold = part2UsingFold(realInput)
    check(part2UsingFold == 211447)
    "part2UsingFold $part2UsingFold".println()

//    val input = readInput("Day01")
//    part1(input).println()
//    part2(input).println()
}
