package kr.hope.algorithm.graph.network.flow.category.ford.fulkerson.solution.baekjoon

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.min

/**
 * @see https://www.acmicpc.net/problem/6086
 */
fun main() {
    val ioManager = MaximumFlowIOManager2dArray()
    val capacities = ioManager.readCapacity()

    val solver = MaximumFlow()
    val result = solver.solution(capacities)

    ioManager.writeResult(result)
}

class MaximumFlowIOManager2dArray {
    private val br = BufferedReader(InputStreamReader(System.`in`))
    private val bw = BufferedWriter(OutputStreamWriter(System.out))

    fun readCapacity(): Array<Array<Int>> {
        val capacities = Array(MaximumFlow.VERTEX) { Array(MaximumFlow.VERTEX) { 0 }  }

        val n = br.readLine().toInt()

        repeat(n) {
            val capacity = br.readLine().split(' ')
            val from = capacity[0].first().toIdx()
            val to = capacity[1].first().toIdx()
            val amount = capacity[2].toInt()
            capacities[from][to] += amount
            capacities[to][from] += amount
        }

        return capacities
    }

    private fun Char.toIdx(): Int {
        return when {
            this.code >= 'a'.code -> this.code - 'a'.code + 26
            else -> this.code - 'A'.code
        }
    }

    fun writeResult(result: Int) {
        bw.write("$result\n")
        bw.flush()
    }
}

class MaximumFlow {
    companion object {
        const val VERTEX = 52
        const val SOURCE = 0
        const val SINK = 25
        const val MAX_CAPACITY = 1000
    }

    fun solution(capacities: Array<Array<Int>>): Int {
        val flows = Array(VERTEX) { Array(VERTEX) { 0 } }
        var route = findRoute(capacities, flows)
        var totalFlow = 0

        while (route != null) {
            val minFlow = findMinFlow(capacities, flows, route)
            pushFlows(flows, route, minFlow)
            totalFlow += minFlow
            route = findRoute(capacities, flows)
        }

        return totalFlow
    }

    private fun findRoute(capacities: Array<Array<Int>>, flows: Array<Array<Int>>): Array<Int>? {
        val parent = Array(VERTEX) { -1 }
        val queue = ArrayDeque<Int>()
        queue.add(SOURCE)
        parent[SOURCE] = SOURCE

        while (queue.isNotEmpty() && parent[SINK] == -1) {
            val from = queue.removeFirst()
            capacities[from].forEachIndexed { to, capacity ->
                val residual = capacity - flows[from][to]
                if (residual > 0 && parent[to] == -1) {
                    queue.add(to)
                    parent[to] = from
                }
            }
        }

        return if (parent[SINK] == -1) null else parent
    }

    private fun findMinFlow(capacities: Array<Array<Int>>, flows: Array<Array<Int>>, route: Array<Int>): Int {
        var to = SINK
        var minFlow = MAX_CAPACITY + 1
        while (to != SOURCE) {
            val from = route[to]
            val residual = capacities[from][to] - flows[from][to]
            minFlow = min(residual, minFlow)
            to = from
        }
        return minFlow
    }

    private fun pushFlows(flows: Array<Array<Int>>, route: Array<Int>, minFlow: Int) {
        var to = SINK
        while (to != SOURCE) {
            val from = route[to]
            flows[from][to] += minFlow
            flows[to][from] -= minFlow
            to = from
        }
    }
}