package kr.hope.algorithm.graph.minimum.spanning.tree.category.prim.solution.baekjoon

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.PriorityQueue

/**
 * @see https://www.acmicpc.net/problem/1922
 */
fun main() {
    val ioManager = MinimumSpanningTreeELogVIOManager()
    val (v, edges) = ioManager.readVertexesAndEdges()

    val solver = MinimumSpanningTreeELogV()
    val totalCost = solver.solution(v, edges)

    println(totalCost)
}

data class EdgeELogV(
    val to: Int,
    val cost: Int,
)

class MinimumSpanningTreeELogVIOManager {
    private val br = BufferedReader(InputStreamReader(System.`in`))

    fun readVertexesAndEdges(): Pair<Int, Array<MutableList<EdgeELogV>>> {
        val vertexes = br.readLine().toInt()
        val edgeCount = br.readLine().toInt()
        val edges = Array(vertexes) { mutableListOf<EdgeELogV>() }
        repeat(edgeCount) {
            val (from, to, cost) = br.readLine().split(" ").map { it.toInt() }
            edges[from - 1].add(EdgeELogV(to - 1, cost))
            edges[to - 1].add(EdgeELogV(from - 1, cost))
        }

        return vertexes to edges
    }
}

class MinimumSpanningTreeELogV {
    fun solution(v: Int, edges: Array<MutableList<EdgeELogV>>): Int {
        var visitedCount = 0
        val visited = Array(v) { false }
        var totalCost = 0
        val pq = PriorityQueue<EdgeELogV>(compareBy { it.cost })

        pq.add(EdgeELogV(0, 0))
        while (pq.isNotEmpty() && visitedCount < v) {
            val (from, cost) = pq.poll()
            if (visited[from]) continue

            totalCost += cost
            visited[from] = true
            visitedCount++

            edges[from].forEach { edge ->
                if (!visited[edge.to]) { pq.add(edge) }
            }
        }

        return totalCost
    }
}

