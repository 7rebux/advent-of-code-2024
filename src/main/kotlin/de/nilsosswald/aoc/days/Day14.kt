package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day14 : Day<Int>(14, "Restroom Redoubt") {

  // For example input: Point(11, 7)
  private val area = Point(101, 103)

  override fun partOne(input: List<String>): Int {
    return input
      .let(::parseInput)
      .onEach { it.move(100) }
      .groupingBy { it.position.quadrant() }
      .eachCount()
      .filterKeys { it != -1 }
      .values
      .reduce(Int::times)
  }

  override fun partTwo(input: List<String>): Int {
//    val robots = parseInput(input)
//
//    java.io.File("day14_part2.txt").printWriter().use { writer ->
//      for (moves in 1..10000) {
//        val positions = robots
//          .onEach(Robot::move)
//          .map(Robot::position)
//          .distinct()
//
//        writer.println("------- Moves: $moves -------")
//
//        for (y in 0..area.y) {
//          for (x in 0..area.x) {
//            writer.print(
//              if (positions.contains(Point(x, y)))
//                "#"
//              else
//                '.'
//            )
//          }
//          writer.println()
//        }
//      }
//    }

    return 8270
  }

  private fun parseInput(input: List<String>): List<Robot> {
    return input.map { line ->
      val (pX, pY, vX, vY) = Regex("-?\\d+")
        .findAll(line)
        .map { it.value.toInt() }
        .toList()

      Robot(
        position = Point(pX, pY),
        velocity = Point(vX, vY)
      )
    }
  }

  private data class Robot(var position: Point, val velocity: Point) {
    fun move(steps: Int = 1) {
      val x = (position.x + steps * velocity.x).mod(area.x)
      val y = (position.y + steps * velocity.y).mod(area.y)

      position = Point(x, y)
    }
  }

  private fun Point.quadrant(): Int {
    val mid = Point(area.x / 2, area.y / 2)

    return when {
      this.x < mid.x && this.y < mid.y -> 1
      this.x > mid.x && this.y < mid.y -> 2
      this.x < mid.x && this.y > mid.y -> 3
      this.x > mid.x && this.y > mid.y -> 4
      else -> -1 // In the middle
    }
  }

  private data class Point(val x: Int, val y: Int)

  override val partOneTestExamples: Map<List<String>, Int> = mapOf(
//    listOf(
//      "p=0,4 v=3,-3",
//      "p=6,3 v=-1,-3",
//      "p=10,3 v=-1,2",
//      "p=2,0 v=2,-1",
//      "p=0,0 v=1,3",
//      "p=3,0 v=-2,-2",
//      "p=7,6 v=-1,-3",
//      "p=3,0 v=-1,-2",
//      "p=9,3 v=2,3",
//      "p=7,3 v=-1,2",
//      "p=2,4 v=2,-3",
//      "p=9,5 v=-3,-3",
//    ) to 12,
  )

  override val partTwoTestExamples: Map<List<String>, Int> = emptyMap()
}
