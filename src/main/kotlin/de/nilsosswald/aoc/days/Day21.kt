package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day21 : Day<Long, Long>(21, "Keypad Conundrum") {

  override fun partOne(input: List<String>) = input.sumOf { computeComplexity(it, 2) }

  override fun partTwo(input: List<String>) = input.sumOf { computeComplexity(it, 25) }

  private fun computeComplexity(code: String, robots: Int): Long {
    val numericPart = code.dropLast(1).toLong()
    return calcMinSteps(code, robots) * numericPart
  }

  private fun calcMinSteps(
    code: String,
    robots: Int,
    initial: Boolean = true,
    cache: MutableMap<Pair<String, Int>, Long> = mutableMapOf()
  ): Long {
    val keypad = if (initial) numericKeypad else directionalKeypad

    return cache.getOrPut(code to robots) {
      "A$code".zipWithNext().sumOf { (from, to) ->
        val possiblePaths = possiblePaths(from, to, keypad)

        if (robots == 0) {
          possiblePaths.minOf { it.length }.toLong()
        } else {
          possiblePaths.minOf { path -> calcMinSteps(path, robots - 1, false, cache) }
        }
      }
    }
  }

  private val pathsCache = mutableMapOf<Triple<Char, Char, Keypad>, List<String>>()

  private fun possiblePaths(from: Char, to: Char, keypad: Keypad): List<String> {
    return pathsCache.getOrPut(Triple(from, to, keypad)) {
      val steps = mutableListOf<Direction>()
      val destination = keypad.positionOf(to)

      keypad.position = keypad.positionOf(from)

      while (keypad.position != destination) {
        val delta = keypad.position - destination
        val next = when {
          delta.x > 0 -> Direction.LEFT
          delta.x < 0 -> Direction.RIGHT
          delta.y > 0 -> Direction.UP
          delta.y < 0 -> Direction.DOWN
          else -> error("Unreachable case")
        }

        keypad.move(next)
        steps.add(next)
      }

      steps
        .permutations()
        .distinct()
        .filter { validatePath(it, from, keypad) }
        .map { it.joinToString("") + "A" }
    }
  }

  private fun validatePath(
    path: List<Direction>,
    start: Char,
    keypad: Keypad
  ): Boolean {
    keypad.position = keypad.positionOf(start)

    for (dir in path) {
      if (!keypad.canMove(dir)) {
        return false
      }

      keypad.move(dir)
    }

    return true
  }

  private fun <T> List<T>.permutations(): List<List<T>> {
    return if (isEmpty()) {
      listOf(emptyList())
    } else mutableListOf<List<T>>().also { result ->
      for (i in this.indices) {
        (this - this[i]).permutations().forEach {
          result.add(it + this[i])
        }
      }
    }
  }

  private class Keypad(private val schema: List<List<Char>>) {

    var position = positionOf('A')

    fun positionOf(char: Char): Point {
      val y = schema.indexOfFirst { it.contains(char) }
      val x = schema[y].indexOf(char)

      return Point(x, y)
    }

    fun canMove(direction: Direction): Boolean {
      return schema[position + direction.offset] != ' '
    }

    fun move(direction: Direction) {
      position += direction.offset
    }
  }

  private val numericKeypad = Keypad(
    listOf(
      listOf('7', '8', '9'),
      listOf('4', '5', '6'),
      listOf('1', '2', '3'),
      listOf(' ', '0', 'A'),
    )
  )

  private val directionalKeypad = Keypad(
    listOf(
      listOf(' ', '^', 'A'),
      listOf('<', 'v', '>'),
    )
  )

  private enum class Direction(private val identifier: Char, val offset: Point) {
    UP('^', Point(0, -1)),
    LEFT('<', Point(-1, 0)),
    DOWN('v', Point(0, 1)),
    RIGHT('>', Point(1, 0));

    override fun toString() = identifier.toString()
  }

  private data class Point(val x: Int, val y: Int)

  private operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)

  private operator fun Point.minus(other: Point) = Point(x - other.x, y - other.y)

  @Suppress("Unused")
  private operator fun List<List<Char>>.get(point: Point) = this[point.y][point.x]

  override val partOneTestExamples: Map<List<String>, Long> = mapOf(
    listOf(
      "029A",
      "980A",
      "179A",
      "456A",
      "379A",
    ) to 126384
  )

  override val partTwoTestExamples: Map<List<String>, Long> = emptyMap()
}
