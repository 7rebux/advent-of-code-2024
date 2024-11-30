import de.nosswald.aoc.Day
import de.nosswald.aoc.days.Day01
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class DaysTest {
    data class Answer<T>(
        val day: Day<T>,
        val partOne: T,
        val partTwo: T
    )

    @TestFactory
    fun answers() = listOf(
        Answer(Day01, 21, 720),
    ).map {
        DynamicTest.dynamicTest("Day ${it.day.number} - ${it.day.title}") {
            if (it.day.partOneTestExamples.isNotEmpty()) {
                print("Testing Part 1 examples..")
                it.day.partOneTestExamples.entries.forEach { entry ->
                    Assertions.assertEquals(entry.value, it.day.partOne(entry.key))
                }
                print(" SUCCESS\n")
            }

            print("Testing Part 1..")
            Assertions.assertEquals(it.partOne, it.day.partOne())
            print(" SUCCESS\n")

            if (it.day.partTwoTestExamples.isNotEmpty()) {
                print("Testing Part 2 examples..")
                it.day.partTwoTestExamples.entries.forEach { entry ->
                    Assertions.assertEquals(entry.value, it.day.partTwo(entry.key))
                }
                print(" SUCCESS\n")
            }

            print("Testing Part 2..")
            Assertions.assertEquals(it.partTwo, it.day.partTwo())
            print(" SUCCESS\n")
        }
    }
}
