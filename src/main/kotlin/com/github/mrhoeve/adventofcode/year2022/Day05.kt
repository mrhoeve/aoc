package com.github.mrhoeve.adventofcode.year2022

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(5)
@Component
class Day05() : GameBase2022() {
    private val stacks: MutableMap<String, MutableList<Crate>> = mutableMapOf()
    private var lines: MutableList<String> = mutableListOf()

    override fun solvePartOne() {
        reset()
        loadInputPartOne()
        logger.info(
            "The stacks on top using CrateMover 9000 are ${
                stacks.map { it.value.last().identifier }.joinToString(separator = "")
            }"
        )
    }

    override fun solvePartTwo() {
        reset()
        loadInputPartTwo()
        logger.info(
            "The stacks on top using CrateMover 9001 are ${
                stacks.map { it.value.last().identifier }.joinToString(separator = "")
            }"
        )
    }

    override fun inputFile(): String {
        return "/2022/input_05.txt"
    }

    fun reset() {
        stacks.clear()
        lines.clear()
    }

    fun loadInputPartOne() {
        openAndParseFile(this::parseLinePartOne)
    }

    fun loadInputPartTwo() {
        openAndParseFile(this::parseLinePartTwo)
    }

    private fun parseLinePartOne(line: String) {
        if (line.isNotBlank()) {
            if (stacks.size == 0) {
                lines.add(line)
            } else {
                moveCratesPartOne(line)
            }
        } else {
            if (stacks.size == 0) {
                initializeStacks()
            }
        }
    }

    fun parseLinePartTwo(line: String) {
        if (line.isNotBlank()) {
            if (stacks.size == 0) {
                lines.add(line)
            } else {
                moveCratesPartTwo(line)
            }
        } else {
            if (stacks.size == 0) {
                initializeStacks()
            }
        }
    }

    private fun initializeStacks() {
        val stacksFoundAtIndex: MutableMap<Int, Int> = mutableMapOf()
        lines.last().split(" ").filter { !it.isNullOrBlank() }.forEach { stack ->
            stacksFoundAtIndex[stack.toInt()] = lines.last().indexOf(stack)
            stacks[stack] = mutableListOf()
        }
        lines.removeLast()
        while (lines.size > 0) {
            for (currentStack in 1..stacksFoundAtIndex.size) {
                stacksFoundAtIndex[currentStack]?.let { currentStackIndex ->
                    if (lines.last().length > currentStackIndex) {
                        val crate = lines.last().substring(currentStackIndex, currentStackIndex + 1)
                        if (!crate.isNullOrBlank()) {
                            stacks[currentStack.toString()]?.add(Crate(crate))
                        }
                    }
                }
            }
            lines.removeLast()
        }
    }

    private fun moveCratesPartOne(line: String) {
        val moveCrates = processLine(line)

        for (count in 1..moveCrates.stacksToMove) {
            stacks[moveCrates.fromStack]?.last()?.let { stacks[moveCrates.toStack]?.add(it) }
            stacks[moveCrates.fromStack]?.removeLast()
        }
    }

    private fun moveCratesPartTwo(line: String) {
        val moveCrates = processLine(line)

        stacks[moveCrates.fromStack]?.takeLast(moveCrates.stacksToMove)?.let {
            stacks[moveCrates.toStack]?.addAll(it)
        }
        stacks[moveCrates.fromStack] =
            stacks[moveCrates.fromStack]?.dropLast(moveCrates.stacksToMove)?.toMutableList() ?: mutableListOf()
    }

    private fun processLine(line: String): MoveCrates {
        val moveArgs = Regex("move (\\d+) from (\\d+) to (\\d+)").find(line)!!
        val (stacksToMove, fromStack, toStack) = moveArgs.destructured
        return MoveCrates(stacksToMove.toInt(), fromStack, toStack)
    }

    data class Crate(
        val identifier: String
    )

    data class MoveCrates(
        val stacksToMove: Int,
        val fromStack: String,
        val toStack: String
    )
}
