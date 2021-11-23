package org.tty.dioc.util

import kotlin.math.max

fun leftBlock(value: Any?, length: Int): String {
    return String.format("%-${length}s", value.toString())
}

fun space(length: Int): String {
    return leftBlock("", length)
}

fun Any?.toTruncateString(): String {
    if (this == null) {
        return "null"
    }
    val str = this.toString()
    val lineSeq = str.lineSequence()
    var lineCount = 0
    var lastLine = ""
    for (line in lineSeq) {
        lineCount ++
        if (lineCount > 1) {
            return "$lastLine..."
        }
        lastLine = line
    }
    return str
}

/**
 * to print [Collection] as a table
 */
fun <T> formatTable(tableName: String, collection: Collection<T>, indent: Int = 4, space: Int = 5, title: List<String>? = null, selector: (T) -> List<*>): CharSequence {
    if (collection.isEmpty() && title == null) {
        return "(empty table)"
    }

    val columnLength = title.optional { size } ?: selector(collection.first()).size
    val sizeList = Array(columnLength) { 0 }

    collection.forEach {
        val v = selector(it)
        require(columnLength == v.size) { "columnSize is not equal." }
        v.forEachIndexed { index, any ->
            sizeList[index] = max(sizeList[index], any.toTruncateString().length + space)
        }
    }
    title?.forEachIndexed { index, s ->
        sizeList[index] = max(sizeList[index], s.toTruncateString().length + space)
    }

    fun buildRow(sb: StringBuilder, value: List<*>) {
        require(value.size == columnLength) { "titleSize is not equal to columnSize." }
        sb.append(space(indent))
        value.forEachIndexed { index, v ->
            sb.append(leftBlock(v.toTruncateString(), sizeList[index]))
        }
    }

    return buildString {
        append("$tableName [\n")
        if (title != null) {
            buildRow(this, title)
            appendLine()
        }
        collection.forEach {
            buildRow(this, selector(it))
            appendLine()
        }
        append("]")
    }
}