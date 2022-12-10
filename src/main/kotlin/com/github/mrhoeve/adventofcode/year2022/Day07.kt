package com.github.mrhoeve.adventofcode.year2022

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(7)
@Component
class Day07() : GameBase2022() {
    private var rootFolder = Folder("/")
    private var currentFolder = rootFolder
    private var result: MutableList<Folder> = mutableListOf()

    private val regex_cd = Regex("\\$ cd (.+)")
    private val regex_dir = Regex("dir (.+)")
    private val regex_file = Regex("(\\d+) (.+)")

    override fun solvePartOne() {
        reset()
        loadInputPartOne()
        val maximumSize: Long = 100000
        findAllFolderWithMaximumSize(maximumSize, rootFolder)
        logger.info("Total size of all folders with a maximumsize of $maximumSize is ${result.sumOf { it.folderSize() }}")
    }

    override fun solvePartTwo() {
        reset()
        loadInputPartTwo()
        val minimumSize: Long = 30000000 - calculateFreeSpace(70000000)
        findAllFolderWithMinimumSize(minimumSize, rootFolder)
        logger.info(
            "Size of the smallest folder with a minimumsize of $minimumSize is ${
                result.minBy { it.folderSize() }.folderSize()
            }"
        )
    }

    override fun inputFile(): String {
        return "/2022/input_07.txt"
    }

    fun loadInputPartOne() {
        openAndParseFile(this::parseLinePartOne)
    }

    fun loadInputPartTwo() {
        openAndParseFile(this::parseLinePartTwo)
    }

    private fun parseLinePartOne(line: String) {
        if (line.isNotBlank()) {
            if (line.startsWith("$")) {
                processCommand(line)
            } else {
                processFolderOrFile(line)
            }
        }
    }

    fun parseLinePartTwo(line: String) {
        if (line.isNotBlank()) {
            if (line.startsWith("$")) {
                processCommand(line)
            } else {
                processFolderOrFile(line)
            }
        }
    }

    private fun processCommand(line: String) {
        regex_cd.find(line)?.let { found ->
            val (cdToFolder) = found.destructured
            currentFolder = when (cdToFolder) {
                "/" -> rootFolder
                ".." -> {
                    currentFolder.parent?.let {
                        it
                    } ?: run {
                        rootFolder
                    }
                }

                else -> {
                    currentFolder.subfolders.firstOrNull { it.name == cdToFolder } ?: run {
                        throw RuntimeException("Unsupported command: $line")
                    }
                }
            }
        }
    }

    private fun processFolderOrFile(line: String) {
        regex_dir.find(line)?.let { foundFolder ->
            val (foldername) = foundFolder.destructured
            currentFolder.subfolders.add(Folder(foldername, currentFolder))
        }
        regex_file.find(line)?.let { foundFile ->
            val (size, name) = foundFile.destructured
            currentFolder.files.add(File(name, size.toLong()))
        }
    }

    private fun findAllFolderWithMaximumSize(maximumSize: Long, folder: Folder) {
        if (folder.folderSize() <= maximumSize) result.add(folder)
        folder.subfolders.forEach { findAllFolderWithMaximumSize(maximumSize, it) }
    }

    private fun findAllFolderWithMinimumSize(minimumSize: Long, folder: Folder) {
        if (folder.folderSize() >= minimumSize) result.add(folder)
        folder.subfolders.forEach { findAllFolderWithMinimumSize(minimumSize, it) }
    }

    private fun calculateFreeSpace(totalDiskSpace: Long): Long {
        return totalDiskSpace - rootFolder.folderSize()
    }

    private fun reset() {
        rootFolder.files.clear()
        rootFolder.subfolders.clear()
        result.clear()
    }

    data class Folder(
        val name: String,
        val parent: Folder? = null,
        val subfolders: MutableList<Folder> = mutableListOf(),
        val files: MutableList<File> = mutableListOf()
    ) {

        fun folderSize(): Long {
            return files.sumOf { it.length }.plus(subfolders.sumOf { it.folderSize() })
        }

        override fun toString(): String {
            return "Folder(name='$name', parent=${parent?.name}, subfolders=$subfolders, files=$files)"
        }
    }

    data class File(
        val name: String,
        val length: Long
    )
}
