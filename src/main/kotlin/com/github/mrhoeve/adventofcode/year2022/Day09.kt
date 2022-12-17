package com.github.mrhoeve.adventofcode.year2022

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import kotlin.math.abs

@Order(9)
@Component
class Day09() : GameBase2022() {

    private val steps: MutableList<Step> = mutableListOf()
    private val players: MutableList<Player> = mutableListOf()

    private val regex_direction_input = Regex("([UDLR]+) (\\d+)")

    override fun initializeGame() {
        super.initializeGame()
        loadInput()
    }

    override fun solvePartOne() {
        setupPlayers(2)
        executeDirections()
        logger.info("With two players, the tail visits ${players.last().tilesVisited.count()} different tiles")
    }

    override fun solvePartTwo() {
        setupPlayers(10)
        executeDirections()
        logger.info("With ten players, the tail visits ${players.last().tilesVisited.count()} different tiles")
    }

    override fun inputFile(): String {
        return "/2022/input_09.txt"
    }

    fun loadInput() {
        openAndParseFile(this::parseLine)
    }

    private fun setupPlayers(numberOfPlayers: Int) {
        players.clear()
        for (player in 1..numberOfPlayers) {
            players.add(Player())
        }
    }

    private fun parseLine(line: String) {
        if (line.isNotBlank()) {
            regex_direction_input.find(line)?.let {
                val (direction, steps) = it.destructured
                this.steps.add(Step(Direction.fromValue(direction), steps.toInt()))
            }
        }
    }

    private fun executeDirections() {
        steps.forEach { currentStep ->
            for (step in 1..currentStep.steps) {
                when (currentStep.direction) {
                    Direction.UP -> players.first().currentPosition.y += 1
                    Direction.DOWN -> players.first().currentPosition.y -= 1
                    Direction.LEFT -> players.first().currentPosition.x -= 1
                    Direction.RIGHT -> players.first().currentPosition.x += 1
                }
                calculateStepsOfOtherPlayers(currentStep.direction)
            }
        }
    }

    private fun calculateStepsOfOtherPlayers(direction: Direction) {
        for (player in 1 until players.size) {
            calculateNextStep(players[player - 1], players[player])
        }
    }

    private fun calculateNextStep(playerAhead: Player, playerFollowing: Player) {
        if (twoPlayersTouch(playerAhead, playerFollowing)) return
        val tailYEnd: Int
        val tailXEnd: Int

        val diffX = playerAhead.currentPosition.x - playerFollowing.currentPosition.x
        val diffY = playerAhead.currentPosition.y - playerFollowing.currentPosition.y
        if (abs(diffX) >= 1 && abs(diffY) >= 1) {
            tailXEnd = takeStep(playerAhead.currentPosition.x, playerFollowing.currentPosition.x)
            tailYEnd = takeStep(playerAhead.currentPosition.y, playerFollowing.currentPosition.y)
        } else {
            if (diffY == 0) {
                tailYEnd = playerFollowing.currentPosition.y
                tailXEnd = takeStep(playerAhead.currentPosition.x, playerFollowing.currentPosition.x)
            } else {
                tailXEnd = playerFollowing.currentPosition.x
                tailYEnd = takeStep(playerAhead.currentPosition.y, playerFollowing.currentPosition.y)
            }
        }
        playerFollowing.currentPosition.x = tailXEnd
        playerFollowing.currentPosition.y = tailYEnd
        playerFollowing.tilesVisited.add(Position(tailXEnd, tailYEnd))
    }

    private fun takeStep(positionPlayerAhead: Int, positionPlayerFollowing: Int): Int {
        return if (positionPlayerAhead < positionPlayerFollowing) {
            positionPlayerFollowing - 1
        } else {
            positionPlayerFollowing + 1
        }
    }

    private fun twoPlayersTouch(playerAhead: Player, playerFollowing: Player): Boolean {
        return (abs(playerAhead.currentPosition.x - playerFollowing.currentPosition.x) <= 1 &&
                abs(playerAhead.currentPosition.y - playerFollowing.currentPosition.y) <= 1)
    }

    data class Player(
        var currentPosition: Position = Position(0, 0),
        var tilesVisited: MutableSet<Position> = mutableSetOf(Position(0, 0))
    )

    data class Position(
        var x: Int = 0,
        var y: Int = 0,
    )

    data class Step(
        val direction: Direction,
        val steps: Int
    )

    enum class Direction(val value: String) {
        UP("U"),
        DOWN("D"),
        LEFT("L"),
        RIGHT("R");

        companion object {
            private val map = Direction.values().associateBy(Direction::value)
            fun fromValue(value: String) = map[value] ?: throw RuntimeException("Invalid value: $value")
        }
    }
}
