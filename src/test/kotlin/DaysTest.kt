import de.nilsosswald.aoc.Day
import de.nilsosswald.aoc.days.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class DaysTest {

    @TestFactory
    fun answers() = listOf(
        Answer(Day01, 2904518, 18650129),
        Answer(Day02, 572, 612),
        Answer(Day03, 188116424, 104245808),
        Answer(Day04, 2562, 1902),
        Answer(Day05, 4569, 6456),
        Answer(Day06, 4903, 1911),
        Answer(Day07, 2299996598890, 362646859298554),
        Answer(Day09, 6283404590840, 6304576012713),
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

    data class Answer<T>(
        val day: Day<T>,
        val partOne: T,
        val partTwo: T
    )
}
