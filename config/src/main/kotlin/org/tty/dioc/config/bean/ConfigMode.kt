package org.tty.dioc.config.bean

import org.tty.dioc.base.DebugOnly
import org.tty.dioc.config.schema.ConfigRule
import org.tty.dioc.config.schema.ConfigRuleApi

/**
 * mode of the config
 */
@ConfigRuleApi(configRule = ConfigRule.CodeReadOnly)
data class ConfigMode(
    /**
     * whether open annotation support, by default, it is **true**.
     */
    val annotation: Boolean = true,

    /**
     * whether open file support
     */
    val file: Boolean = true,

    /**
     * test string
     */
    @DebugOnly
    val test: String = "testString"
)