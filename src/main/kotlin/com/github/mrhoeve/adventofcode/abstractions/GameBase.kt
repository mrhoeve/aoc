package com.github.mrhoeve.adventofcode.abstractions

import com.github.mrhoeve.adventofcode.abstractions.interfaces.Game
import org.slf4j.LoggerFactory

abstract class GameBase : Game {
    var logger = LoggerFactory.getLogger(this.javaClass)

    override fun initializeGame() {
        // Base implementation, can be overriden when necessary
    }

    fun openAndParseFile(parseMethod: (input: String) -> Unit): Unit {
        try {
            this.javaClass.getResourceAsStream(inputFile())?.bufferedReader()?.forEachLine { line ->
                parseMethod(line)
            }
        } catch (t: Throwable) {
            logger.error("Exception occured when opening file ${inputFile()}", t)
        }
    }
}
