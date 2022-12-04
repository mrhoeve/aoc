package com.github.mrhoeve.adventofcode.year2022

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(2)
@Component
class Day02() : GameBase2022() {
    private val listOfGames: MutableList<Game> = mutableListOf()

    override fun solvePartOne() {
        loadInputPartOne()
        logger.info("When XYZ is being used for RPS, the total amount of points is ${listOfGames.sumOf { it.points() }}")
    }

    override fun solvePartTwo() {
        loadInputPartTwo()
        logger.info(
            "When XYZ is being used for WDL, the total amount of points is ${listOfGames.sumOf { it.points() }}"
        )
    }

    override fun inputFile(): String {
        return "/2022/input_02.txt"
    }

    fun loadInputPartOne() {
        listOfGames.clear()
        openAndParseFile(this::parseLinePartOne)
    }

    fun loadInputPartTwo() {
        listOfGames.clear()
        openAndParseFile(this::parseLinePartTwo)
    }

    private fun parseLinePartOne(line: String) {
        if (line.isNotBlank()) {
            listOfGames.add(
                Game(
                    theirChoice(line.split(" ").first()), myChoicePartOne(line.split(" ").last())
                )
            )
        }
    }

    private fun parseLinePartTwo(line: String) {
        if (line.isNotBlank()) {
            val theirChoice = theirChoice(line.split(" ").first())
            val strategy = line.split(" ").last()
            listOfGames.add(
                Game(
                    theirChoice, myChoicePartTwo(theirChoice, strategy)
                )
            )
        }
    }

    private fun theirChoice(option: String): RPS {
        return when (option.uppercase()) {
            "A" -> RPS.Rock
            "B" -> RPS.Paper
            "C" -> RPS.Scissors
            else -> throw RuntimeException("Invalid value: $option")
        }
    }

    private fun myChoicePartOne(option: String): RPS {
        return when (option.uppercase()) {
            "X" -> RPS.Rock
            "Y" -> RPS.Paper
            "Z" -> RPS.Scissors
            else -> throw RuntimeException("Invalid value: $option")
        }
    }

    private fun myChoicePartTwo(theirChoise: RPS, option: String): RPS {
        return when (option.uppercase()) {
            "X" -> when (theirChoise) {
                RPS.Rock -> RPS.Scissors
                RPS.Scissors -> RPS.Paper
                else -> RPS.Rock
            }

            "Y" -> theirChoise
            "Z" -> when (theirChoise) {
                RPS.Scissors -> RPS.Rock
                RPS.Paper -> RPS.Scissors
                else -> RPS.Paper
            }

            else -> throw RuntimeException("Invalid value: $option")
        }
    }

    private enum class RPS(val points: Long) {
        Rock(1), Paper(2), Scissors(3)
    }

    private data class Game(
        val theirChoice: RPS, val myChoice: RPS
    ) {
        fun points(): Long {
            return when (determineOutcome()) {
                Outcome.WIN -> myChoice.points + 6
                Outcome.DRAW -> myChoice.points + 3
                else -> myChoice.points
            }
        }

        private fun determineOutcome(): Outcome {
            if (iWin()) return Outcome.WIN
            if (isDraw()) return Outcome.DRAW
            return Outcome.LOOSE
        }

        private fun iWin(): Boolean {
            return myChoice == RPS.Rock && theirChoice == RPS.Scissors || myChoice == RPS.Scissors && theirChoice == RPS.Paper || myChoice == RPS.Paper && theirChoice == RPS.Rock
        }

        private fun isDraw(): Boolean = myChoice == theirChoice

        private enum class Outcome {
            WIN, DRAW, LOOSE
        }
    }

}
// Rock defeats Scissors, Scissors defeats Paper, and Paper defeats Rock.
