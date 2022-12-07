package com.github.mrhoeve.adventofcode.year2022

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(6)
@Component
class Day06() : GameBase2022() {
    private var marker: Int = 0
    override fun solvePartOne() {
        loadInputPartOne()
        logger.info("When length of marker is 4 chars, the marker is found at $marker")
    }

    override fun solvePartTwo() {
        loadInputPartTwo()
        logger.info("When length of marker is 14 chars, the marker is found at $marker")
    }

    override fun inputFile(): String {
        return "/2022/input_06.txt"
    }

    fun loadInputPartOne() {
        openAndParseFile(this::parseLinePartOne)
    }

    fun loadInputPartTwo() {
        openAndParseFile(this::parseLinePartTwo)
    }

    private fun parseLinePartOne(line: String) {
        if (line.isNotBlank()) {
            findMarker(line, 4)
        }
    }

    fun parseLinePartTwo(line: String) {
        if (line.isNotBlank()) {
            findMarker(line, 14)
        }
    }

    private fun findMarker(line: String, lengthOfMarker: Int) {
        for (currentMarker in lengthOfMarker..line.length) {
            if (line.subSequence(currentMarker - lengthOfMarker, currentMarker).chunked(1).distinct()
                    .count() == lengthOfMarker
            ) {
                marker = currentMarker
                break
            }
        }
    }
}
