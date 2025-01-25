package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day11 : Day<Long, Long>(11, "Plutonian Pebbles") {

  override fun partOne(input: List<String>): Long {
    return input
      .let(::parseInput)
      .let { stones -> countStonesAfterBlinks(stones, 25)}
  }

  override fun partTwo(input: List<String>): Long {
    return input
      .let(::parseInput)
      .let { stones -> countStonesAfterBlinks(stones, 75)}
  }

  private fun parseInput(input: List<String>): Map<Long, Long> {
    return input
      .first()
      .split(" ")
      .map(String::toLong)
      // Because .groupingBy { it }.eachCount() is hard typed to Map<K, Int>
      .groupBy { it }
      .mapValues { (_, values) -> values.size.toLong() }
  }

  private fun countStonesAfterBlinks(initial: Map<Long, Long>, blinks: Int): Long {
    return generateSequence(initial, ::blink)
      .take(blinks + 1)
      .last()
      .values
      .sum()
  }

  private fun blink(stones: Map<Long, Long>): Map<Long, Long> {
    return stones.entries
      .flatMap { (stone, count) ->
        stone
          .let(::transformStone)
          .map { it to count }
      }
      .groupingBy { it.first }
      .fold(0) { acc, (_, count) -> acc + count }
  }

  private fun transformStone(stone: Long): List<Long>{
    return when {
      stone == 0L -> listOf(1)
      stone.toString().length % 2 == 0 -> {
        stone.toString()
          .let { it.chunked(it.length / 2) }
          .map(String::toLong)
      }
      else -> listOf(stone * 2024)
    }
  }

  override val partOneTestExamples: Map<List<String>, Long> = mapOf(
    listOf("125 17") to 55312
  )

  override val partTwoTestExamples: Map<List<String>, Long> = emptyMap()
}
