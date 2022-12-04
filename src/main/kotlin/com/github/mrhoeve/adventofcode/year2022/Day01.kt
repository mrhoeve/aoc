package com.github.mrhoeve.adventofcode.year2022

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(1)
@Component
class Day01() : GameBase2022() {
    private val listOfCaloriesPerElf: MutableList<CaloriesPerElf> = mutableListOf()
    private var currentElf = CaloriesPerElf()

    init {
        loadInput()
    }

    override fun solvePartOne() {
        logger.info("The elf with the most amount of calories has ${listOfCaloriesPerElf.maxOfOrNull { it.calories.sum() }} calories")
    }

    override fun solvePartTwo() {
        logger.info(
            "The top 3 elves carry a total of ${
                listOfCaloriesPerElf.map { it.calories.sum() }.sortedDescending().take(3).sum()
            } calories"
        )
    }

    override fun inputFile(): String {
        return "/2022/input_01.txt"
    }

    fun loadInput() {
        openAndParseFile(this::parseLine)
    }

    private fun parseLine(line: String) {
        if (line.isNotBlank()) {
            line.toLongOrNull()?.let { currentElf.calories.add(it) }
        } else {
            listOfCaloriesPerElf.add(currentElf)
            currentElf = CaloriesPerElf()
        }
    }

    private data class CaloriesPerElf(
        val calories: MutableList<Long> = mutableListOf()
    )
}
