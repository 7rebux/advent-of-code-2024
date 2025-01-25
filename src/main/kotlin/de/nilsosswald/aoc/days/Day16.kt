package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day
import java.util.PriorityQueue
import kotlin.collections.indexOfFirst
import kotlin.text.contains
import kotlin.text.indexOf

object Day16 : Day<Int, Int>(16, "Reindeer Maze") {

  override fun partOne(input: List<String>): Int {
    val (startTile, endTile) = parseInput(input)
    val queue = PriorityQueue<QueueState>()
      .apply { add(QueueState(DirectionalPath(listOf(startTile), Direction.East), 0)) }
    val visited = mutableMapOf<Pair<Point, Direction>, Int>()

    while (queue.isNotEmpty()) {
      val (path, cost) = queue.poll()
      val (tiles, direction) = path
      val key = Pair(tiles.last(), direction)

      if (tiles.last() == endTile) {
        return cost
      }

      if (visited.getOrDefault(key, Int.MAX_VALUE) > cost) {
        val next = tiles.last() + direction.offset

        if (input[next.y][next.x] != '#') {
          queue += QueueState(
            path.copy(tiles = tiles + next),
            cost + 1
          )
        }

        queue += QueueState(
          path.copy(direction = direction.nextClockwise()),
          cost + 1000
        )
        queue += QueueState(
          path.copy(direction = direction.nextCounterClockwise()),
          cost + 1000
        )

        visited[key] = cost
      }
    }

    error("Could not find a path")
  }

  override fun partTwo(input: List<String>): Int {
    val (startTile, endTile) = parseInput(input)
    val queue = PriorityQueue<QueueState>()
      .apply { add(QueueState(DirectionalPath(listOf(startTile), Direction.East), 0)) }
    val visited = mutableMapOf<Pair<Point, Direction>, Int>()
    val tilesInBestPaths: MutableSet<Point> = mutableSetOf()
    var minCost: Int? = null

    while (queue.isNotEmpty()) {
      val (path, cost) = queue.poll()
      val (tiles, direction) = path
      val key = Pair(tiles.last(), direction)

      if (minCost != null && cost > minCost) {
        break
      }

      if (tiles.last() == endTile) {
        minCost = cost
        tilesInBestPaths += path.tiles
        continue
      }

      if (visited.getOrDefault(key, Int.MAX_VALUE) >= cost) {
        val next = tiles.last() + direction.offset

        if (input[next.y][next.x] != '#') {
          queue += QueueState(
            path.copy(tiles = tiles + next),
            cost + 1
          )
        }

        queue += QueueState(
          path.copy(direction = direction.nextClockwise()),
          cost + 1000
        )
        queue += QueueState(
          path.copy(direction = direction.nextCounterClockwise()),
          cost + 1000
        )

        visited[key] = cost
      }
    }

    return tilesInBestPaths.size
  }

  private fun parseInput(input: List<String>): Pair<Point, Point> {
    val startTile = input.let {
      val y = it.indexOfFirst { line -> line.contains('S') }
      val x = it[y].indexOf('S')
      Point(x, y)
    }
    val endTile = input.let {
      val y = it.indexOfFirst { line -> line.contains('E') }
      val x = it[y].indexOf('E')
      Point(x, y)
    }

    return Pair(startTile, endTile)
  }

  private data class QueueState(val path: DirectionalPath, val cost: Int) : Comparable<QueueState> {
    override fun compareTo(other: QueueState) = this.cost.compareTo(other.cost)
  }

  private data class DirectionalPath(val tiles: List<Point>, val direction: Direction)

  private operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)

  private data class Point(val x: Int, val y: Int)

  private enum class Direction(val offset: Point) {
    North(Point(0, -1)),
    East(Point(1, 0)),
    South(Point(0, 1)),
    West(Point(-1, 0));

    fun nextClockwise() = entries[(ordinal + 1) % entries.size]
    fun nextCounterClockwise() = entries[(ordinal - 1).mod(entries.size)]
  }

  private val firstExample = listOf(
    "###############",
    "#.......#....E#",
    "#.#.###.#.###.#",
    "#.....#.#...#.#",
    "#.###.#####.#.#",
    "#.#.#.......#.#",
    "#.#.#####.###.#",
    "#...........#.#",
    "###.#.#####.#.#",
    "#...#.....#.#.#",
    "#.#.#.###.#.#.#",
    "#.....#...#.#.#",
    "#.###.#.#.#.#.#",
    "#S..#.....#...#",
    "###############",
  )

  private val secondExample = listOf(
    "#################",
    "#...#...#...#..E#",
    "#.#.#.#.#.#.#.#.#",
    "#.#.#.#...#...#.#",
    "#.#.#.#.###.#.#.#",
    "#...#.#.#.....#.#",
    "#.#.#.#.#.#####.#",
    "#.#...#.#.#.....#",
    "#.#.#####.#.###.#",
    "#.#.#.......#...#",
    "#.#.###.#####.###",
    "#.#.#...#.....#.#",
    "#.#.#.#####.###.#",
    "#.#.#.........#.#",
    "#.#.#.#########.#",
    "#S#.............#",
    "#################",
  )

  override val partOneTestExamples: Map<List<String>, Int> = mapOf(
    firstExample to 7036,
    secondExample to 11048
  )

  override val partTwoTestExamples: Map<List<String>, Int> = mapOf(
    firstExample to 45,
    secondExample to 64
  )
}
