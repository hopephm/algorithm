package kr.hope.algorithm.graph.minimum.distance.category.dijkstra.solutions.programmers

import java.util.PriorityQueue

/**
 * @see https://school.programmers.co.kr/learn/courses/30/lessons/72413
 */
class TaxiCost {
    fun solution(n: Int, s: Int, a: Int, b: Int, fares: Array<IntArray>): Int {
        val graph = generateGraph(n, fares)
        val commonMinCost = dijkstra(s-1, graph)
        val aMinCost = dijkstra(a-1, graph)
        val bMinCost = dijkstra(b-1, graph)
        return (0 until n).minOf { v ->
            commonMinCost[v] + aMinCost[v] + bMinCost[v]
        }
    }

    private fun generateGraph(
        size: Int,
        fares: Array<IntArray>,
    ): Array<MutableList<Vertex>> {
        val graph = Array(size) { mutableListOf<Vertex>() }
        fares.forEach { (from, to, cost) ->
            graph[from - 1].add(Vertex(to - 1, cost))
            graph[to - 1].add(Vertex(from - 1, cost))
        }
        return graph
    }

    private fun dijkstra(
        startIdx: Int,
        graph: Array<MutableList<Vertex>>,
    ): IntArray {
        val visited = Array(graph.size) { false }
        val minCost = Array(graph.size) { Int.MAX_VALUE }
        minCost[startIdx] = 0

        val priorityQueue = PriorityQueue<Vertex>(compareBy { it.cost })
        priorityQueue.add(Vertex(startIdx,0))

        while (priorityQueue.isNotEmpty()) {
            val from = priorityQueue.poll()
            visited[from.idx] = true

            graph[from.idx].forEach { to ->
                val curCost = minCost[from.idx] + to.cost
                if (minCost[to.idx] > curCost) {
                    minCost[to.idx] = curCost
                    if (!visited[to.idx]) priorityQueue.add(Vertex(to.idx, minCost[to.idx]))
                }
            }
        }
        return minCost.toIntArray()
    }

    data class Vertex(
        val idx: Int,
        val cost: Int,
    )
}