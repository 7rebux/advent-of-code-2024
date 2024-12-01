package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day
import kotlin.math.abs

object Day01 : Day<Long>(1, "Historian Hysteria") {

    override fun partOne(input: List<String>): Long {
        val (left, right) = input
            .map { line ->
                line.split("   ")
                    .map(String::toLong)
                    .let { (a, b) -> a to b }
            }
            .unzip()
            .let { (left, right) -> left.sorted() to right.sorted() }

        return left.indices.sumOf { i -> abs(left[i] - right[i]) }
    }

    override fun partTwo(input: List<String>): Long {
        val (left, right) = input
            .map { line ->
                line.split("   ")
                    .map(String::toLong)
                    .let { (a, b) -> a to b }
            }
            .unzip()

        return left.sumOf { left -> right.count { it == left } * left }
    }

    override val partOneTestExamples: Map<List<String>, Long> = mapOf(
        listOf(
            "3   4",
            "4   3",
            "2   5",
            "1   3",
            "3   9",
            "3   3"
        ) to 11
    )

    override val partTwoTestExamples: Map<List<String>, Long> = mapOf(
        listOf(
            "3   4",
            "4   3",
            "2   5",
            "1   3",
            "3   9",
            "3   3"
        ) to 31
    )
}
