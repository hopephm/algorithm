package kr.hope.data.structure.string.trie.solution.programmers

/**
 * @see https://school.programmers.co.kr/learn/courses/30/lessons/60060
 */
class SearchLyrics {
    fun solution(words: Array<String>, queries: Array<String>): IntArray {
        val trie = Trie()
        val reversedTrie = Trie()

        words.forEach { word ->
            trie.insert(word)
            reversedTrie.insert(word.reversed())
        }

        return queries.map { query ->
            when {
                query.last() == '?' -> trie.searchWordCount(query)
                else -> reversedTrie.searchWordCount(query.reversed())
            }
        }.toIntArray()
    }
}

typealias Depth = Int
typealias Count = Int

class Trie {
    private val root = Node()

    fun insert(word: String) {
        var current = root
        word.forEachIndexed { idx, char ->
            val depth = word.length - idx
            current.increaseWordCount(depth)
            current.addChildIfAbsent(char)
            current = current.children[char]!!
        }
    }

    fun searchWordCount(word: String): Int {
        val wildCardIdx = word.indexOf('?')
        val wildCardCount = word.slice(wildCardIdx until word.length).length

        val prefix = word.slice(0 until wildCardIdx)

        return findNode(prefix)?.wordCount?.get(wildCardCount) ?: 0
    }

    private fun findNode(prefix: String): Node? {
        var current = root
        prefix.forEach { char ->
            current = current.children[char] ?: return null
        }
        return current
    }

    class Node(
        val wordCount: MutableMap<Depth, Count> = mutableMapOf(),
        val children: MutableMap<Char, Node> = mutableMapOf(),
    ) {
        fun increaseWordCount(depth: Int) {
            wordCount[depth] = (wordCount[depth] ?: 0) + 1
        }

        fun addChildIfAbsent(char: Char) {
            if (children[char] == null) {
                children[char] = Node()
            }
        }
    }
}