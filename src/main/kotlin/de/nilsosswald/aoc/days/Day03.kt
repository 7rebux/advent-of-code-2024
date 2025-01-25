package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day03 : Day<Int, Int>(3, "Mull It Over") {

  private val regex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")

  override fun partOne(input: List<String>): Int {
    return regex.findAll(input.joinToString())
      .map(MatchResult::destructured)
      .sumOf { (a, b) -> a.toInt() * b.toInt() }
  }

  override fun partTwo(input: List<String>): Int {
    return with(input.joinToString()) {
      regex.findAll(this)
        .filterNot { match ->
          this.lastIndexOf("don't()", match.range.first) > this.lastIndexOf("do()", match.range.first)
        }
        .map(MatchResult::destructured)
        .sumOf { (a, b) -> a.toInt() * b.toInt() }
    }
  }

  override val partOneTestExamples: Map<List<String>, Int> = mapOf(
    listOf("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))") to 161
  )

  override val partTwoTestExamples: Map<List<String>, Int> = mapOf(
    listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))") to 48
  )
}
