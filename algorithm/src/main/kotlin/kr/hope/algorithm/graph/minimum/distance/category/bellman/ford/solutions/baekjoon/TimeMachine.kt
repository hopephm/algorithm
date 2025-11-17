package kr.hope.algorithm.graph.minimum.distance.category.bellman.ford.solutions.baekjoon

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

/**
 * @see https://www.acmicpc.net/problem/11657
 */
fun main() {
    val ioManager = TimeMachineIOManager()
    val (cities, buses) = ioManager.readCitiesAndBuses()

    val solver = TimeMachine()
    val minCost = solver.solution(cities, buses)
    
    ioManager.print(minCost)
}

data class Bus(
    val from: Int,
    val to: Int,
    val cost: Long,
) {
    companion object {
        const val MAX_COST = Long.MAX_VALUE
    }
}

class TimeMachineIOManager {
    private val br = BufferedReader(InputStreamReader(System.`in`))
    private val bw = BufferedWriter(OutputStreamWriter(System.out))

    fun readCitiesAndBuses(): Pair<Int, Array<Bus>> {
        val (cities, busCount) = br.readLine().split(" ").map { it.toInt() }
        val buses = (0 until busCount).map {
            val (from, to, cost) = br.readLine().split(" ").map { it.toInt() }
            Bus(from - 1, to - 1, cost.toLong())
        }
        return cities to buses.toTypedArray()
    }

    fun print(minCosts: Array<Long>?) {
        when {
            minCosts == null -> bw.write("-1")
            else -> {
                minCosts.forEachIndexed { idx, cost ->
                    if (idx == 0) return@forEachIndexed
                    val minCost = if (cost == Bus.MAX_COST) -1 else cost
                    bw.write("$minCost\n")
                }
            }
        }
        bw.flush()
    }
}

class TimeMachine {
    fun solution(cities: Int, buses: Array<Bus>): Array<Long>? {
        val minDist = Array(cities) { Bus.MAX_COST }
        minDist[0] = 0

        (1 .. cities).forEach { repeat ->
            var anyMinDistChanged = false
            buses.forEach { bus ->
                if (minDist[bus.from] != Bus.MAX_COST && minDist[bus.to] > minDist[bus.from] + bus.cost) {
                    minDist[bus.to] = minDist[bus.from] + bus.cost
                    anyMinDistChanged = true
                }
            }

            when {
                !anyMinDistChanged -> return minDist
                repeat == cities && anyMinDistChanged -> return null
            }
        }

        return minDist
    }
}