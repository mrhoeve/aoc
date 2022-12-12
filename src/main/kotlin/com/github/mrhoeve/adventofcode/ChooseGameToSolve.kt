package com.github.mrhoeve.adventofcode

import com.github.mrhoeve.adventofcode.abstractions.interfaces.Game
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class ChooseGameToSolve(private val games: List<Game>) {
    @EventListener(ApplicationReadyEvent::class)
    fun runGame() {
        // TOOD: The idea is to build a menu based on the year and on the given order.
        // Until then: just play the last game (typically the game of that day ;-))
        games.last().initializeGame()
        games.last().solvePartOne()
        games.last().solvePartTwo()
    }
}
