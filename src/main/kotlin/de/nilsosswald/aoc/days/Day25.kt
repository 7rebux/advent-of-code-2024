package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day25 : Day<Int, Nothing?>(25, "Code Chronicle") {

  override fun partOne(input: List<String>): Int {
    val (locks, keys) = parseInput(input)

    return locks.sumOf { lock ->
      keys.count { key ->
        key
          .zip(lock)
          .map { (a, b) -> a + b }
          .none { it > 5 }
      }
    }
  }

  private fun parseInput(input: List<String>): List<List<List<Int>>> {
    return input
      .filter(String::isNotBlank)
      .chunked(7)
      .partition { schematic ->
        schematic.first().all { it == '#' }
      }
      .let { (locks, keys) ->
        listOf(
          locks.map { it.drop(1) },
          keys.map { it.dropLast(1) }
        )
      }
      .map { schematics ->
        schematics.map { schematic ->
          schematic.first().indices.map { x ->
            schematic.count { it[x] == '#' }
          }
        }
      }
  }

  override val partOneTestExamples: Map<List<String>, Int> = mapOf(
    listOf(
      "#####",
      ".####",
      ".####",
      ".####",
      ".#.#.",
      ".#...",
      ".....",
      "",
      "#####",
      "##.##",
      ".#.##",
      "...##",
      "...#.",
      "...#.",
      ".....",
      "",
      ".....",
      "#....",
      "#....",
      "#...#",
      "#.#.#",
      "#.###",
      "#####",
      "",
      ".....",
      ".....",
      "#.#..",
      "###..",
      "###.#",
      "###.#",
      "#####",
      "",
      ".....",
      ".....",
      ".....",
      "#....",
      "#.#..",
      "#.#.#",
      "#####",
    ) to 3
  )

  override fun partTwo(input: List<String>) = null

  override val partTwoTestExamples: Map<List<String>, Nothing?> = emptyMap()
}
