package de.nosswald.aoc.days

import de.nosswald.aoc.Day

object Day01 : Day<Int>(1, "Example") {
    override fun partOne(input: List<String>): Int {
        return input.map(String::toInt).sum()
    }

    override fun partTwo(input: List<String>): Int {
        return input.map(String::toInt).reduce { acc, i -> acc * i }
    }

    override val partOneTestExamples: Map<List<String>, Int> = mapOf(
        listOf("1", "2") to 3,
        listOf("4", "2", "3") to 9
    )

    override val partTwoTestExamples: Map<List<String>, Int> = mapOf(
        listOf("1", "2") to 2,
        listOf("4", "2", "3") to 24
    )
}
