package org.tty.dioc.config.keys

/**
 * visitor for [KeySchema].
 * if you want to register a key, you could invoke [config].
 *
 */
object ConfigKeys {
    private val store = HashMap<String, KeySchema>()

    fun <T: KeySchema> config(value: T): T {
        store[value.name] = value
        return value
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T: KeySchema> get(name: String): T? {
        val value = store[name]

        return if (value == null) {
            null
        } else {
            value as T
        }
    }
}