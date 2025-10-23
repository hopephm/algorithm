package kr.hope.algorithm.graph.unionfind.solution.programmers

/**
 * @see https://school.programmers.co.kr/learn/courses/30/lessons/43162
 */
class Network {
    fun solution(n: Int, computers: Array<IntArray>): Int {
        val parent = computers.mapIndexed { idx, _ -> idx }.toTypedArray()
        (0 until n).forEach { i ->
            (i + 1 until n).forEach { j ->
                if (computers[i][j] == 1) {
                    union(i, j, parent)
                }
            }
        }

        return parent.map {
            root(it, parent)
        }.distinct().size
    }

    private fun union(node1: Int, node2: Int, parent: Array<Int>) {
        val root1 = root(node1, parent)
        val root2 = root(node2, parent)
        parent[root1] = root2
    }

    private fun root(node: Int, parent: Array<Int>): Int {
        if (parent[node] == node) {
            return node
        }
        return root(parent[node], parent).also {
            parent[node] = it
        }
    }
}