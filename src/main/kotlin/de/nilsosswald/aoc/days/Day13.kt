package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day13 : Day<Long>(13, "Claw Contraption") {

  override fun partOne(input: List<String>) = parseClawMachines(input).sumOf(::calculateMinimumTokens)

  override fun partTwo(input: List<String>): Long {
    val offset = 10000000000000

    return parseClawMachines(input).sumOf { clawMachine ->
      val modified = clawMachine.copy(
        prize = Point(
          clawMachine.prize.x + offset,
          clawMachine.prize.y + offset
        )
      )

      calculateMinimumTokens(modified)
    }
  }

  private fun parseClawMachines(input: List<String>) : List<ClawMachine> {
    return input
      .filter(String::isNotEmpty)
      .chunked(3)
      .map { raw ->
        val values = raw.map(::extractNumbers)

        ClawMachine(
          Point(values[0][0], values[0][1]),
          Point(values[1][0], values[1][1]),
          Point(values[2][0], values[2][1]),
        )
      }
  }

  private fun extractNumbers(line: String): List<Long> {
    return Regex("\\d+")
      .findAll(line)
      .map { it.value.toLong() }
      .toList()
  }

  // https://www.reddit.com/r/adventofcode/comments/1hd7irq/2024_day_13_an_explanation_of_the_mathematics/
  private fun calculateMinimumTokens(clawMachine: ClawMachine) : Long {
    val det = clawMachine.buttonA.x * clawMachine.buttonB.y - clawMachine.buttonA.y * clawMachine.buttonB.x
    val a = (clawMachine.prize.x * clawMachine.buttonB.y - clawMachine.prize.y * clawMachine.buttonB.x) / det
    val b = (clawMachine.buttonA.x * clawMachine.prize.y - clawMachine.buttonA.y * clawMachine.prize.x) / det

    val valid = clawMachine.buttonA.x * a + clawMachine.buttonB.x * b == clawMachine.prize.x
      && clawMachine.buttonA.y * a + clawMachine.buttonB.y * b == clawMachine.prize.y

    return if (valid) a * 3 + b else 0
  }

  private data class ClawMachine(val buttonA: Point, val buttonB: Point, val prize: Point)

  private data class Point(val x: Long, val y: Long)

  override val partOneTestExamples: Map<List<String>, Long> = mapOf(
    listOf(
      "Button A: X+94, Y+34",
      "Button B: X+22, Y+67",
      "Prize: X=8400, Y=5400",
      "",
      "Button A: X+26, Y+66",
      "Button B: X+67, Y+21",
      "Prize: X=12748, Y=12176",
      "",
      "Button A: X+17, Y+86",
      "Button B: X+84, Y+37",
      "Prize: X=7870, Y=6450",
      "",
      "Button A: X+69, Y+23",
      "Button B: X+27, Y+71",
      "Prize: X=18641, Y=10279",
    ) to 480
  )

  override val partTwoTestExamples: Map<List<String>, Long> = emptyMap()
}
