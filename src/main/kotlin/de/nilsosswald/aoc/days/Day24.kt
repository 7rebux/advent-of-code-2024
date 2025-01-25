package de.nilsosswald.aoc.days

import de.nilsosswald.aoc.Day

object Day24 : Day<Long>(24, "Crossed Wires") {

  override fun partOne(input: List<String>): Long {
    return parseWires(input)
      .filter { it.name.startsWith("z") }
      .sortedByDescending(Wire::name)
      .map(Wire::output)
      .joinToString("") { if (it) "1" else "0" }
      .toLong(radix = 2)
  }

  // Solved graphically using https://graphviz.org/
  override fun partTwo(input: List<String>): Long {
    // gst,khg,nhn,tvb,vdc,z12,z21,z33
    return -1
  }

  private fun parseWires(input: List<String>): Collection<Wire> {
    val wires = input
      .takeWhile(String::isNotBlank)
      .map { line ->
        val (name, value) = line.split(": ")
        InputWire(name, value == "1")
      }
      .associateBy(Wire::name)
      .toMutableMap<String, Wire>()

    val queue = input
      .takeLastWhile(String::isNotBlank)
      .map { line ->
        val (input, output) = line.split(" -> ")
        val (a, gate, b) = input.split(" ")

        QueueEntry(output, gate, a, b)
      }
      .toMutableList()

    while (queue.isNotEmpty()) {
      val entry = queue
        .first { (_, _, a, b) ->
          a in wires && b in wires
        }
        .also(queue::remove)

      val gate = when (entry.gate) {
        "AND" -> AndGate
        "OR" -> OrGate
        "XOR" -> XorGate
        else -> error("Invalid gate")
      }

      wires[entry.output] = CompositeWire(
        entry.output,
        wires[entry.inputA]!!,
        wires[entry.inputB]!!,
        gate
      )
    }

    return wires.values
  }

  private data class QueueEntry(
    val output: String,
    val gate: String,
    val inputA: String,
    val inputB: String
  )

  private fun interface Gate {
    fun test(a: Wire, b: Wire): Boolean
  }

  private object AndGate : Gate {
    override fun test(a: Wire, b: Wire)= a.output() && b.output()
  }

  private object OrGate : Gate {
    override fun test(a: Wire, b: Wire) = a.output() || b.output()
  }

  private object XorGate : Gate {
    override fun test(a: Wire, b: Wire)= a.output() != b.output()
  }

  private sealed class Wire(open val name: String) {
    abstract fun output(): Boolean
  }

  private data class InputWire(
    override val name: String,
    val initial: Boolean
  ) : Wire(name) {
    override fun output() = initial
  }

  private data class CompositeWire(
    override val name: String,
    val a: Wire,
    val b: Wire,
    val gate: Gate
  ) : Wire(name), Gate by gate {
    override fun output() = test(a, b)
  }

  override val partOneTestExamples: Map<List<String>, Long> = mapOf(
    listOf(
      "x00: 1",
      "x01: 1",
      "x02: 1",
      "y00: 0",
      "y01: 1",
      "y02: 0",
      "",
      "x00 AND y00 -> z00",
      "x01 XOR y01 -> z01",
      "x02 OR y02 -> z02",
    ) to 4,

    listOf(
      "x00: 1",
      "x01: 0",
      "x02: 1",
      "x03: 1",
      "x04: 0",
      "y00: 1",
      "y01: 1",
      "y02: 1",
      "y03: 1",
      "y04: 1",
      "",
      "ntg XOR fgs -> mjb",
      "y02 OR x01 -> tnw",
      "kwq OR kpj -> z05",
      "x00 OR x03 -> fst",
      "tgd XOR rvg -> z01",
      "vdt OR tnw -> bfw",
      "bfw AND frj -> z10",
      "ffh OR nrd -> bqk",
      "y00 AND y03 -> djm",
      "y03 OR y00 -> psh",
      "bqk OR frj -> z08",
      "tnw OR fst -> frj",
      "gnj AND tgd -> z11",
      "bfw XOR mjb -> z00",
      "x03 OR x00 -> vdt",
      "gnj AND wpb -> z02",
      "x04 AND y00 -> kjc",
      "djm OR pbm -> qhw",
      "nrd AND vdt -> hwm",
      "kjc AND fst -> rvg",
      "y04 OR y02 -> fgs",
      "y01 AND x02 -> pbm",
      "ntg OR kjc -> kwq",
      "psh XOR fgs -> tgd",
      "qhw XOR tgd -> z09",
      "pbm OR djm -> kpj",
      "x03 XOR y03 -> ffh",
      "x00 XOR y04 -> ntg",
      "bfw OR bqk -> z06",
      "nrd XOR fgs -> wpb",
      "frj XOR qhw -> z04",
      "bqk OR frj -> z07",
      "y03 OR x01 -> nrd",
      "hwm AND bqk -> z03",
      "tgd XOR rvg -> z12",
      "tnw OR pbm -> gnj",
    ) to 2024
  )

  override val partTwoTestExamples: Map<List<String>, Long> = emptyMap()
}
