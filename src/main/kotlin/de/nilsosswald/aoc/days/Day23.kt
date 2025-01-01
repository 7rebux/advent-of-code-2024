package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day23 : Day<String>(23, "LAN Party") {

  override fun partOne(input: List<String>): String {
    val graph = parseInput(input)

    return graph.keys
      .flatMap { node ->
        val neighbors = graph[node].orEmpty()

        neighbors.flatMap { neighbor1 ->
          neighbors.mapNotNull { neighbor2 ->
            when {
              neighbor1 == neighbor2 -> null
              neighbor2 !in graph[neighbor1].orEmpty() -> null
              else -> setOf(node, neighbor1, neighbor2)
            }
          }
        }
      }
      .distinct()
      .count { triangle ->
        triangle.any { it.startsWith('t') }
      }
      .toString()
  }

  override fun partTwo(input: List<String>): String {
    return parseInput(input)
      .let(::findLargestClique)
      .sorted()
      .joinToString(",")
  }

  private fun parseInput(input: List<String>): Map<String, List<String>> {
    return input.fold(mutableMapOf<String, MutableList<String>>()) { acc, line ->
      val (a, b) = line.split("-")
      acc.apply {
        computeIfAbsent(a) { mutableListOf() }.add(b)
        computeIfAbsent(b) { mutableListOf() }.add(a)
      }
    }
  }

  private fun findLargestClique(graph: Map<String, List<String>>): List<String> {
    fun findCliques(current: List<String>, candidates: List<String>): List<String> {
      return when {
        candidates.isEmpty() -> current
        else -> {
          candidates.indices.map { i ->
            val newCandidate = candidates[i]
            val newCandidates = candidates
              .subList(i + 1, candidates.size)
              .filter { graph[newCandidate].orEmpty().contains(it) }

            findCliques(current + newCandidate, newCandidates)
          }.maxByOrNull { it.size } ?: current
        }
      }
    }

    return findCliques(emptyList(), graph.keys.toList())
  }

  private data class Node(val name: String, val children: List<Node>)

  private val exampleInput = listOf(
    "kh-tc",
    "qp-kh",
    "de-cg",
    "ka-co",
    "yn-aq",
    "qp-ub",
    "cg-tb",
    "vc-aq",
    "tb-ka",
    "wh-tc",
    "yn-cg",
    "kh-ub",
    "ta-co",
    "de-co",
    "tc-td",
    "tb-wq",
    "wh-td",
    "ta-ka",
    "td-qp",
    "aq-cg",
    "wq-ub",
    "ub-vc",
    "de-ta",
    "wq-aq",
    "wq-vc",
    "wh-yn",
    "ka-de",
    "kh-ta",
    "co-tc",
    "wh-qp",
    "tb-vc",
    "td-yn",
  )
  override val partOneTestExamples: Map<List<String>, String> = mapOf(
    exampleInput to "7"
  )

  override val partTwoTestExamples: Map<List<String>, String> = mapOf(
    exampleInput to "co,de,ka,ta"
  )
}
