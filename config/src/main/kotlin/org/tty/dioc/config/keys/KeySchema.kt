package org.tty.dioc.config.keys

/**
 * schema of the key.
 * the declaration of the config, registered in [ConfigKeys]
 * @see ConfigKeys
 */
sealed interface KeySchema {
    /**
     * the type of the key stored
     */
    val name: String
}

