package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day15 : Day<Int, Int>(15, "Warehouse Woes") {

  override fun partOne(input: List<String>) = solve(input)

  override fun partTwo(input: List<String>) = solve(input, expand = true)

  private fun solve(input: List<String>, expand: Boolean = false): Int {
    val (warehouse, steps) = parseInput(input, expand)
    steps.forEach(warehouse::move)
    return warehouse.getBlockPositions().sumOf { it.gpsCoordinate() }
  }

  private fun parseInput(
    input: List<String>,
    expand: Boolean = false
  ): Pair<Warehouse, List<Direction>> {
    val map = input
      .takeWhile(String::isNotEmpty)
      .map(CharSequence::toMutableList)
      .let {
        if (expand) it.expand() else it
      }
      .map { it.toMutableList() }
      .toMutableList()
    val steps = input
      .takeLastWhile(String::isNotEmpty)
      .flatMap { line ->
        line.map(Direction::byIdentifier)
      }

    return Pair(Warehouse(map), steps)
  }

  private fun List<List<Char>>.expand(): List<List<Char>> {
    return this.map { line ->
      line.flatMap { c ->
        when (c) {
          '#' -> listOf('#', '#')
          'O' -> listOf('[', ']')
          '.' -> listOf('.', '.')
          '@' -> listOf('@', '.')
          else -> error("Unexpected character: $c")
        }
      }
    }
  }

  private data class Warehouse(val map: MutableList<MutableList<Char>>) {

    private var position = findRobotPosition()

    fun move(direction: Direction) {
      val next = position + direction.offset

      map[position] = '.'

      when (map[next]) {
        '.' -> position = next
        'O' -> {
          val blocksToMove = generateSequence(next) { it + direction.offset }
            .takeWhile { it in map && map[it] in listOf('O', '#') }
            .toList()
          val canMove = blocksToMove.none { map[it] == '#' }

          if (canMove) {
            map[blocksToMove.last() + direction.offset] = 'O'
            position = next
          }
        }
        '[', ']' -> {
          when (direction) {
            Direction.LEFT, Direction.RIGHT -> {
              val blocksToMove = generateSequence(next) { it + direction.offset }
                .takeWhile { it in map && map[it] in listOf('[', ']', '#') }
                .toList()
              val canMove = blocksToMove.none { map[it] == '#' }

              if (canMove) {
                blocksToMove.reversed().forEach { pos ->
                  map[pos + direction.offset] = map[pos]
                }
                position = next
              }
            }
            Direction.UP, Direction.DOWN -> {
              val partner = if (map[next] == '[') {
                next + Direction.RIGHT.offset
              } else {
                next + Direction.LEFT.offset
              }
              val blocksToMove = generateSequence(listOf(next, partner)) { row ->
                row
                  .map { it + direction.offset }
                  .flatMap { p ->
                    when {
                      p in map && map[p] == '[' -> listOf(p, p + Direction.RIGHT.offset)
                      p in map && map[p] == ']' -> listOf(p, p + Direction.LEFT.offset)
                      p in map && map[p] == '#' -> listOf(p)
                      else -> emptyList()
                    }
                  }
              }
                .takeWhile { sequence ->
                  sequence.isNotEmpty() && sequence.all {
                    it in map && map[it] in listOf('[', ']', '#')
                  }
                }
                .toList()
              val canMove = blocksToMove.flatten().none { map[it] == '#' }

              if (canMove) {
                blocksToMove.reversed().forEach { row ->
                  row.distinct().forEach { pos ->
                    map[pos + direction.offset] = map[pos]
                    map[pos] = '.'
                  }
                }
                position = next
              }
            }
          }
        }
      }

      map[position] = '@'
    }

    fun getBlockPositions(): List<Point> {
      return map.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
          when (c) {
            'O', '[' -> Point(x, y)
            else -> null
          }
        }
      }
    }

    private fun findRobotPosition(): Point {
      val y = map.indexOfFirst { line -> line.contains('@') }
      val x = map[y].indexOf('@')

      return Point(x, y)
    }
  }

  private data class Point(val x: Int, val y: Int)

  private fun Point.gpsCoordinate() = x + (y * 100)

  private enum class Direction(private val identifier: Char, val offset: Point) {
    UP('^', Point(0, -1)),
    LEFT('<', Point(-1, 0)),
    DOWN('v', Point(0, 1)),
    RIGHT('>', Point(1, 0));

    companion object {
      fun byIdentifier(identifier: Char): Direction =
        entries.first { it.identifier == identifier }
    }
  }

  private operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)

  @Suppress("Unused")
  private operator fun MutableList<MutableList<Char>>.get(point: Point) = this[point.y][point.x]

  private operator fun MutableList<MutableList<Char>>.set(point: Point, value: Char) {
    this[point.y][point.x] = value
  }

  private operator fun MutableList<MutableList<Char>>.contains(point: Point) =
    point.y in indices && point.x in this[point.y].indices

  private val largeExample = listOf(
    "##########",
    "#..O..O.O#",
    "#......O.#",
    "#.OO..O.O#",
    "#..O@..O.#",
    "#O#..O...#",
    "#O..O..O.#",
    "#.OO.O.OO#",
    "#....O...#",
    "##########",
    "",
    "<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^",
    "vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v",
    "><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<",
    "<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^",
    "^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><",
    "^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^",
    ">^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^",
    "<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>",
    "^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>",
    "v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^",
  )

  override val partOneTestExamples: Map<List<String>, Int> = mapOf(
    listOf(
      "########",
      "#..O.O.#",
      "##@.O..#",
      "#...O..#",
      "#.#.O..#",
      "#...O..#",
      "#......#",
      "########",
      "",
      "<^^>>>vv<v>>v<<",
    ) to 2028,

    largeExample to 10092
  )

  override val partTwoTestExamples: Map<List<String>, Int> = mapOf(
    largeExample to 9021
  )
}
