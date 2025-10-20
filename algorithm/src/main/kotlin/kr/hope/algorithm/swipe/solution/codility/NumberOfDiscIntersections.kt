package kr.hope.algorithm.swipe.solution.codility

/**
 * @see https://app.codility.com/programmers/lessons/6-sorting/number_of_disc_intersections/
 */
class NumberOfDiscIntersections {
    fun solution(A: IntArray): Int {
        val (startPoints: List<Long>, endPoints: List<Long>) = A.mapIndexed { index, radius ->
            (index - radius.toLong()) to (index + radius.toLong())
        }.unzip()

        val sortedStartPoints = startPoints.sorted()
        val sortedEndPoints = endPoints.sorted()

        var endIndex = 0
        var stream = 0
        var intersects = 0

        sortedStartPoints.forEach { startPoint ->
            while (sortedEndPoints[endIndex] < startPoint) {
                stream--
                endIndex++
            }
            stream++
            intersects += stream - 1

            if (10000000 < intersects) {
                return -1
            }
        }

        return intersects
    }
}