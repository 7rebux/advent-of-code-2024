package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day22 : Day<Long>(22, "Monkey Market") {

  override fun partOne(input: List<String>): Long {
    return input
      .sumOf {
        generateSecretNumbers(it.toLong())
          .drop(2000)
          .first()
      }
  }

  override fun partTwo(input: List<String>): Long {
    val bananas = buildMap {
      input
        .map {
          generateSecretNumbers(it.toLong())
            .take(2001)
            .map { it % 10 }
        }
        .forEach { sequence ->
          sequence
            .windowed(5, 1)
            .map {
              Pair(
                it.zipWithNext(Long::minus),
                it.last()
              )
            }
            .distinctBy { it.first }
            .forEach { (key, value) ->
              this[key] = this.getOrDefault(key, 0L) + value
            }
        }
    }

    return bananas.maxOf { it.value }
  }

  private fun generateSecretNumbers(seed: Long): Sequence<Long> {
    return generateSequence(seed) {
      it.xor(it shl 6).prune()
        .let { it.xor(it shr 5).prune() }
        .let { it.xor(it shl 11).prune() }
    }
  }

  private fun Long.prune() = this % 16777216L

  override val partOneTestExamples: Map<List<String>, Long> = mapOf(
    listOf(
      "1",
      "10",
      "100",
      "2024",
    ) to 37327623
  )

  override val partTwoTestExamples: Map<List<String>, Long> = mapOf(
    listOf(
      "1",
      "2",
      "3",
      "2024",
    ) to 23
  )
}
