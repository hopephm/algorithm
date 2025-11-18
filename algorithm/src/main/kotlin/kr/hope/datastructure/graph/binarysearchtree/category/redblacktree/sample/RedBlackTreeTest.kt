package kr.hope.datastructure.graph.binarysearchtree.category.redblacktree.sample

import java.rmi.UnexpectedException
import java.util.TreeSet
import kotlin.reflect.KClass

/**
 * @note TreeMap 은 모듈단위 접근이 차단되어 있어 JVM option "--add-opens=java.base/java.util=ALL-UNNAMED" 활성화 필요
 * @see ../red-black-tree-summary.md: 삽입 후 Fix-up
 */
fun main() {
    RedBlackTreeTest.recoloringTest()
    RedBlackTreeTest.restructuringTest()
}

object RedBlackTreeTest {
    private val accessor = TreeSetNodeAccessor()

    fun recoloringTest() {
        // given
        val treeSet = TreeSet<Int>()
        val samples = listOf(50,30,70)
        treeSet.addAll(samples)

        val originRoot = accessor.getRoot(treeSet) ?: throw IllegalStateException("루트 노드는 존재해야 합니다.")

        // when
        treeSet.add(25)

        // then
        originRoot.validate(key = 50, isBlack = true)
        originRoot.leftChild.validate(key = 30, isBlack = false)
        originRoot.rightChild.validate(key = 70, isBlack = false)

        val updatedRoot = accessor.getRoot(treeSet) ?: throw IllegalStateException("루트 노드는 존재해야 합니다.")
        updatedRoot.validate(key = 50, isBlack = true)
        updatedRoot.leftChild.validate(key = 30, isBlack = true)
        updatedRoot.rightChild.validate(key = 70, isBlack = true)
        updatedRoot.leftChild!!.leftChild.validate(key = 25, isBlack = false)
    }

    fun restructuringTest() {
        // given
        val treeSet = TreeSet<Int>()
        val samples = listOf(50,30,70,25)
        treeSet.addAll(samples)

        val originRoot = accessor.getRoot(treeSet) ?: throw IllegalStateException("루트 노드는 존재해야 합니다.")

        // when
        treeSet.add(10)

        // then
        originRoot.validate(key = 50, isBlack = true)
        originRoot.leftChild.validate(key = 30, isBlack = true)
        originRoot.rightChild.validate(key = 70, isBlack = true)
        originRoot.leftChild!!.leftChild.validate(key = 25, isBlack = false)

        val updatedRoot = accessor.getRoot(treeSet) ?: throw IllegalStateException("루트 노드는 존재해야 합니다.")
        updatedRoot.validate(key = 50, isBlack = true)
        updatedRoot.leftChild.validate(key = 25, isBlack = true)
        updatedRoot.rightChild.validate(key = 70, isBlack = true)
        updatedRoot.leftChild!!.leftChild.validate(key = 10, isBlack = false)
        updatedRoot.leftChild!!.rightChild.validate(key = 30, isBlack = false)
    }

    private fun RedBlackTreeNode?.validate(key: Int, isBlack: Boolean) {
        when {
            this == null -> throw UnexpectedException("해당 노드가 존재하지 않습니다.")
            this.key != key -> throw UnexpectedException("예상한 key($key)와 실제 key(${this.key})가 일치하지 않습니다.")
            this.isBlack != isBlack -> throw UnexpectedException("예상한 color(${isBlack.color()})와 실제 color(${this.isBlack.color()})가 일치하지 않습니다.")
        }
    }
}

data class RedBlackTreeNode(
    val key: Int,
    var leftChild: RedBlackTreeNode? = null,
    var rightChild: RedBlackTreeNode? = null,
    val isBlack: Boolean,
) {
    fun Boolean.color() = if (this) "Black" else "Red"
}

class TreeSetNodeAccessor {
    fun getRoot(treeSet: TreeSet<Int>): RedBlackTreeNode? {
        val treeMap = treeSet.getField("m", treeSet::class)
        val root = treeMap?.getField("root", treeMap::class)
        return root?.toNode(root::class)
    }

    private fun Any.getField(fieldName: String, clazz: KClass<*>): Any? {
        return clazz.java.getDeclaredField(fieldName).also { field ->
            field.isAccessible = true
        }.get(this)
    }

    private fun Any.toNode(clazz: KClass<*>): RedBlackTreeNode {
        return RedBlackTreeNode(
            key = this.getField("key", clazz).toString().toInt(),
            leftChild = this.getField("left", clazz).takeIf { it != null }?.toNode(clazz),
            rightChild = this.getField("right", clazz).takeIf { it != null }?.toNode(clazz),
            isBlack = this.getField("color", clazz).toString().toBoolean(),
        )
    }
}