package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day
import kotlin.text.digitToInt

object Day09 : Day<Long>(9, "Disk Fragmenter") {

  override fun partOne(input: List<String>): Long {
    val disk = parseInput(input)
    val blocks = disk.flatten()
    val emptyBlocks = blocks.indices.filter { blocks[it] == null }.toMutableList()

    return blocks.withIndex().reversed().sumOf { (index, value) ->
      if (value != null) {
        value * (emptyBlocks.removeFirstOrNull() ?: index)
      } else {
        emptyBlocks.removeLastOrNull()
        0
      }
    }
  }

  override fun partTwo(input: List<String>): Long {
    val originalDisk = parseInput(input)
    val sortedDisk = originalDisk.toMutableList()

    originalDisk.reversed().forEach { space ->
      if (space is Free) {
        return@forEach
      }

      val currentIndex = sortedDisk.indexOf(space)
      val (foundIndex, foundSpace) = sortedDisk
        .withIndex()
        .firstOrNull { (i, s) ->
          s is Free
            && s.size >= space.size
            && i <= currentIndex
        } ?: return@forEach
      val sizeDelta = foundSpace.size - space.size

      sortedDisk[foundIndex] = space
      sortedDisk[currentIndex] = Free(space.size)

      if (sizeDelta > 0) {
        sortedDisk.add(foundIndex + 1, Free(sizeDelta))
      }
    }

    return sortedDisk
      .flatten()
      .withIndex()
      .filter { (_, x) -> x != null }
      .sumOf { (pos, id) -> pos * id!! }
  }

  private fun parseInput(input: List<String>): List<Space> {
    var id = 0L

    return input
      .first()
      .foldIndexed(mutableListOf<Space>()) { i, acc, ch ->
        val size = ch.digitToInt()
        acc += if (i % 2 == 0) File(id++, size) else Free(size)
        return@foldIndexed acc
      }
  }

  private fun List<Space>.flatten() = this.flatMap { space ->
    List(space.size) {
      when (space) {
        is File -> space.id
        is Free -> null
      }
    }
  }

  private sealed class Space(val size: Int)

  private class Free(size: Int) : Space(size)

  private class File(val id: Long, size: Int) : Space(size)

  override val partOneTestExamples: Map<List<String>, Long> = mapOf(
    listOf("2333133121414131402") to 1928
  )

  override val partTwoTestExamples: Map<List<String>, Long> = mapOf(
    listOf("2333133121414131402") to 2858
  )
}
