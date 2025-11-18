package kr.hope.algorithm.graph.networkflow.usecase.bipartitematch.solution.baekjoon

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

/**
 * https://www.acmicpc.net/problem/9576
 * * 훨씬 낮은 시간복잡도의 정렬로도 풀 수 있음
 */
fun main() {
    val ioManager = BookSharingIOManager()
    val solver = BookSharing()

    val testCase = ioManager.readTestCases()

    repeat(testCase) {
        val capacities = ioManager.readCapacities()
        val result = solver.solution(capacities)
        ioManager.writeResult(result)
    }
}

class BookSharingIOManager {
    private val br = BufferedReader(InputStreamReader(System.`in`))
    private val bw = BufferedWriter(OutputStreamWriter(System.out))

    fun readTestCases(): Int {
        return br.readLine().toInt()
    }

    fun readCapacities(): Array<Array<Boolean>> {
        val (n, m) = br.readLine().split(" ").map { it.toInt() }
        val capacities = Array(m) { Array(n) { false } }
        (0 until m).forEach { student ->
            val (from, to) = br.readLine().split(" ").map { it.toInt() }
            (from - 1 until to).forEach { book ->
                capacities[student][book] = true
            }
        }
        return capacities
    }

    fun writeResult(result: Int) {
        bw.write("$result\n")
        bw.flush()
    }
}

class BookSharing {
    fun solution(capacities: Array<Array<Boolean>>): Int {
        val students = capacities.size
        val books = capacities.firstOrNull()?.size ?: 0

        val studentBook = Array(books) { -1 }
        var result = 0

        (0 until students).forEach { student ->
            val searched = Array(students) { false }
            if (canMatchBook(student, studentBook, searched, capacities)) {
                result++
            }
        }

        return result
    }

    private fun canMatchBook(student: Int, studentBook: Array<Int>, searched: Array<Boolean>, capacities: Array<Array<Boolean>>): Boolean {
        if (searched[student]) return false
        searched[student] = true

        studentBook.indices.forEach { book ->
            if (capacities[student][book]) {
                if (studentBook[book] == -1 || canMatchBook(studentBook[book], studentBook, searched, capacities)) {
                    studentBook[book] = student
                    return true
                }
            }
        }

        return false
    }
}