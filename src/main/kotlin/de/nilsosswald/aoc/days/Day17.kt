package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day17 : Day<String, Long>(17, "Chronospatial Computer") {

  override fun partOne(input: List<String>) = parseInput(input).run()

  // https://www.reddit.com/r/adventofcode/comments/1hgig79/2024_day_17_part_2_i_need_the_hit_me_over_the
  override fun partTwo(input: List<String>): Long {
    val computer = parseInput(input)
    val candidates = computer.program
      .reversed()
      .map(Int::toLong)
      .fold(listOf(0L)) { candidates, instruction ->
        candidates.flatMap { candidate ->
          val shifted = candidate shl 3
          (shifted..shifted + 8).mapNotNull { attempt ->
            computer.copy().run {
              this.registerA = attempt
              attempt.takeIf { this.run().split(",").first().toLong() == instruction }
            }
          }
        }
      }

    return candidates.first()
  }

  private fun parseInput(input: List<String>): Computer {
    val (a, b, c) = input
      .take(3)
      .map { it.substringAfter(": ").toLong() }
    val program = input
      .last()
      .substringAfter("Program: ")
      .split(",")
      .map(String::toInt)

    return Computer(program, a, b, c)
  }

  private data class Computer(
    val program: List<Int>,
    var registerA: Long,
    var registerB: Long,
    var registerC: Long,
  ) {
    fun run(): String {
      var pointer = 0
      var output = mutableListOf<Int>()

      while (pointer < program.lastIndex) {
        val opcode = program[pointer]
        val literalOperand = program[pointer + 1]
        val comboOperand = literalOperand.toComboOperand()
        var jumped = false

        when (opcode) {
          0 -> registerA = registerA shr comboOperand.toInt()
          1 -> registerB = registerB xor literalOperand.toLong()
          2 -> registerB = comboOperand % 8
          3 -> {
            if (registerA != 0L) {
              pointer = literalOperand
              jumped = true
            }
          }
          4 -> registerB = registerB xor registerC
          5 -> output += (comboOperand % 8).toInt()
          6 -> registerB = registerA shr comboOperand.toInt()
          7 -> registerC = registerA shr comboOperand.toInt()
        }

        if (!jumped) {
          pointer += 2
        }
      }

      return output.joinToString(",")
    }

    private fun Int.toComboOperand() = when (this) {
      in 0..3 -> this.toLong()
      4 -> registerA
      5 -> registerB
      6 -> registerC
      else -> error("Invalid operand $this")
    }
  }

  override val partOneTestExamples: Map<List<String>, String> = mapOf(
    listOf(
      "Register A: 729",
      "Register B: 0",
      "Register C: 0",
      "",
      "Program: 0,1,5,4,3,0",
    ) to "4,6,3,5,6,3,5,2,1,0"
  )

  override val partTwoTestExamples: Map<List<String>, Long> = mapOf(
    listOf(
      "Register A: 2024",
      "Register B: 0",
      "Register C: 0",
      "",
      "Program: 0,3,5,4,3,0",
    ) to 117440
  )
}
