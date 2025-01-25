package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day18 : Day<Int, String>(18, "RAM Run") {

  // For example input: Point(6, 6)
  private val goal = Point(70, 70)

  override fun partOne(input: List<String>): Int {
    return input
      .let(::parseInput).take(1024) // For example input: 12
      .let(::minStepsToGoal)
      ?: -1
  }

  override fun partTwo(input: List<String>): String {
    val corrupted = parseInput(input)
    val first = generateSequence(1, Int::inc)
      .takeWhile {
        it.let(corrupted::take).let(::minStepsToGoal) != null
      }
      .last()
      .let(corrupted::get)

    return "${first.x},${first.y}"
  }

  private fun parseInput(input: List<String>): List<Point> {
    return input.map {
      it
        .split(",")
        .let { Point(it[0].toInt(), it[1].toInt()) }
    }
  }

  private fun minStepsToGoal(corrupted: List<Point>): Int? {
    val queue = mutableListOf(Point(0, 0) to 0)
    val visited = mutableSetOf<Point>()

    while (queue.isNotEmpty()) {
      val (current, steps) = queue.removeFirst()

      if (current == goal) {
        return steps
      }

      // Only add neighbors if we did not already queue them
      if (visited.add(current)) {
        val neighbors = current
          .neighbors()
          .filter {
            it !in corrupted
              && it.x in 0..goal.x
              && it.y in 0..goal.y
          }
          .map { it to steps + 1 }

        queue.addAll(neighbors)
      }
    }

    return null
  }

  private data class Point(val x: Int, val y: Int)

  private fun Point.neighbors() = buildSet {
    add(Point(x + 1, y))
    add(Point(x, y - 1))
    add(Point(x - 1, y))
    add(Point(x, y + 1))
  }

  private val exampleInput = listOf(
    "5,4",
    "4,2",
    "4,5",
    "3,0",
    "2,1",
    "6,3",
    "2,4",
    "1,5",
    "0,6",
    "3,3",
    "2,6",
    "5,1",
    "1,2",
    "5,5",
    "2,5",
    "6,5",
    "1,4",
    "0,4",
    "6,4",
    "1,1",
    "6,1",
    "1,0",
    "0,5",
    "1,6",
    "2,0",
  )

  override val partOneTestExamples: Map<List<String>, Int> = mapOf(
    // exampleInput to 22
  )

  override val partTwoTestExamples: Map<List<String>, String> = mapOf(
    // exampleInput to "6,1"
  )
}
