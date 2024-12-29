package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day10 : Day<Int>(10, "Hoof It") {

  override fun partOne(input: List<String>): Int {
    return findTrailheads(input).sumOf { trailhead ->
      findHikingTrailEnds(trailhead, input)
        .distinct()
        .size
    }
  }

  override fun partTwo(input: List<String>): Int {
    return findTrailheads(input).sumOf { trailhead ->
      findHikingTrailEnds(trailhead, input).size
    }
  }

  private fun findTrailheads(input: List<String>): List<Point> {
    return input.flatMapIndexed { y, line ->
      line.mapIndexedNotNull { x, c ->
        if (c == '0') Point(x, y) else null
      }
    }
  }

  private fun findHikingTrailEnds(
    start: Point,
    input: List<String>,
    currentHeight: Int = 0
  ): List<Point> {
    if (currentHeight == 9) {
      return listOf(start)
    }

    return start
      .neighbors()
      .filter { it in input && input[it].digitToIntOrNull() == currentHeight + 1 }
      .flatMap { findHikingTrailEnds(it, input, currentHeight + 1) }
  }

  private operator fun List<String>.contains(point: Point) =
    point.y in this.indices && point.x in this[0].indices

  @Suppress("Unused")
  private operator fun List<String>.get(point: Point) =
    this[point.y][point.x]

  private fun Point.neighbors() = buildSet {
    add(Point(x + 1, y))
    add(Point(x, y - 1))
    add(Point(x - 1, y))
    add(Point(x, y + 1))
  }

  private data class Point(val x: Int, val y: Int)

  override val partOneTestExamples: Map<List<String>, Int> = mapOf(
    listOf(
      "0123",
      "1234",
      "8765",
      "9876",
    ) to 1,

    listOf(
      "...0...",
      "...1...",
      "...2...",
      "6543456",
      "7.....7",
      "8.....8",
      "9.....9",
    ) to 2,

    listOf(
      "..90..9",
      "...1.98",
      "...2..7",
      "6543456",
      "765.987",
      "876....",
      "987....",
    ) to 4,

    listOf(
      "10..9..",
      "2...8..",
      "3...7..",
      "4567654",
      "...8..3",
      "...9..2",
      ".....01",
    ) to 3,

    listOf(
      "89010123",
      "78121874",
      "87430965",
      "96549874",
      "45678903",
      "32019012",
      "01329801",
      "10456732",
    ) to 36
  )

  override val partTwoTestExamples: Map<List<String>, Int> = mapOf(
    listOf(
      ".....0.",
      "..4321.",
      "..5..2.",
      "..6543.",
      "..7..4.",
      "..8765.",
      "..9....",
    ) to 3,

    listOf(
      "..90..9",
      "...1.98",
      "...2..7",
      "6543456",
      "765.987",
      "876....",
      "987....",
    ) to 13,

    listOf(
      "012345",
      "123456",
      "234567",
      "345678",
      "4.6789",
      "56789.",
    ) to 227,

    listOf(
      "89010123",
      "78121874",
      "87430965",
      "96549874",
      "45678903",
      "32019012",
      "01329801",
      "10456732",
    ) to 81,
  )
}
