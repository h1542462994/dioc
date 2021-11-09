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
    var annotation: Boolean = true,

    /**
     * whether open file support, by default **open**.
     */
    var file: Boolean = true,

    /**
     * test string
     */
    @DebugOnly
    @ConfigRuleApi(configRule = ConfigRule.Declare)
    //@ConfigSavable
    var test: String = "testString"
)