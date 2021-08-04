package org.tty.dioc.collection

class RBTree<T: Any>() {
    private var node: Node = nil
    private var comparator: Comparator<T>? = null

    constructor(comparator: Comparator<T>) : this() {
        this.comparator = comparator
    }

    fun add(value: T) {
        if (node == nil) {
            node = Node2(b, value, nil, nil)
        } else {
            val entry = findEntry(value)

        }
    }

    private fun findEntry(value: T): NodeEntry {
        TODO("")
    }

    companion object {
        private const val b = "b"
        private const val r = "r"
        val nil = Nodes.nil(b)
    }
}