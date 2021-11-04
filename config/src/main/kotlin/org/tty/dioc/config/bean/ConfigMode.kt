package org.tty.dioc.config.bean

import org.tty.dioc.annotation.DebugOnly
import org.tty.dioc.config.schema.ConfigRule
import org.tty.dioc.config.schema.ConfigRuleApi

/**
 * mode of the config
 */
@ConfigRuleApi(configRule = ConfigRule.CodeReadOnly)
data class ConfigMode(
    /**
     * whether open annotation support, by default **open**.
     */
    val annotation: Boolean = true,

    /**
     * whether open file support, by default **open**.
     */
    val file: Boolean = true,

    /**
     * test string
     */
    @DebugOnly
    val test: String = "testString"
)