package com.github.mrhoeve.adventofcode.year2022

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(10)
@Component
class Day10() : GameBase2022() {

    private val cycles: MutableList<Cycle> = mutableListOf()
    private val crtLines: MutableList<String> = mutableListOf()

    override fun initializeGame() {
        super.initializeGame()
        loadInput()
    }

    override fun solvePartOne() {
        val calculateSignalStrengthOfIndexes = listOf(20, 60, 100, 140, 180, 220)
        cycles.filter { it.index in calculateSignalStrengthOfIndexes }.sumOf { it.index * it.duringCycleValue }
        logger.info(
            "Total signalstrenth during cycles ${calculateSignalStrengthOfIndexes.joinToString()} is ${
                cycles.filter { it.index in calculateSignalStrengthOfIndexes }.sumOf { it.index * it.duringCycleValue }
            }"
        )
    }

    override fun solvePartTwo() {
        println(crtLines.joinToString("\n"))
        logger.info("The answer is BGKAEREZ")
    }

    override fun inputFile(): String {
        return "/2022/input_10.txt"
    }

    fun loadInput() {
        openAndParseFile(this::parseLine)
    }

    private fun parseLine(line: String) {
        if (line.isNotBlank()) {
            if (line.startsWith("noop", ignoreCase = true)) {
                addCycle()
            } else {
                val value = line.substring(5)
                addCycle()
                addCycle(value.toInt())
            }
        }
    }

    private fun addCycle(changeValueAfterCycleWith: Int? = null) {
        changeValueAfterCycleWith?.let {
            cycles.add(
                Cycle(
                    if (cycles.size == 0) 1 else cycles.last().index + 1,
                    if (cycles.size == 0) 1 else cycles.last().afterCycleValue,
                    if (cycles.size == 0) 1 else cycles.last().afterCycleValue + it
                )
            )
        } ?: run {
            cycles.add(
                Cycle(
                    if (cycles.size == 0) 1 else cycles.last().index + 1,
                    if (cycles.size == 0) 1 else cycles.last().afterCycleValue,
                    if (cycles.size == 0) 1 else cycles.last().afterCycleValue
                )
            )
        }
        addPixelOrDot()
    }

    private fun addPixelOrDot() {
        val indexOfCyle = cycles.last().index
        val currentCrtRow = (indexOfCyle - 1) / 40
        if (crtLines.getOrNull(currentCrtRow) == null) crtLines.add("")
        val currentIndex = crtLines[currentCrtRow].length
        if (currentIndex in cycles.last().duringCycleValue - 1..cycles.last().duringCycleValue + 1) {
            crtLines[currentCrtRow] = crtLines[currentCrtRow].plus("#")
        } else {
            crtLines[currentCrtRow] = crtLines[currentCrtRow].plus(" ")
        }
    }

    data class Cycle(
        val index: Int,
        val duringCycleValue: Int,
        val afterCycleValue: Int = duringCycleValue
    )
}
