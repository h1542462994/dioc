package org.tty.dioc.collection

class NodeEntry {
    val nodes = ArrayList<Node>()
    val current: Node
    get() {
        return nodes.last()
    }

    val parent: Node
    get() {
        return nodes[nodes.size - 1]
    }



}
