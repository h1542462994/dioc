package org.tty.dioc.collection

/**
 * a node has a value and 2 children
 */
data class Node2(
    override val tag: Any?,
    val value: Any,
    val leftChild: Node,
    val rightChild: Node
) : Node {
    override fun toString(): String {
        return "[${value}:${tag}]"
    }
}