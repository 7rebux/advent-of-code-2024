package de.nilsosswald.aoc

import de.nilsosswald.aoc.utils.InputReader

abstract class Day<O, T>(val number: Int, val title: String) {
  private val inputList by lazy { InputReader.readAsList(number) }

  abstract fun partOne(input: List<String> = inputList): O
  abstract fun partTwo(input: List<String> = inputList): T

  abstract val partOneTestExamples: Map<List<String>, O>
  abstract val partTwoTestExamples: Map<List<String>, T>
}
