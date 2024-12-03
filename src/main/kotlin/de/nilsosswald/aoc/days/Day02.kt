package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day
import kotlin.collections.all
import kotlin.collections.map
import kotlin.collections.zipWithNext
import kotlin.math.abs
import kotlin.math.sign

object Day02 : Day<Int>(2, "Red-Nosed Reports") {

  override fun partOne(input: List<String>): Int {
    return input
      .map { line -> line.split(" ").map(String::toInt) }
      .count(::isSafe)
  }

  override fun partTwo(input: List<String>): Int {
    return input
      .map { line -> line.split(" ").map(String::toInt) }
      .count { levels ->
        levels.indices
          .asSequence()
          .map { i ->
            levels.toMutableList().apply { this.removeAt(i) }
          }
          .any(::isSafe)
      }
  }

  private fun isSafe(levels: List<Int>): Boolean {
    return levels
      .zipWithNext { a, b -> a - b }
      .let { diffs ->
        diffs.map(Int::sign).distinct().size == 1
          && diffs.map(::abs).all { x -> x in 1..3 }
      }
  }

  override val partOneTestExamples: Map<List<String>, Int> = mapOf(
    listOf(
      "7 6 4 2 1",
      "1 2 7 8 9",
      "9 7 6 2 1",
      "1 3 2 4 5",
      "8 6 4 4 1",
      "1 3 6 7 9"
    ) to 2
  )

  override val partTwoTestExamples: Map<List<String>, Int> = mapOf(
    listOf(
      "7 6 4 2 1",
      "1 2 7 8 9",
      "9 7 6 2 1",
      "1 3 2 4 5",
      "8 6 4 4 1",
      "1 3 6 7 9"
    ) to 4
  )
}
