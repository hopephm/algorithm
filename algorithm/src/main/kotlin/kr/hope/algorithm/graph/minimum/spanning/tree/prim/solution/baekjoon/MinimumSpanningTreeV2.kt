package kr.hope.algorithm.graph.minimum.spanning.tree.prim.solution.baekjoon

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @see https://www.acmicpc.net/problem/1922
 */
fun main() {
    val ioManager = MinimumSpanningTreeV2IOManager()
    val (v, edges) = ioManager.readVertexesAndEdges()

    val solver = MinimumSpanningTreeV2()
    val totalCost = solver.solution(v, edges)

    println(totalCost)
}

data class EdgeV2(
    val to: Int,
    val cost: Int,
)

class MinimumSpanningTreeV2IOManager {
    private val br = BufferedReader(InputStreamReader(System.`in`))

    fun readVertexesAndEdges(): Pair<Int, Array<MutableList<EdgeV2>>> {
        val vertexes = br.readLine().toInt()
        val edgeCount = br.readLine().toInt()
        val edges = Array(vertexes) { mutableListOf<EdgeV2>() }
        repeat(edgeCount) {
            val (from, to, cost) = br.readLine().split(" ").map { it.toInt() }
            edges[from - 1].add(EdgeV2(to - 1, cost))
            edges[to - 1].add(EdgeV2(from - 1, cost))
        }

        return vertexes to edges
    }
}

class MinimumSpanningTreeV2 {
    companion object {
        private const val INF = 10001
    }

    fun solution(v: Int, edges: Array<MutableList<EdgeV2>>): Int {
        val visited = Array(v) { false }
        val minEdgeCost = Array(v) { INF }
        val parent = Array(v) { it }

        minEdgeCost[0] = 0

        repeat(v) {
            val from = minEdgeCost.withIndex().minBy { (idx, value) ->
                if (!visited[idx]) value else INF
            }.index

            visited[from] = true

            edges[from].forEach { (to, cost) ->
                if (!visited[to] && minEdgeCost[to] > cost) {
                    minEdgeCost[to] = cost
                    parent[to] = from
                }
            }
        }

        return minEdgeCost.reduce { acc, cost -> acc + cost }
    }
}

