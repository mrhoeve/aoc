package com.github.mrhoeve.adventofcode.abstractions.interfaces

interface Game {
    fun year(): Int
    fun inputFile(): String
    fun solvePartOne(): Unit
    fun solvePartTwo(): Unit
}
