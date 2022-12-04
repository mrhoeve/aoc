package com.github.mrhoeve.adventofcode.year2022

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(3)
@Component
class Day03() : GameBase2022() {
    private val tokenCount: MutableList<Long> = mutableListOf()
    private val lines: MutableList<String> = mutableListOf()


    override fun solvePartOne() {
        loadInputPartOne()
        logger.info("When searching for common token in two parts of the rucksacks, the total value is ${tokenCount.sumOf { it }}")
    }

    override fun solvePartTwo() {
        loadInputPartTwo()
        logger.info(
            "When searching for the common token over three elves, the total value of tokens is ${tokenCount.sumOf { it }}"
        )
    }

    override fun inputFile(): String {
        return "/2022/input_03.txt"
    }

    fun loadInputPartOne() {
        tokenCount.clear()
        openAndParseFile(this::parseLinePartOne)
    }

    fun loadInputPartTwo() {
        tokenCount.clear()
        openAndParseFile(this::parseLinePartTwo)
    }

    private fun parseLinePartOne(line: String) {
        if (line.isNotBlank()) {
            // Split each line in two equal parts (rucksacks) and search for the token that's shared in each part
            val leftPartOfRucksack = line.substring(0, (line.length / 2))
            val rightPartOfRucksack = line.substring((line.length / 2))
            leftPartOfRucksack.toCharArray().distinct().filter { rightPartOfRucksack.contains(it, false) }.forEach {
                tokenCount.add(
                    calculateValue(it)
                )
            }
        }
    }

    fun parseLinePartTwo(line: String) {
        if (line.isNotBlank()) {
            // Search for the common token in every 3 rucksacks
            lines.add(line)
            if (lines.size == 3) {
                lines.first().toCharArray().distinct()
                    .filter { lines[1].contains(it, false) && lines[2].contains(it, false) }.forEach {
                        tokenCount.add(
                            calculateValue(it)
                        )
                    }
                lines.clear()
            }
        }
    }

    private fun calculateValue(char: Char): Long {
        return if (char.isUpperCase())
            (char.code - 38).toLong()
        else
            (char.code - 96).toLong()
    }
}
