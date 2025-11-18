package kr.hope.algorithm.graph.minimumdistance.category.floydwarshall.solutions.programmers

/**
 * @see https://school.programmers.co.kr/learn/courses/30/lessons/49191
 */
class Ranking {
    fun solution(n: Int, results: Array<IntArray>): Int {
        val fightResult = initFightResult(n, results)
        (0 until n).forEach { viaFighter ->
            (0 until n).forEach { fighter1 ->
                (0 until n).forEach { fighter2 ->
                    when {
                        fightResult[fighter1][viaFighter] == 1 && fightResult[viaFighter][fighter2] == 1 ->
                            fightResult[fighter1][fighter2] = 1
                        fightResult[fighter1][viaFighter] == -1 && fightResult[viaFighter][fighter2] == -1 ->
                            fightResult[fighter1][fighter2] = -1
                    }
                }
            }
        }
        return fightResult.count { result ->
            result.count { it == 0 } == 1
        }
    }

    private fun initFightResult(n: Int, results: Array<IntArray>): Array<IntArray> {
        val fightResult = Array(n) { Array(n) { 0 }.toIntArray() }
        results.forEach { (win, lose) ->
            fightResult[win - 1][lose - 1] = 1
            fightResult[lose - 1][win - 1] = -1
        }
        return fightResult
    }
}