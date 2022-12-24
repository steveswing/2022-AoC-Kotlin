package com.briarshore.aoc2022.day08

import println
import readInput

@ExperimentalStdlibApi
fun main() {

    fun scenicScore(trees: List<Int>): Int {
        var count = 0
        for (height in trees.drop(1)) {
            if (height < trees.first()) {
                count++
            }
            if (height >= trees.first()) {
                count++
                break
            }
        }
        return count
    }

    fun visibleFromEdge(trees: List<Int>): Boolean {
        val treesToEdge = trees.drop(1)
        return treesToEdge.takeWhile { trees.first() > it }.count() == treesToEdge.size
    }

    fun transpose(matrix: List<List<Int>>): List<List<Int>> {
        val width = matrix.size
        val height = matrix.first().size
        if (matrix.any { it.size != height }) {
            throw IllegalArgumentException("All nested lists must have the same size, but sizes were ${matrix.filter { it.size != height }}")
        }

        return (0 until width).map { col -> (0 until height).map { row -> matrix[row][col] } }
    }

    fun innerTreesBF(lines: List<String>): Int {
        val heights = lines.map { s -> s.map { it.digitToInt() } }
        val rowCount = heights.size
        val columnCount = heights.first().size

        var visible = Array(rowCount) { BooleanArray(columnCount) }

        (0 until rowCount).map { r ->
            var height = -1
            (0 until columnCount).map { c ->
                if (heights[r][c] > height) {
                    height = heights[r][c]
                    visible[r][c] = true
                }
            }

            height = -1
            (columnCount - 1 downTo 0).map { c ->
                if (heights[r][c] > height) {
                    height = heights[r][c]
                    visible[r][c] = true
                }
            }
        }

        (0 until columnCount).map { c ->
            var height = -1
            (0 until rowCount).map { r ->
                if (heights[r][c] > height) {
                    height = heights[r][c]
                    visible[r][c] = true
                }
                height = -1
                (rowCount - 1 downTo 0).map { r ->
                    if (heights[r][c] > height) {
                        height = heights[r][c]
                        visible[r][c] = true
                    }
                }
            }
        }
        return visible.sumOf { c -> c.count { it } }
    }

    fun innerTrees(lines: List<String>): Int {
        val grid: List<List<Int>> = lines.map { it.map { c -> c.digitToInt() }.toList() }.toList()
        val columnWiseGrid: List<List<Int>> = transpose(grid)

        var visible = mutableSetOf<Pair<Int, Int>>()
        (1 until grid.size - 1).map { r ->
            (1 until grid[r].size - 1).map { c ->
                if (visibleFromEdge(grid[r].drop(c)) || visibleFromEdge(grid[r].reversed().drop(grid[r].size - 1 - c))) {
                    visible.add(Pair(r, c))
                } else if (visibleFromEdge(columnWiseGrid[c].drop(r)) || visibleFromEdge(columnWiseGrid[c].reversed().drop(columnWiseGrid[c].size - 1 - r))) {
                    visible.add(Pair(r, c))
                } else {

                }
            }
        }
        return visible.size
    }

    fun viewingScores(lines: List<String>): Int {
        val grid: List<List<Int>> = lines.map { it.map { c -> c.digitToInt() }.toList() }.toList()
        val columnWiseGrid: List<List<Int>> = transpose(grid)

        val scores = mutableMapOf<Pair<Int, Int>, Int>()
        (1 until grid.size - 1).map { r ->
            (1 until grid[r].size - 1).map { c ->
                val score =
                    scenicScore(grid[r].drop(c)) *
                    scenicScore(grid[r].reversed().drop(grid[r].size - 1 - c)) *
                    scenicScore(columnWiseGrid[c].drop(r)) *
                    scenicScore(columnWiseGrid[c].reversed().drop(columnWiseGrid[c].size - 1 - r))

                scores.put(Pair(r, c), score)
            }
        }
        return scores.values.maxOf { it }
    }

    fun outerTrees(lines: List<String>): Int = (lines.size - 1) * 4

    fun part1(lines: List<String>): Int {
        return outerTrees(lines) + innerTrees(lines)
    }

    fun part2(lines: List<String>): Int {
        return viewingScores(lines)
    }

    check(scenicScore(listOf(5, 4, 9)) == 2)
    check(scenicScore(listOf(5, 3)) == 1)
    check(scenicScore(listOf(5, 5)) == 1)
    check(scenicScore(listOf(5, 1, 2)) == 2)

    check(scenicScore(listOf(5, 5, 2)) == 1)

    check(scenicScore(listOf(5, 3, 3)) == 2)
    check(scenicScore(listOf(5, 3, 5, 3)) == 2)
    check(scenicScore(listOf(5, 3)) == 1)


    check(!visibleFromEdge(listOf(5, 5, 1, 2)))
    check(visibleFromEdge(listOf(5, 1, 2)))
    check(!visibleFromEdge(listOf(1, 2)))
    check(visibleFromEdge(listOf(5, 3, 3, 2)))
    check(!visibleFromEdge(listOf(3, 3, 2)))
    check(visibleFromEdge(listOf(3, 2)))
    check(!visibleFromEdge(listOf(3, 5, 4, 9)))
    check(!visibleFromEdge(listOf(5, 4, 9)))
    check(!visibleFromEdge(listOf(4, 9)))

    check(!visibleFromEdge(listOf(1, 5, 5, 2)))
    check(!visibleFromEdge(listOf(5, 5, 2)))
    check(visibleFromEdge(listOf(5, 2)))
    check(!visibleFromEdge(listOf(3, 3, 5, 6)))
    check(!visibleFromEdge(listOf(3, 5, 6)))
    check(!visibleFromEdge(listOf(5, 6)))
    check(visibleFromEdge(listOf(9, 4, 5, 3)))
    check(!visibleFromEdge(listOf(4, 5, 3)))
    check(visibleFromEdge(listOf(5, 3)))

    check(!visibleFromEdge(listOf(5, 5, 3, 5)))
    check(!visibleFromEdge(listOf(5, 3, 5)))
    check(!visibleFromEdge(listOf(3, 5)))
    check(!visibleFromEdge(listOf(5, 3, 5, 3)))
    check(!visibleFromEdge(listOf(3, 5, 3)))
    check(visibleFromEdge(listOf(5, 3)))
    check(!visibleFromEdge(listOf(1, 3, 4, 9)))
    check(!visibleFromEdge(listOf(3, 4, 9)))
    check(!visibleFromEdge(listOf(4, 9)))

    check(!visibleFromEdge(listOf(3, 5, 5, 0)))
    check(!visibleFromEdge(listOf(5, 5, 0)))
    check(visibleFromEdge(listOf(5, 0)))
    check(!visibleFromEdge(listOf(5, 3, 5, 3)))
    check(!visibleFromEdge(listOf(3, 5, 3)))
    check(visibleFromEdge(listOf(5, 3)))
    check(!visibleFromEdge(listOf(4, 3, 1, 7)))
    check(!visibleFromEdge(listOf(3, 1, 7)))
    check(!visibleFromEdge(listOf(1, 7)))

    val sampleInput = readInput("d8p1-sample")
    check(outerTrees(sampleInput) == 16)
    check(innerTreesBF(sampleInput) == 21)
    check(part1(sampleInput) == 21)

    check(part2(sampleInput) == 8)

    val input = readInput("d8p1-input")
    val part1 = part1(input)
    check(outerTrees(input) == 98*4)
    "part1 $part1".println()
    check(part1 == 1792)
    val innerTreesBF = innerTreesBF(input)
    "innerTreesBF $innerTreesBF".println()
    check(innerTreesBF == 1537)

    val part2 = part2(input)
    "part2 $part2".println()
    check(part2 == 334880)
}
