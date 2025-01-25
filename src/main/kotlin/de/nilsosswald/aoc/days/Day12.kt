package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day12 : Day<Int, Int>(12, "Garden Groups") {

  override fun partOne(input: List<String>): Int {
    return findRegions(input).sumOf { region ->
      region.area * region.perimeter
    }
  }

  override fun partTwo(input: List<String>): Int {
    return findRegions(input).sumOf { region ->
      region.area * region.calculateFences()
    }
  }

  private fun findRegions(input: List<String>): List<Region> {
    return buildList {
      for ((y, line) in input.withIndex()) {
        for ((x, id) in line.withIndex()) {
          val start = Point(x, y)
          val points = mutableSetOf<Point>()
          val borders = mutableSetOf<Point>()

          if (this.any { start in it.points }) {
            continue
          }

          val queue = mutableListOf(start)

          while (queue.isNotEmpty()) {
            val point = queue.removeFirst()

            if (input[point.y][point.x] == id) {
              points += point

              val neighbors = point
                .neighbors()
                .filter { neighbor ->
                  input.getOrNull(neighbor.y)?.getOrNull(neighbor.x) == id
                }

              if (neighbors.size < 4) {
                borders += point
              }

              queue.addAll(neighbors.filter { it !in queue && it !in points })
            }
          }

          this += Region(id, points, borders)
        }
      }
    }
  }

  private fun Region.calculateFences(): Int {
    return Direction.entries.sumOf { facing ->
      val outerBorders = borders
        .filter { it + facing.offset !in points }
        .toMutableSet()
      var fencesNeeded = 0

      while (outerBorders.isNotEmpty()) {
        val queue = mutableListOf(outerBorders.first())
        val visited = mutableSetOf<Point>()

        while (queue.isNotEmpty()) {
          val current = queue.removeFirst()
          if (current !in visited) {
            visited.add(current)
            queue.addAll(current.neighbors().filter { it in outerBorders })
          }
        }

        outerBorders.removeAll(visited)
        fencesNeeded++
      }

      return@sumOf fencesNeeded
    }
  }

  private data class Region(
    val id: Char,
    val points: Set<Point>,
    val borders: Set<Point>
  ) {
    val area get() = points.size
    val perimeter get() = points.sumOf { point ->
      4 - point.neighbors().count { it in points }
    }
  }

  private enum class Direction(val offset: Point) {
    NORTH(Point(0, -1)),
    EAST(Point(1, 0)),
    SOUTH(Point(0, 1)),
    WEST(Point(-1, 0)),
  }

  private data class Point(val x: Int, val y: Int)

  private fun Point.neighbors() = buildSet {
    add(Point(x + 1, y))
    add(Point(x, y - 1))
    add(Point(x - 1, y))
    add(Point(x, y + 1))
  }

  private operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)

  override val partOneTestExamples: Map<List<String>, Int> = mapOf(
    listOf(
      "AAAA",
      "BBCD",
      "BBCC",
      "EEEC",
    ) to 140,

    listOf(
      "OOOOO",
      "OXOXO",
      "OOOOO",
      "OXOXO",
      "OOOOO",
    ) to 772,

    listOf(
      "RRRRIICCFF",
      "RRRRIICCCF",
      "VVRRRCCFFF",
      "VVRCCCJFFF",
      "VVVVCJJCFE",
      "VVIVCCJJEE",
      "VVIIICJJEE",
      "MIIIIIJJEE",
      "MIIISIJEEE",
      "MMMISSJEEE",
    ) to 1930
  )
  override val partTwoTestExamples: Map<List<String>, Int> = mapOf(
    listOf(
      "AAAA",
      "BBCD",
      "BBCC",
      "EEEC",
    ) to 80,

    listOf(
      "OOOOO",
      "OXOXO",
      "OOOOO",
      "OXOXO",
      "OOOOO",
    ) to 436,

    listOf(
      "AAAAAA",
      "AAABBA",
      "AAABBA",
      "ABBAAA",
      "ABBAAA",
      "AAAAAA",
    ) to 368,

    listOf(
      "RRRRIICCFF",
      "RRRRIICCCF",
      "VVRRRCCFFF",
      "VVRCCCJFFF",
      "VVVVCJJCFE",
      "VVIVCCJJEE",
      "VVIIICJJEE",
      "MIIIIIJJEE",
      "MIIISIJEEE",
      "MMMISSJEEE",
    ) to 1206
  )
}
