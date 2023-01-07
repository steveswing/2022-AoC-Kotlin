package com.briarshore.aoc2022.day13

import kotlinx.serialization.json.*
import println
import kotlin.math.max
import readInput

fun <L, R> List<L>.zipWithPadding(that: List<R>): List<Pair<L?, R?>> =
    buildList { for (i in 0 until max(this@zipWithPadding.size, that.size)) add(this@zipWithPadding.getOrNull(i) to that.getOrNull(i)) }

fun JsonArray.zipWithPadding(that: JsonArray) = this.toList().zipWithPadding(that.toList())

fun isInCorrectOrder(left: JsonArray, right: JsonArray) = left < right

operator fun JsonArray.compareTo(that: JsonArray): Int {
    val stack = ArrayDeque<Pair<JsonElement?, JsonElement?>>().apply { this@compareTo.zipWithPadding(that).reversed().forEach(::addFirst) }

    while (stack.isNotEmpty()) {
        val (left, right) = stack.removeFirst()
        when {
            left == null && right != null                   -> return -1
            left != null && right == null                   -> return 1
            left is JsonPrimitive && right is JsonPrimitive -> when {
                left.int < right.int  -> return -1
                left.int == right.int -> continue
                left.int > right.int  -> return 1
            }

            left is JsonArray && right is JsonArray     -> left.zipWithPadding(right).reversed().forEach(stack::addFirst)
            left is JsonPrimitive && right is JsonArray -> stack.addFirst(JsonArray(listOf(left)) to right)
            left is JsonArray && right is JsonPrimitive -> stack.addFirst(left to JsonArray(listOf(right)))
            else                                        -> error("Invalid comparison")
        }
    }

    error("Comparison failed")
}

fun dividerPacket(number: Int): JsonArray = JsonArray(listOf(JsonArray(listOf(JsonPrimitive(number)))))

fun main() {
    fun parse(lines: List<String>) = lines.filter { it.isNotEmpty() }.map { Json.parseToJsonElement(it).jsonArray }

    fun part1(lines: List<String>): Int = lines
        .asSequence()
        .chunked(3)
        .map { parse(it) }
        .map { isInCorrectOrder(it[0], it[1]) }
        .mapIndexed { index, result -> (index + 1).takeIf { result } ?: 0 }
        .sumOf { it }

    fun part2(lines: List<String>): Int {
        val divider2 = dividerPacket(2)
        val divider6 = dividerPacket(6)
        return buildList { addAll(parse(lines)); add(divider2); add(divider6) }
            .sortedWith(JsonArray::compareTo)
            .mapIndexed { index, it -> if (it == divider2 || it == divider6) index + 1 else 1 }
            .fold(1) { acc, it -> acc * it }
    }

    val sampleInput = readInput("d13-sample")
    val part1Sample = part1(sampleInput)
    "part1Sample $part1Sample".println()
    check(part1Sample == 13)

    val part2Sample = part2(sampleInput)
    "part2Sample $part2Sample".println()
    check(part2Sample == 140)

    val input = readInput("d13-input")
    val part1 = part1(input)
    "part1 $part1".println()
    check(part1 == 5905)

    val part2 = part2(input)
    "part2 $part2".println()
    check(part2 == 21691)
}
