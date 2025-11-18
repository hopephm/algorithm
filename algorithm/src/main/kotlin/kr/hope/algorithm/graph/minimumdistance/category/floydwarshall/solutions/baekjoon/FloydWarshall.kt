package kr.hope.algorithm.graph.minimumdistance.category.floydwarshall.solutions.baekjoon

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

/**
 * @see https://www.acmicpc.net/problem/11404
 */
fun main() {
    val ioManager = FloydWarshallSystemIOManager()

    val n = ioManager.readN()
    val busList = ioManager.readBusList()

    val solver = FloydWarshall()
    val minDist = solver.solution(n, busList)

    ioManager.print(minDist)
}

data class Bus(
    val from: Int,
    val to: Int,
    val cost: Int,
)

class FloydWarshallSystemIOManager {
    private val br = BufferedReader(InputStreamReader(System.`in`))
    private val bw = BufferedWriter(OutputStreamWriter(System.`out`))

    fun readN(): Int {
        return br.readLine().toInt()
    }

    fun readBusList(): List<Bus> {
        val m = br.readLine().toInt()
        return (0 until m).map {
            val (from, to, cost) = br.readLine().split(" ").map { it.toInt() }
            Bus(from, to, cost)
        }
    }

    fun print(dist: Array<LongArray>) {
        dist.forEach { row ->
            row.forEach {
                val min = when {
                    it == FloydWarshall.MAX_COST -> 0L
                    else -> it
                }
                bw.write("$min ")
            }
            bw.write("\n")
        }
        bw.flush()
    }
}

class FloydWarshall {
    companion object {
        const val MAX_COST = 100000000000L
    }

    fun solution(n: Int, busList: List<Bus>): Array<LongArray> {
        val dist = initDist(n, busList)

        (0 until n).forEach { k ->
            (0 until n).forEach { y ->
                (0 until n).forEach { x ->
                    val bypassDist = dist[y][k] + dist[k][x]
                    if (bypassDist < dist[y][x]) {
                        dist[y][x] = bypassDist
                    }
                }
            }
        }

        return dist
    }

    private fun initDist(n: Int, busList: List<Bus>): Array<LongArray> {
        val minDist = Array(n) { Array(n) { MAX_COST }.toLongArray() }
        (0 until n).forEach { minDist[it][it] = 0 }
        busList.forEach {
            if (minDist[it.from - 1][it.to - 1] > it.cost.toLong()) {
                minDist[it.from - 1][it.to - 1] = it.cost.toLong()
            }
        }
        return minDist
    }
}