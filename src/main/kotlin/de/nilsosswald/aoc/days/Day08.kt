package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day08 : Day<Int>(8, "Resonant Collinearity") {

  override fun partOne(input: List<String>): Int {
    return parseInput(input)
      .flatMap { (_, points) ->
        points.flatMap { a ->
          points.minus(a).flatMap { b ->
            val delta = b - a
            listOf(a - delta, b + delta)
          }
        }
      }
      .distinct()
      .count { it in input }
  }

  override fun partTwo(input: List<String>): Int {
    return parseInput(input)
      .flatMap { (_, points) ->
        points.flatMap { a ->
          points.minus(a).flatMap { b ->
            val delta = b - a

            buildList {
              generateSequence(a) { it - delta }
                .takeWhile { node -> node in input }
                .let(::addAll)
              generateSequence(b) { it + delta }
                .takeWhile { node -> node in input }
                .let(::addAll)
            }
          }
        }
      }
      .distinct()
      .count()
  }

  private fun parseInput(input: List<String>): Map<Char, List<Point>> {
    return input
      .flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
          when (c) {
            '.', '#' -> null
            else -> c to Point(x, y)
          }
        }
      }
      .groupBy({ it.first }, { it.second })
  }

  private operator fun List<String>.contains(point: Point) =
    point.y in indices && point.x in this[point.y].indices

  private operator fun Point.minus(other: Point) =
    Point(x - other.x, y - other.y)

  private operator fun Point.plus(other: Point) =
    Point(x + other.x, y + other.y)

  private data class Point(val x: Int, val y: Int)

  override val partOneTestExamples: Map<List<String>, Int> = mapOf(
    listOf(
      "..........",
      "...#......",
      "..........",
      "....a.....",
      "..........",
      ".....a....",
      "..........",
      "......#...",
      "..........",
      "..........",
    ) to 2,

    listOf(
      "..........",
      "...#......",
      "#.........",
      "....a.....",
      "........a.",
      ".....a....",
      "..#.......",
      "......#...",
      "..........",
      "..........",
    ) to 4,

    listOf(
      "..........",
      "...#......",
      "#.........",
      "....a.....",
      "........a.",
      ".....a....",
      "..#.......",
      "......A...",
      "..........",
      "..........",
    ) to 4,

    listOf(
      "......#....#",
      "...#....0...",
      "....#0....#.",
      "..#....0....",
      "....0....#..",
      ".#....A.....",
      "...#........",
      "#......#....",
      "........A...",
      ".........A..",
      "..........#.",
      "..........#.",
    ) to 14
  )

  override val partTwoTestExamples: Map<List<String>, Int> = mapOf(
    listOf(
      "T....#....",
      "...T......",
      ".T....#...",
      ".........#",
      "..#.......",
      "..........",
      "...#......",
      "..........",
      "....#.....",
      "..........",
    ) to 9,

    listOf(
      "##....#....#",
      ".#.#....0...",
      "..#.#0....#.",
      "..##...0....",
      "....0....#..",
      ".#...#A....#",
      "...#..#.....",
      "#....#.#....",
      "..#.....A...",
      "....#....A..",
      ".#........#.",
      "...#......##",
    ) to 34
  )
}
