package kr.hope.algorithm.graph.minimum.spanning.tree.category.kruskal.solution.baekjoon

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @see https://www.acmicpc.net/problem/1197
 */
fun main() {
    val ioManager = MinimumSpanningTreeIOManager()
    val (v, edges) = ioManager.readVertexesAndEdges()

    val solver = MinimumSpanningTree()
    val totalCost = solver.solution(v, edges)

    println(totalCost)
}

data class Edge(
    val from: Int,
    val to: Int,
    val cost: Int,
)

class MinimumSpanningTreeIOManager {
    private val br = BufferedReader(InputStreamReader(System.`in`))

    fun readVertexesAndEdges(): Pair<Int, Array<Edge>> {
        val (vertexes, edgeCount) = br.readLine().split(" ").map { it.toInt() }
        val edges = (0 until edgeCount).map {
            val (from, to, cost) = br.readLine().split(" ").map { it.toInt() }
            Edge(from-1, to-1, cost)
        }.toTypedArray()
        return vertexes to edges
    }
}

class MinimumSpanningTree {
    fun solution(v: Int, edges: Array<Edge>): Int {
        val sortedEdges = edges.sortedBy { it.cost }
        val root = (0 until v).map { it }.toIntArray()

        var unionCount = 0
        var cost = 0
        sortedEdges.forEach { edge ->
            val fromRoot = root.find(edge.from)
            val toRoot = root.find(edge.to)

            if (fromRoot != toRoot) {
                root.union(fromRoot, toRoot)
                unionCount++
                cost += edge.cost
            }

            if (unionCount == v-1) {
                return cost
            }
        }

        return cost
    }

    private fun IntArray.find(idx: Int): Int {
        if (this[idx] == idx) {
            return idx
        }
        this[idx] = this.find(this[idx])
        return this[idx]
    }

    private fun IntArray.union(idx1: Int, idx2: Int) {
        val idx1Root = this.find(idx1)
        val idx2Root = this.find(idx2)
        this[idx2Root] = idx1Root
    }
}

