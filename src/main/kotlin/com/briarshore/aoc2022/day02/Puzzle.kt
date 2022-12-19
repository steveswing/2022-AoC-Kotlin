package com.briarshore.aoc2022.day02

import println
import readInput

enum class Hand(val points: Int) {
    Rock(1), Paper(2), Scissors(3)
}

enum class Outcome(val points: Int) {
    Win(6), Draw(3), Lose(0)
}

interface IPoints {
    fun points(): Int
}
data class Score(val hand: Hand, val outcome: Outcome) : IPoints {
    override fun points(): Int = hand.points + outcome.points
}

data class Round(val moveScore: Score, val counterMoveScore: Score)
fun main() {
    val moves: Map<String, Hand> = mapOf(Pair("A", Hand.Rock), Pair("B", Hand.Paper), Pair("C", Hand.Scissors))

    val outcomes: Map<String, Outcome> = mapOf(Pair("X", Outcome.Lose), Pair("Y", Outcome.Draw), Pair("Z", Outcome.Win))

    val testStrategy: List<Pair<Hand, Outcome>> = listOf(Pair("A", "Y"), Pair("B", "X"), Pair("C", "Z"))
        .map { Pair(moves.getValue(it.first), outcomes.getValue(it.second)) }

    check(part1(testStrategy) == 12)

    val realInput = readInput("d2p1-input")
    val realStrategy: List<Pair<Hand, Outcome>> = realInput.map { it.split(" ") }.map { Pair(moves.getValue(it.first()), outcomes.getValue(it.last())) }
    val part1 = part1(realStrategy)
    "part1 $part1".println()
}

fun part1(strategy: List<Pair<Hand, Outcome>>): Int {
    return strategy.map { playRound(it.first, it.second) }.sumOf { it.second.points() }
}

fun playRound(move: Hand, riggedOutcome: Outcome): Pair<Score, Score> {
    val round = rigOutcome(move, riggedOutcome)
    return Pair(round.moveScore, round.counterMoveScore)
}

fun determineOutcome(move: Hand, counterMove: Hand): Round {
    if (move == counterMove) {
        return Round(Score(move, Outcome.Draw), Score(counterMove, Outcome.Draw))
    } else if (move == Hand.Rock && counterMove == Hand.Scissors) {
        return Round(Score(move, Outcome.Win), Score(counterMove, Outcome.Lose))
    } else if (move == Hand.Paper && counterMove == Hand.Rock) {
        return Round(Score(move, Outcome.Win), Score(counterMove, Outcome.Lose))
    } else if (move == Hand.Scissors && counterMove == Hand.Paper) {
        return Round(Score(move, Outcome.Win), Score(counterMove, Outcome.Lose))
    } else if (move == Hand.Rock && counterMove == Hand.Paper) {
        return Round(Score(move, Outcome.Lose), Score(counterMove, Outcome.Win))
    } else if (move == Hand.Paper && counterMove == Hand.Scissors) {
        return Round(Score(move, Outcome.Lose), Score(counterMove, Outcome.Win))
    } else if (move == Hand.Scissors && counterMove == Hand.Rock) {
        return Round(Score(move, Outcome.Lose), Score(counterMove, Outcome.Win))
    }
    throw Exception("Unreachable")
}

fun rigOutcome(move: Hand, riggedOutcome: Outcome): Round {
    return when (riggedOutcome) {
        Outcome.Draw -> Round(Score(move, riggedOutcome), Score(move, riggedOutcome))
        Outcome.Lose -> Round(Score(move, Outcome.Win), Score(counter(move, riggedOutcome), riggedOutcome))
        Outcome.Win -> Round(Score(move, Outcome.Lose), Score(counter(move, riggedOutcome), riggedOutcome))
    }
}

fun counter(move: Hand, riggedOutcome: Outcome): Hand {
    return when(riggedOutcome) {
        Outcome.Draw -> move
        Outcome.Lose ->
            when(move) {
                Hand.Paper -> Hand.Rock
                Hand.Scissors -> Hand.Paper
                Hand.Rock -> Hand.Scissors
            }
        Outcome.Win ->
            when(move) {
                Hand.Paper -> Hand.Scissors
                Hand.Scissors -> Hand.Rock
                Hand.Rock -> Hand.Paper
            }
    }
}
