package com.briarshore.aoc2022.day11

import println
import readInput
import kotlin.math.absoluteValue

typealias WorryLevel = Long

infix fun Int.safeTimes(other: Int) = (this * other).also {
    check(other == 0 || it / other == this) { "Integer overflow at $this * $other" }
}

infix fun Long.safeTimes(other: Long) = (this * other).also {
    check(other == 0L || it / other == this) { "Long overflow at $this * $other" }
}

infix fun Int.safeTimes(other: Long) = (this.toLong() * other).also {
    check(other == 0L || it / other == this.toLong()) { "Integer overflow at $this * $other" }
}

infix fun Long.safeTimes(other: Int) = (this * other).also {
    check(other == 0 || it / other == this) { "Long overflow at $this * $other" }
}

fun String.toWorryLevel(): WorryLevel? = toLongOrNull()

fun gcd(a: Int, b: Int): Int = if (b == 0) a.absoluteValue else gcd(b, a % b)

fun gcd(a: Long, b: Long): Long = if (b == 0L) a.absoluteValue else gcd(b, a % b)

fun gcd(f: Long, vararg n: Long): Long = n.fold(f, ::gcd)

fun Iterable<Long>.gcd(): Long = reduce(::gcd)

fun lcm(a: Int, b: Int): Int = (a safeTimes b) / gcd(a, b)

fun lcm(f: Int, vararg n: Int): Long = n.map { it.toLong() }.fold(f.toLong(), ::lcm)

fun lcm(a: Long, b: Long): Long = (a safeTimes b) / gcd(a, b)

fun lcm(f: Long, vararg n: Long): Long = n.map { it }.fold(f, ::lcm)

@JvmName("lcmForInt")
fun Iterable<Int>.lcm(): Long = map { it.toLong() }.reduce(::lcm)

fun Iterable<Long>.lcm(): Long = reduce(::lcm)

fun Iterable<Long>.product(): Long = reduce(Long::safeTimes)

fun Sequence<Long>.product(): Long = reduce(Long::safeTimes)

@JvmName("intProduct")
fun Iterable<Int>.product(): Long = fold(1L, Long::safeTimes)

@JvmName("intProduct")
fun Sequence<Int>.product(): Long = fold(1L, Long::safeTimes)

class Monkey constructor(
    var id: Int, var items: MutableList<WorryLevel>, var operation: (WorryLevel) -> WorryLevel, var divisor: Int, var trueFwd: Int, var falseFwd: Int
) {
    var inspections: Long = 0

    fun inspect(count: Long) {
        inspections += count
    }

    data class Builder(
        var id: Int?,
        var items: MutableList<WorryLevel>?,
        var divisor: Int?,
        var operation: ((WorryLevel) -> WorryLevel)?,
        var trueFwd: Int?,
        var falseFwd: Int?
    ) {
        fun id(id: Int?) = apply { this.id = id }
        fun items(items: MutableList<WorryLevel>?) = apply { this.items = items }
        fun divisor(divisor: Int?) = apply { this.divisor = divisor }
        fun operation(operation: (WorryLevel) -> WorryLevel) = apply { this.operation = operation }
        fun trueFwd(trueFwd: Int?) = apply { this.trueFwd = trueFwd }
        fun falseFwd(falseFwd: Int?) = apply { this.falseFwd = falseFwd }
        fun build(): Monkey {
            return Monkey(this.id!!, this.items!!, this.operation!!, this.divisor!!, this.trueFwd!!, this.falseFwd!!)
        }
    }
}

fun parseOperationInner(s: String): (WorryLevel) -> WorryLevel {
    val (operator, operand2) = s.split(" ")

    return when (operator) {
        "+" -> { old -> old + (operand2.toWorryLevel() ?: old) }
        "*" -> { old -> old * (operand2.toWorryLevel() ?: old) }
        else -> error("failed to parse operation")
    }
}

fun parse(m: Monkey.Builder, line: String): Monkey.Builder {
    when {
        line.startsWith("Monkey ") -> m.id = line.substringAfter("Monkey ").substringBeforeLast(':').toInt()
        line.contains("Starting items: ") -> m.items = line.substringAfter("Starting items: ").split(", ").map { it.toLong() }.toMutableList()
        line.contains("Operation: new = old ") -> m.operation = parseOperationInner(line.substringAfter("Operation: new = old "))
        line.contains("Test: divisible by ") -> m.divisor = line.substringAfter("Test: divisible by ").toInt()
        line.contains("If true: throw to monkey ") -> m.trueFwd = line.substringAfter("If true: throw to monkey ").toInt()
        line.contains("If false: throw to monkey ") -> m.falseFwd = line.substringAfter("If false: throw to monkey ").toInt()
    }
    return m
}

fun parseInt(line: String): Int {
    return when {
        line.startsWith("Monkey ") -> line.substringAfter("Monkey ").substringBeforeLast(':').toInt()
        line.contains("Test: divisible by ") -> line.substringAfter("Test: divisible by ").toInt()
        line.contains("If true: throw to monkey ") -> line.substringAfter("If true: throw to monkey ").toInt()
        line.contains("If false: throw to monkey ") -> line.substringAfter("If false: throw to monkey ").toInt()
        else -> error("couldn't parse int from $line")
    }
}

fun parseItems(line: String): MutableList<WorryLevel> {
    return when {
        line.contains("Starting items: ") -> line.substringAfter("Starting items: ").split(", ").map { it.toLong() }.toMutableList()
        else -> error("couldn't parse int from $line")
    }
}

fun parseOperation(line: String): (WorryLevel) -> WorryLevel {
    return when {
        line.contains("Operation: new = old ") -> parseOperationInner(line.substringAfter("Operation: new = old "))
        else -> error("couldn't parse int from $line")
    }
}

fun createMonkeyBuilder() = Monkey.Builder(null, null, null, null, null, null)

fun main() {
    fun parseMonkeys(lines: List<String>): List<Monkey> {
        return lines.chunked(7)
            .map { Monkey(parseInt(it[0]), parseItems(it[1]), parseOperation(it[2]), parseInt(it[3]), parseInt(it[4]), parseInt(it[5])) }
    }

    fun parseMonkies(lines: List<String>): List<Monkey> {
        return lines.fold(mutableListOf(createMonkeyBuilder())) { acc: MutableList<Monkey.Builder>, s: String ->
            if (s.isEmpty()) {
                acc += createMonkeyBuilder()
            } else {
                parse(acc.last(), s)
            }
            return@fold acc
        }.map { it.build() }
    }

    fun simianShenanigans(rounds: Int, monkeys: List<Monkey>, worryLevel: Int) {
        val lcm = monkeys.map { it.divisor }.lcm().toInt()

        (1..rounds).map {
            monkeys.map { monkey ->
                val items = monkey.items
                val testResult = when (rounds) {
                    20 -> items.map { item -> monkey.operation(item) / worryLevel }.partition { item -> 0L == item % monkey.divisor }
                    else -> items.map { item -> monkey.operation(item) % lcm }.partition { item -> 0L == item % monkey.divisor }
                }
                testResult.first.map { item -> monkeys[monkey.trueFwd].items += item }
                testResult.second.map { item -> monkeys[monkey.falseFwd].items += item }
                monkey.inspect(monkey.items.size.toLong())
                monkey.items.clear()
            }
        }
    }

    fun part1(lines: List<String>): Long {
        val monkeys = parseMonkeys(lines)

        val worryLevel = 3
        val rounds = 20
        simianShenanigans(rounds, monkeys, worryLevel)

        return monkeys.map { it.inspections }.sortedDescending().take(2).product()
//        return monkeys.sortedByDescending { it.inspections }.take(2).fold(1L) { acc: Long, m: Monkey -> acc * m.inspections}
    }

    fun part2(lines: List<String>): Long {
        val monkeys = parseMonkeys(lines)

        val worryLevel = 3
        val rounds = 10000
        simianShenanigans(rounds, monkeys, worryLevel)

        return monkeys.sortedByDescending { it.inspections }.take(2).fold(1L) { acc: Long, m: Monkey -> acc * m.inspections }
    }

    val sampleInput = readInput("d11-sample")
    val part1Sample = part1(sampleInput)
    "part1Sample simian shenanigans $part1Sample".println()
    check(part1Sample == 10605L)

    val input = readInput("d11-input")
    val part1 = part1(input)
    "part1 simian shenanigans $part1".println()
    check(part1 == 117640L)

    val part2Sample = part2(sampleInput)
    "part2Sample simian shenanigans $part2Sample".println()
    check(part2Sample == 2713310158L)

    val part2 = part2(input)
    "part2 simian shenanigans $part2".println()
    check(part2 == 30616425600L)
}
