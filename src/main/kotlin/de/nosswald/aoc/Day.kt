package de.nosswald.aoc

import de.nosswald.aoc.utils.InputReader

abstract class Day<T>(val number: Int, val title: String) {
    private val inputList by lazy { InputReader.readAsList(number) }

    abstract fun partOne(input: List<String> = inputList): T
    abstract fun partTwo(input: List<String> = inputList): T

    abstract val partOneTestExamples: Map<List<String>, T>
    abstract val partTwoTestExamples: Map<List<String>, T>
}
