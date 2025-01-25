package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day06 : Day<Int, Int>(6, "Guard Gallivant") {

  override fun partOne(input: List<String>): Int {
    return walk(input)
      .distinctBy(PathEntry::point)
      .count()
  }

  override fun partTwo(input: List<String>): Int {
    val start = getStartPoint(input)
    val possibleObstaclePoints = walk(input, start)
      .map(PathEntry::point)
      .toSet()

    return possibleObstaclePoints.count { obstacle ->
      val seen = mutableSetOf<PathEntry>()

      walk(input, start, obstacle)
        .takeWhile(seen::add)
        .last()
        .let { (dir, point) -> point + dir.offset in input  }
    }
  }

  private fun walk(
    input: List<String>,
    start: Point = getStartPoint(input),
    extraObstacle: Point? = null
  ): Sequence<PathEntry> {
    return generateSequence(PathEntry(Direction.North, start)) { (dir, current) ->
      val next = current + dir.offset

      when {
        next !in input -> null
        input[next] == '#' || next == extraObstacle -> PathEntry(dir.nextClockwise(), current)
        else -> PathEntry(dir, next)
      }
    }
  }

  private data class Point(val x: Int, val y: Int)

  private enum class Direction(val offset: Point) {
    North(Point(0, -1)),
    East(Point(1, 0)),
    South(Point(0, 1)),
    West(Point(-1, 0));

    fun nextClockwise() = entries[(ordinal + 1) % entries.size]
  }

  private data class PathEntry(val direction: Direction, val point: Point)

  private fun getStartPoint(input: List<String>): Point {
    val y = input.indexOfFirst { line -> line.contains('^') }
    val x = input[y].indexOf('^')

    return Point(x, y)
  }

  private operator fun Point.plus(other: Point) =
    Point(x + other.x, y + other.y)

  @Suppress("Unused")
  private operator fun List<String>.get(point: Point) =
    this[point.y][point.x]

  private operator fun List<String>.contains(point: Point) =
    point.y in this.indices && point.x in this[0].indices

  override val partOneTestExamples: Map<List<String>, Int> = mapOf(
    listOf(
      "....#.....",
      ".........#",
      "..........",
      "..#.......",
      ".......#..",
      "..........",
      ".#..^.....",
      "........#.",
      "#.........",
      "......#..."
    ) to 41
  )

  override val partTwoTestExamples: Map<List<String>, Int> = mapOf(
    listOf(
      "....#.....",
      ".........#",
      "..........",
      "..#.......",
      ".......#..",
      "..........",
      ".#..^.....",
      "........#.",
      "#.........",
      "......#..."
    ) to 6
  )
}
