package com.github.mrhoeve.adventofcode.year2022

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(8)
@Component
class Day08() : GameBase2022() {
    private val forest: MutableList<MutableList<Tree>> = mutableListOf()

    init {
        loadInput()
    }

    override fun solvePartOne() {
        logger.info("There are ${forest.flatten().count { it.isVisible }} trees visible")
    }

    override fun solvePartTwo() {
        logger.info("The highest possible scenic score is ${forest.flatten().maxOf { it.scenicScore }}")
    }

    override fun inputFile(): String {
        return "/2022/input_08.txt"
    }

    fun loadInput() {
        openAndParseFile(this::parseLine)
        processForestVisibility()
    }

    private fun parseLine(line: String) {
        if (line.isNotBlank()) {
            forest.add(line.chunked(1).map { Tree(it.toInt()) }.toMutableList())
        }
    }

    private fun processForestVisibility() {
        forest.forEachIndexed { index, rowOfTrees ->
            when (index) {
                0, forest.size - 1 -> rowOfTrees.forEach { it.isVisible = true }
                else -> processRowOfTrees(rowOfTrees, index)
            }
        }
    }

    private fun processRowOfTrees(rowOfTrees: MutableList<Tree>, currentIndexOfRowOfTrees: Int) {
        rowOfTrees.forEachIndexed { index, tree ->
            when (index) {
                0, rowOfTrees.size - 1 -> tree.isVisible = true
                else -> checkVisibilityOfTree(tree, currentIndexOfRowOfTrees, index)
            }
        }
    }

    private fun checkVisibilityOfTree(currentTree: Tree, currentRowIndex: Int, currentTreeIndex: Int) {
        val currentTreeHeight = currentTree.height
        var scenicScore: Int = 0
        // check columns above
        val treesAbove = forest.subList(0, currentRowIndex).map { it[currentTreeIndex] }.reversed()
        if (treesAbove.all { it.height < currentTreeHeight }) currentTree.isVisible = true
        scenicScore =
            if (treesAbove.indexOfFirst { it.height >= currentTreeHeight } != -1) treesAbove.indexOfFirst { it.height >= currentTreeHeight } + 1 else currentRowIndex
        // check left
        val treesLeft = forest[currentRowIndex].subList(0, currentTreeIndex).reversed()
        if (treesLeft.all { it.height < currentTreeHeight }) currentTree.isVisible = true
        scenicScore *=
            if (treesLeft.indexOfFirst { it.height >= currentTreeHeight } != -1) (treesLeft.indexOfFirst { it.height >= currentTreeHeight } + 1) else treesLeft.size
        // check right
        val treesRight = forest[currentRowIndex].subList(currentTreeIndex + 1, forest[currentRowIndex].size)
        if (treesRight.all { it.height < currentTreeHeight }) currentTree.isVisible = true
        scenicScore *=
            if (treesRight.indexOfFirst { it.height >= currentTreeHeight } != -1) (treesRight.indexOfFirst { it.height >= currentTreeHeight } + 1) else treesRight.size
        // check below
        val treesBelow = forest.subList(currentRowIndex + 1, forest.size).map { it[currentTreeIndex] }
        if (treesBelow.all { it.height < currentTreeHeight }) currentTree.isVisible = true
        scenicScore *=
            if (treesBelow.indexOfFirst { it.height >= currentTreeHeight } != -1) (treesBelow.indexOfFirst { it.height >= currentTreeHeight } + 1) else treesBelow.size
        currentTree.scenicScore = scenicScore
    }

    data class Tree(
        val height: Int,
        var isVisible: Boolean = false,
        var scenicScore: Int = 0
    )
}
