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
        Answer(Day08, 371, 1229),
        Answer(Day09, 6283404590840, 6304576012713),
        Answer(Day10, 778, 1925),
        Answer(Day11, 183620, 220377651399268),
        Answer(Day12, 1352976, 808796),
        Answer(Day13, 29436, 103729094227877),
        Answer(Day14, 230436441, 8270),
        Answer(Day15, 1478649, 1495455),
        Answer(Day16, 73404, 449),
        Answer(Day17, "3,4,3,1,7,6,5,6,0", "109019930331546"),
        Answer(Day18, "296", "28,44"),
        Answer(Day19, 216, 603191454138773),
        Answer(Day20, 1409, 1012821),
        Answer(Day21, 138764, 169137886514152),
        Answer(Day22, 19150344884, 2121),
        Answer(Day23, "926", "az,ed,hz,it,ld,nh,pc,td,ty,ux,wc,yg,zz"),
        Answer(Day24, 51410244478064, -1),
        Answer(Day25, 3065, -1),
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
