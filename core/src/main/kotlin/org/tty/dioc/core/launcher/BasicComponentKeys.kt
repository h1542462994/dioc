package org.tty.dioc.core.launcher

import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.core.basic.BasicComponentStorage
import org.tty.dioc.core.basic.ProviderResolver

object BasicComponentKeys {
    val configSchemas = ConfigSchemas::class.qualifiedName as String
    val providerResolver = ProviderResolver::class.qualifiedName as String

    const val configModule = "<module>::org.tty.dioc.config"
}

val BasicComponentStorage.configSchemas: ConfigSchemas get() = this.getComponent(BasicComponentKeys.configSchemas)
val BasicComponentStorage.providerResolver: ProviderResolver get() = this.getComponent(BasicComponentKeys.providerResolver)