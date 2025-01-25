package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day05 : Day<Int, Int>(5, "Print Queue") {

  override fun partOne(input: List<String>): Int {
    val rules = parseRuleMap(input)

    return parseUpdates(input)
      .filter { it.isSortedBy(rules) }
      .sumOf(::middleElement)
  }

  override fun partTwo(input: List<String>): Int {
    val rules = parseRuleMap(input)

    return parseUpdates(input)
      .filterNot { update -> update.isSortedBy(rules) }
      .map { update ->
        update.sortedWith { a, b ->
          when {
            rules[a].orEmpty().contains(b) -> 1
            rules[b].orEmpty().contains(a) -> -1
            else -> 0
          }
        }
      }
      .sumOf(::middleElement)
  }

  private fun parseRuleMap(input: List<String>) = input
    .takeWhile { line -> line.isNotBlank() }
    .map { line ->
      line
        .split("|")
        .map(String::toInt)
        .let { (a, b) -> a to b }
    }
    .groupBy { it.second }
    .mapValues { (_, value) -> value.map { it.first } }

  private fun parseUpdates(input: List<String>) = input
    .takeLastWhile { line -> line.isNotBlank() }
    .map { it.split(",").map(String::toInt) }

  private fun List<Int>.isSortedBy(rules: Map<Int, List<Int>>) = this
    .withIndex()
    .none { (i, page) ->
      rules[page].orEmpty().any(this.subList(i + 1, this.size)::contains)
    }

  private fun <T> middleElement(list: List<T>) = list[list.size / 2]

  private val exampleInput = listOf(
    "47|53",
    "97|13",
    "97|61",
    "97|47",
    "75|29",
    "61|13",
    "75|53",
    "29|13",
    "97|29",
    "53|29",
    "61|53",
    "97|53",
    "61|29",
    "47|13",
    "75|47",
    "97|75",
    "47|61",
    "75|61",
    "47|29",
    "75|13",
    "53|13",
    "",
    "75,47,61,53,29",
    "97,61,53,29,13",
    "75,29,13",
    "75,97,47,61,53",
    "61,13,29",
    "97,13,75,29,47"
  )

  override val partOneTestExamples: Map<List<String>, Int> = mapOf(
    exampleInput to 143
  )

  override val partTwoTestExamples: Map<List<String>, Int> = mapOf(
    exampleInput to 123
  )
}
