package kr.hope.algorithm.graph.network.flow.ford.fulkerson.solution.baekjoon

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.min

/**
 * @see https://www.acmicpc.net/problem/6086
 */
fun main() {
    val ioManager = MaximumFlowIOManagerAdjacencyList()
    val edges = ioManager.readCapacity()

    val solver = MaximumFlowAdjacencyList()
    val result = solver.solution(edges)

    ioManager.writeResult(result)
}

data class Edge(
    val to: Int,
    var capacity: Int,
    var flow: Int = 0,
    var reverse: Edge? = null
) {
    val residual: Int get() = capacity - flow

    fun pushFlow(amount: Int) {
        flow += amount
        reverse?.let { it.flow -= amount }
    }
}

class MaximumFlowIOManagerAdjacencyList {
    private val br = BufferedReader(InputStreamReader(System.`in`))
    private val bw = BufferedWriter(OutputStreamWriter(System.out))

    fun readCapacity(): Array<MutableList<Edge>> {
        val edges = Array(MaximumFlowAdjacencyList.VERTEX) { mutableListOf<Edge>()  }

        val n = br.readLine().toInt()

        repeat(n) {
            val capacity = br.readLine().split(' ')
            val from = capacity[0].first().toIdx()
            val to = capacity[1].first().toIdx()
            val amount = capacity[2].toInt()

            val existEdge = edges[from].find { it.to == to }

            when {
                existEdge == null -> {
                    val edge = Edge(to, amount)
                    val reverseEdge = Edge(from, amount)

                    edges[from].add(edge)
                    edges[to].add(reverseEdge)

                    edge.reverse = reverseEdge
                    reverseEdge.reverse = edge
                }
                else -> {
                    existEdge.capacity += amount
                    existEdge.reverse!!.capacity += amount
                }
            }
        }

        return edges
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

class MaximumFlowAdjacencyList {
    companion object {
        const val VERTEX = 52
        const val SOURCE = 0
        const val SINK = 25
        const val MAX_CAPACITY = 1000
    }

    fun solution(edges: Array<MutableList<Edge>>): Int {
        var route = findRoute(edges)
        var totalFlow = 0

        while (route != null) {
            val minFlow = findMinFlow(edges, route)
            pushFlows(edges, route, minFlow)
            totalFlow += minFlow
            route = findRoute(edges)
        }

        return totalFlow
    }

    private fun findRoute(edges: Array<MutableList<Edge>>): Array<Int>? {
        val parent = Array(VERTEX) { -1 }
        val queue = ArrayDeque<Int>()
        queue.add(SOURCE)
        parent[SOURCE] = SOURCE

        while (queue.isNotEmpty() && parent[SINK] == -1) {
            val from = queue.removeFirst()
            edges[from].forEach { edge ->
                if (edge.residual > 0 && parent[edge.to] == -1) {
                    queue.add(edge.to)
                    parent[edge.to] = from
                }
            }
        }

        return if (parent[SINK] == -1) null else parent
    }

    private fun findMinFlow(edges: Array<MutableList<Edge>>, route: Array<Int>): Int {
        var to = SINK
        var minFlow = MAX_CAPACITY + 1
        while (to != SOURCE) {
            val from = route[to]
            val edge = edges[from].find { it.to == to } ?: throw IllegalStateException()
            minFlow = min(edge.residual, minFlow)
            to = from
        }
        return minFlow
    }

    private fun pushFlows(edges: Array<MutableList<Edge>>, route: Array<Int>, minFlow: Int) {
        var to = SINK
        while (to != SOURCE) {
            val from = route[to]
            val edge = edges[from].find { it.to == to } ?: throw IllegalStateException()
            edge.pushFlow(minFlow)
            to = from
        }
    }

    private fun Int.toChar2(): Char {
        return when {
            this >= 26 -> Char('a'.code + this - 26)
            else -> Char('A'.code + this)
        }
    }
}