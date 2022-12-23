package com.briarshore.aoc2022.day07

import println
import readInput

fun main() {
    fun shared(input: List<String>): Map<String, Int> {
        return buildMap {
            put("", 0)
            var cwd = ""
            input.fold(this) { acc, value ->
                run {
                    when {
                        value.startsWith("$ cd ") -> {
                            val dir = value.substringAfter("$ cd ")
                            cwd = when (dir) {
                                "/" -> ""
                                ".." -> cwd.substringBeforeLast('/', "")
                                else -> if (cwd.isEmpty()) dir else "$cwd/$dir"
                            }
                        }

                        value == "$ ls" -> {
                            "listing ${cwd}".println()
                        }

                        value.startsWith("dir ") -> {
                            "$value".println()
                        }

                        else -> {
                            var dir = cwd
                            val (size, _) = value.split(" ")
                            while (true) {
                                put(dir, getOrElse(dir) { 0 } + size.toInt())
                                if (dir.isEmpty()) break
                                dir = dir.substringBeforeLast('/', "")
                            }
                        }
                    }
                }
                acc
            }
        }
    }

    fun part1(input: List<String>): Int {
        return shared(input).values.sumOf { if (it <= 100_000) it else 0 }
    }

    fun part2(input: List<String>): Int {
        val sizesByName = shared(input)
        val total = sizesByName.getValue("")
        return sizesByName.values.asSequence().filter { 70_000_000 - (total - it) >= 30_000_000  }.min()
    }
    val sampleInput = readInput("d7p1-sample")
    check(part1(sampleInput) == 95437)
    check(part2(sampleInput) == 24933642)

    val input = readInput("d7p1-input")
    val part1 = part1(input)
    check(part1 == 1454188)
    "part1 $part1".println()

    val part2 = part2(input)
    "part2 $part2".println()
    check(part2 == 4183246)
}
