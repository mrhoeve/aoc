package com.github.mrhoeve.adventofcode.year2022

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(4)
@Component
class Day04() : GameBase2022() {
    private var count: Long = 0

    override fun solvePartOne() {
        loadInputPartOne()
        logger.info("There are ${count} sections that are completely in another section")
    }

    override fun solvePartTwo() {
        loadInputPartTwo()
        logger.info("There are ${count} sections that are partially or completely in another section")
    }

    override fun inputFile(): String {
        return "/2022/input_04.txt"
    }

    fun loadInputPartOne() {
        count = 0
        openAndParseFile(this::parseLinePartOne)
    }

    fun loadInputPartTwo() {
        count = 0
        openAndParseFile(this::parseLinePartTwo)
    }

    private fun parseLinePartOne(line: String) {
        if (line.isNotBlank()) {
            val sections = line.split(",")
            val firstSection = calculateSections(sections.first())
            val secondSection = calculateSections(sections.last())
            if (firstSection.containsAll(secondSection) || secondSection.containsAll(firstSection)) {
                count++
            }
        }
    }

    fun parseLinePartTwo(line: String) {
        if (line.isNotBlank()) {
            val sections = line.split(",")
            val firstSection = calculateSections(sections.first())
            val secondSection = calculateSections(sections.last())
            if (firstSection.any { it in secondSection }) {
                count++
            }
        }
    }

    private fun calculateSections(sectionsAsLine: String): List<String> {
        val sections = sectionsAsLine.split("-")
        return if (sections.first() == sections.last())
            listOf(sections.first())
        else {
            val firstSection = sections.first().toLong()
            val secondSection = sections.last().toLong()
            val allSections: MutableList<String> = mutableListOf()
            for (section in firstSection..secondSection) {
                allSections.add(section.toString())
            }
            allSections.toList()
        }
    }
}
