package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day07 : Day<Long, Long>(7, "Bridge Repair") {

  override fun partOne(input: List<String>): Long {
    return solve(input, Plus, Times)
  }

  override fun partTwo(input: List<String>): Long {
    return solve(input, Plus, Times, Concatenate)
  }

  private fun solve(input: List<String>, vararg operators: Operator): Long {
    return input
      .map { line ->
        Calibration(
          line.split(":")[0].toLong(),
          line.split(": ")[1].split(" ").map(String::toLong)
        )
      }
      .filter { calibration ->
        canBeCalculated(calibration.result, calibration.operands, operators.asList())
      }
      .sumOf(Calibration::result)
  }

  private fun canBeCalculated(
    result: Long,
    operands: List<Long>,
    operators: List<Operator>,
    current: Long = 0
  ): Boolean {
    if (current > result) {
      return false
    }

    if (operands.isEmpty()) {
      return current == result
    }

    val next = operands.first()
    val remaining = operands.drop(1)

    return operators.any { operator ->
      canBeCalculated(result, remaining, operators, operator(current, next))
    }
  }

  private object Plus : Operator {
    override fun invoke(a: Long, b: Long) = a + b
  }

  private object Times : Operator {
    override fun invoke(a: Long, b: Long) = a * b
  }

  private object Concatenate : Operator {
    override fun invoke(a: Long, b: Long) = "$a$b".toLong()
  }

  private data class Calibration(val result: Long, val operands: List<Long>)

  private fun interface Operator: (Long, Long) -> Long

  override val partOneTestExamples: Map<List<String>, Long> = mapOf(
    listOf(
      "190: 10 19",
      "3267: 81 40 27",
      "83: 17 5",
      "156: 15 6",
      "7290: 6 8 6 15",
      "161011: 16 10 13",
      "192: 17 8 14",
      "21037: 9 7 18 13",
      "292: 11 6 16 20"
    ) to 3749
  )

  override val partTwoTestExamples: Map<List<String>, Long> = mapOf(
    listOf(
      "190: 10 19",
      "3267: 81 40 27",
      "83: 17 5",
      "156: 15 6",
      "7290: 6 8 6 15",
      "161011: 16 10 13",
      "192: 17 8 14",
      "21037: 9 7 18 13",
      "292: 11 6 16 20"
    ) to 11387
  )
}
