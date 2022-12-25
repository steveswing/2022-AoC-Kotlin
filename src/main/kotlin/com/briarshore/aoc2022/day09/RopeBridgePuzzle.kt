package com.briarshore.aoc2022.day09

import com.briarshore.aoc2022.day09.Direction.*
import println
import readInput
import kotlin.math.abs

enum class Direction(val move: Move) {
    UP(Move(0, 1)), DOWN(Move(0, -1)), LEFT(Move(-1, 0)), RIGHT(Move(1, 0))
}

data class Pos(val x: Int, val y: Int)
data class Move(val dx: Int, val dy: Int) {
    val distance: Int get() = maxOf(abs(dx), abs(dy))
}

operator fun Pos.plus(move: Move): Pos = copy(x = x + move.dx, y = y + move.dy)
operator fun Pos.minus(other: Pos): Move = Move(x - other.x, y - other.y)

fun main() {
    fun String.toMovements(): List<Direction> {
        val (dir, len) = split(" ")
        val direction = when (dir) {
            "U" -> UP
            "D" -> DOWN
            "L" -> LEFT
            "R" -> RIGHT
            else -> error("oops")
        }
        return List(len.toInt()) { direction }
    }

    fun tailTrack(head: Pos, tail: Pos): Move {
        val rope = head - tail
        return if (rope.distance > 1) {
            Move(rope.dx.coerceIn(-1..1), rope.dy.coerceIn(-1..1))
        } else {
            Move(0, 0)
        }
    }

    fun part1(lines: List<String>): Int {
        var head = Pos(0, 0)
        var tail = head
        val tailVisited = mutableSetOf(tail)

        lines.flatMap { it.toMovements() }.forEach {
            head += it.move
            tail += tailTrack(head, tail)
            tailVisited += tail
        }
        return tailVisited.size
    }

    fun part2(lines: List<String>): Int {
        val knotsNbr = 10
        val knots = MutableList(knotsNbr) { Pos(0, 0) }
        val tailVisited = mutableSetOf(knots.last())

        for (d in lines.flatMap { it.toMovements() }) {
            knots[0] = knots[0] + d.move
            for ((headIndex, tailIndex) in knots.indices.zipWithNext()) {
                knots[tailIndex] = knots[tailIndex] + tailTrack(knots[headIndex], knots[tailIndex])
            }
            tailVisited += knots.last()
        }

        return tailVisited.size
    }

    val sampleInput = readInput("d9p1-sample")
    val samplePart1 = part1(sampleInput)
    "samplePart1 $samplePart1".println()
    check(samplePart1 == 13)

    val sampleInput2 = readInput("d9p2-sample")
    val samplePart2 = part2(sampleInput2)
    "samplePart2 $samplePart2".println()
    check(samplePart2 == 36)

    val input = readInput("d9-input")
    val part1 = part1(input)
    "part1 $part1".println()
    check(part1 == 6190)

    val part2 = part2(input)
    "part2 $part2".println()
    check(part2 == 2516)
}
