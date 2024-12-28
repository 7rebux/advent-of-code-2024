package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day19 : Day<Long>(19, "Linen Layout") {

  override fun partOne(input: List<String>): Long {
    val (patterns, designs) = parseInput(input)
    return designs.count { countWays(it, patterns) > 0 }.toLong()
  }

  override fun partTwo(input: List<String>): Long {
    val (patterns, designs) = parseInput(input)
    return designs.sumOf { countWays(it, patterns) }
  }

  private fun parseInput(input: List<String>) = Pair(
    input.first().split(", "),
    input.drop(2)
  )

  private fun countWays(
    design: String,
    patterns: List<String>,
    cache: MutableMap<String, Long> = mutableMapOf()
  ): Long {
    if (design.isBlank()) {
      return 1
    }

    val foundPatterns = design.indices
      .asSequence()
      .map { i -> design.substring(0, i + 1) }
      .filter(patterns::contains)

    return cache.getOrPut(design) {
      foundPatterns.sumOf {
        countWays(design.removePrefix(it), patterns, cache)
      }
    }
  }

  private val exampleInput = listOf(
    "r, wr, b, g, bwu, rb, gb, br",
    "",
    "brwrr",
    "bggr",
    "gbbr",
    "rrbgbr",
    "ubwu",
    "bwurrg",
    "brgr",
    "bbrgwb",
  )

  override val partOneTestExamples: Map<List<String>, Long> = mapOf(
    exampleInput to 6
  )

  override val partTwoTestExamples: Map<List<String>, Long> = mapOf(
    exampleInput to 16
  )
}
