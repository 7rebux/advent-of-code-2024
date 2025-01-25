package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day
import kotlin.math.abs

object Day20 : Day<Int, Int>(20, "Race Condition") {

  private const val GOAL = 100

  override fun partOne(input: List<String>): Int {
    return solve(parseInput(input), 2)
  }

  override fun partTwo(input: List<String>): Int {
    return solve(parseInput(input), 20)
  }

  private fun parseInput(input: List<String>): List<Point> {
    val start = input.let {
      val y = it.indexOfFirst { line -> line.contains('S') }
      val x = it[y].indexOf('S')
      Point(x, y)
    }
    val end = input.let {
      val y = it.indexOfFirst { line -> line.contains('E') }
      val x = it[y].indexOf('E')
      Point(x, y)
    }

    return buildList {
      add(start)

      while (this.last() != end) {
        val next = this.last()
          .neighbors()
          .filter { (x, y) -> input[y][x] != '#' }
          .first { it != this.getOrNull(this.lastIndex - 1) }

        add(next)
      }
    }
  }

  private fun solve(path: List<Point>, maxCheatTime: Int): Int {
    return path.indices.sumOf { start ->
      val indices = (start + GOAL)..path.lastIndex
      indices.count { end ->
        val dist = path[start].manhattanDistance(path[end])
        return@count dist <= maxCheatTime && dist <= end - start - GOAL
      }
    }
  }

  private fun Point.manhattanDistance(other: Point) =
    abs(x - other.x) + abs(y - other.y)

  private fun Point.neighbors() = buildSet {
    add(Point(x + 1, y))
    add(Point(x, y - 1))
    add(Point(x - 1, y))
    add(Point(x, y + 1))
  }

  private data class Point(val x: Int, val y: Int)

  // Not properly testable with my tests implementation :(
  override val partOneTestExamples: Map<List<String>, Int> = emptyMap()
  override val partTwoTestExamples: Map<List<String>, Int> = emptyMap()
}
