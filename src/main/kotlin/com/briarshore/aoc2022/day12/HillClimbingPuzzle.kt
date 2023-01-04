package com.briarshore.aoc2022.day12

import com.briarshore.aoc2022.day12.ParcelKind.END
import com.briarshore.aoc2022.day12.ParcelKind.START
import com.briarshore.aoc2022.day12.Terrain.Companion.parse
import println
import readInput

typealias Coordinates = Pair<Int, Int>
enum class ParcelKind {
    START, BETWEEN, END;

    companion object {
        fun from(c: Char) = when (c) {
            'S' -> START
            'E' -> END
            else -> BETWEEN
        }
    }
}

fun Int.Companion.elevation(c: Char) = when (c) {
    'S' -> 0
    'E' -> 'z' - 'a'
    else -> c - 'a'
}
data class Parcel(val x: Int, val y: Int, val elevation: Int, val kind: ParcelKind) {
    val adjacentCoordinates: List<Coordinates> = listOf(x to y - 1, x to y + 1, x - 1 to y, x + 1 to y)

    companion object {
        fun from(x: Int, y: Int, c: Char) = Parcel(
            x, y, Int.elevation(c), ParcelKind.from(c)
        )
    }
}

data class PathSegment(val parcel: Parcel, val distance: Int = 0) {
    fun advance(parcel: Parcel): PathSegment = PathSegment(parcel, distance + 1)
}

class Terrain(private val parcels: List<List<Parcel>>) {
    private val eastWestBounds = parcels.first().indices
    private val northSouthBounds = parcels.indices
    private val destination = findParcel { it.kind == END }

    fun shortestDistance(isDestination: (Parcel) -> Boolean): Int {
        val frontier = ArrayDeque(listOf(PathSegment(destination)))
        val reached = mutableSetOf(destination)

        while (frontier.isNotEmpty()) {
            val path = frontier.removeFirst()
            if (isDestination(path.parcel)) return path.distance

            path.parcel.adjacentCoordinates.mapNotNull(::getParcel)
                .filter { it.elevation >= path.parcel.elevation - 1 }
                .filter { it !in reached }
                .forEach {
                    frontier.addLast(path.advance(it))
                    reached.add(it)
                }
        }

        error("Failed to find a path")
    }
    private fun findParcel(predicate: (Parcel) -> Boolean): Parcel = parcels.flatten().first(predicate)
    private fun getParcel(coordinates: Coordinates) = coordinates.takeIf { it.withinBounds() }?.let { (x, y) -> parcels[y][x] }
    private fun Coordinates.withinBounds() = first in eastWestBounds && second in northSouthBounds

    companion object {
        fun parse(lines: List<String>): Terrain =
            lines.mapIndexed { y: Int, xs: String ->
                xs.mapIndexed { x: Int, c: Char ->
                    Parcel.from(x, y, c)
                }
            }.let(::Terrain)
    }
}
fun main() {
    fun part1(lines: List<String>): Int {
        return parse(lines).shortestDistance { it.kind == START }
    }

    fun part2(lines: List<String>): Int {
        return parse(lines).shortestDistance { it.elevation == 0 }
    }

    val sampleInput = readInput("d12-sample")
    val samplePart1 = part1(sampleInput)
    "samplePart1 $samplePart1".println()
    check(samplePart1 == 31)

    val samplePart2 = part2(sampleInput)
    "samplePart2 $samplePart2".println()
    check(samplePart2 == 29)

    val input = readInput("d12-input")
    val part1 = part1(input)
    "part1 $part1".println()
    check(part1 == 534)

    val part2 = part2(input)
    "part2 $part2".println()
    check(part2 == 525)
}
