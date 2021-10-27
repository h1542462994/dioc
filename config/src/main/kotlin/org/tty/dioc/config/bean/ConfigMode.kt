package org.tty.dioc.config.bean

/**
 * mode of the config
 */
data class ConfigMode(
    /**
     * whether open annotation support, by default, it is **true**.
     */
    var annotation: Boolean = true,

    /**
     * whether open file support
     */
    var file: Boolean = true,

)