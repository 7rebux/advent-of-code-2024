package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day04 : Day<Int>(4, "Ceres Search") {

  override fun partOne(input: List<String>): Int {
    return input.flatMapIndexed { y, line ->
      line.mapIndexedNotNull { x, c ->
        if (c == 'X') x to y else null
      }
    }.sumOf { point ->
      Direction.entries.count { direction ->
        point.charInDirection(direction, input, 1) == 'M'
          && point.charInDirection(direction, input, 2) == 'A'
          && point.charInDirection(direction, input, 3) == 'S'
      }
    }
  }

  override fun partTwo(input: List<String>): Int {
    return input.flatMapIndexed { y, line ->
      line.mapIndexedNotNull { x, c ->
        if (c == 'A') x to y else null
      }
    }.map { point ->
      listOf(
        point.charInDirection(Direction.NW, input),
        point.charInDirection(Direction.NE, input),
        point.charInDirection(Direction.SW, input),
        point.charInDirection(Direction.SE, input)
      ).joinToString("")
    }.count { it in listOf("MSMS", "SMSM", "SSMM", "MMSS") }
  }

  private enum class Direction { N, E, S, W, NE, SE, SW, NW }

  private fun Pair<Int, Int>.charInDirection(
    direction: Direction,
    input: List<String>,
    step: Int = 1
  ): Char? {
    val (x, y) = this
    val point = when (direction) {
      Direction.N -> Pair(x, y - step)
      Direction.E -> Pair(x + step, y)
      Direction.S -> Pair(x, y + step)
      Direction.W -> Pair(x - step, y)
      Direction.NE -> Pair(x + step, y - step)
      Direction.SE -> Pair(x + step, y + step)
      Direction.SW -> Pair(x - step, y + step)
      Direction.NW -> Pair(x - step, y - step)
    }

    return input.getOrNull(point.second)?.getOrNull(point.first)
  }

  override val partOneTestExamples: Map<List<String>, Int> = mapOf(
    listOf(
      "MMMSXXMASM",
      "MSAMXMSMSA",
      "AMXSXMAAMM",
      "MSAMASMSMX",
      "XMASAMXAMM",
      "XXAMMXXAMA",
      "SMSMSASXSS",
      "SAXAMASAAA",
      "MAMMMXMMMM",
      "MXMXAXMASX",
    ) to 18
  )

  override val partTwoTestExamples: Map<List<String>, Int> = mapOf(
    listOf(
      "MMMSXXMASM",
      "MSAMXMSMSA",
      "AMXSXMAAMM",
      "MSAMASMSMX",
      "XMASAMXAMM",
      "XXAMMXXAMA",
      "SMSMSASXSS",
      "SAXAMASAAA",
      "MAMMMXMMMM",
      "MXMXAXMASX",
    ) to 9
  )
}
