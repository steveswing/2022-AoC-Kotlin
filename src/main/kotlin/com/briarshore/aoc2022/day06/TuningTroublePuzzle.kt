package com.briarshore.aoc2022.day06

import println
import readInput

fun main() {
    fun scanForStartPacket(noise: String, expectedIndex: Int): Int {
        val startPacket = noise
            .asSequence()
            .windowed(4, 1)
            .filter { it.size == 4 && it.toSet().size == 4 }
            .first()
            .toCharArray()

        val result = 4 + noise.indexOf(startPacket.concatToString())
        if (result == expectedIndex) {
            "found start packet ${startPacket.concatToString()} in ${noise} at ${result}".println()
        }
        return result
    }

    fun scanForStartMessage(noise: String, expectedIndex: Int): Int {
        val startPacket = noise
            .asSequence()
            .windowed(14, 1)
            .filter { it.size == 14 && it.toSet().size == 14 }
            .first()
            .toCharArray()

        val result = 14 + noise.indexOf(startPacket.concatToString())
        if (result == expectedIndex) {
            "found start message ${startPacket.concatToString()} in ${noise} at ${result}".println()
        }
        return result
    }

    fun part1(input: List<Pair<String, Int>>): Int {
        input.stream().forEach {
            check(scanForStartPacket(it.first, it.second) == it.second)
        }
        return 0
    }

    fun part2(input: List<Pair<String, Int>>): Int {
        input.stream().forEach {
            val actualIndex = scanForStartMessage(it.first, it.second)
            check(actualIndex == it.second)
        }
        return 0
    }

    val sampleInputPart1 = listOf(
        Pair("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7),
        Pair("bvwbjplbgvbhsrlpgdmjqwftvncz", 5),
        Pair("nppdvjthqldpwncqszvftbrmjlhg", 6),
        Pair("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10),
        Pair("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11)
    )
    check(part1(sampleInputPart1) == 0)
    val input = readInput("d6p1-input")
    val startPacket = scanForStartPacket(input.first(), 1702)
    "startPacket $startPacket".println()


    val sampleInputPart2 = listOf(
        Pair("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 19),
        Pair("bvwbjplbgvbhsrlpgdmjqwftvncz", 23),
        Pair("nppdvjthqldpwncqszvftbrmjlhg", 23),
        Pair("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 29),
        Pair("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 26)
    )

    check(part2(sampleInputPart2) == 0)
    val startMessage = scanForStartMessage(input.first(), 3559)
    "startMessage $startMessage".println()
}
