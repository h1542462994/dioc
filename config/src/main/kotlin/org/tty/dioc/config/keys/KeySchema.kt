package org.tty.dioc.config.keys

import kotlin.reflect.KClass

/**
 * schema of the key.
 * the declaration of the config, registered in [ConfigKeys]
 * @see ConfigKeys
 */
sealed interface KeySchema {
    /**
     * the name of the schema
     */
    val name: String
}

