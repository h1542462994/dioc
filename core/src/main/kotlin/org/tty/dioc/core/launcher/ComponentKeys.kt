package org.tty.dioc.core.launcher

import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.core.CoreModule
import org.tty.dioc.core.basic.ComponentStorage
import org.tty.dioc.core.basic.ProviderResolver
import org.tty.dioc.core.basic.getInternalComponent
import org.tty.dioc.util.Logger

object ComponentKeys {
    val configSchemas = ConfigSchemas::class.qualifiedName as String
    val providerResolver = ProviderResolver::class.qualifiedName as String

    const val configModule = "<module>::org.tty.dioc.config"
    const val coreModule = "<module>::org.tty.dioc.core"
    const val componentStorage = "<self>::org.tty.dioc.basic.ComponentStorage"
}

val ComponentStorage.configSchemas: ConfigSchemas get() = getInternalComponent(ComponentKeys.configSchemas)
val ComponentStorage.providerResolver: ProviderResolver get() = getInternalComponent(ComponentKeys.providerResolver)
val ComponentStorage.componentStorage: ComponentStorage get() = getInternalComponent(ComponentKeys.componentStorage)
val ComponentStorage.logger: Logger get() = getInternalComponent(CoreModule.loggerSchema.name)