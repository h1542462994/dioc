package org.tty.dioc.core.launcher

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.core.basic.BasicComponentStorage
import org.tty.dioc.core.basic.ComponentStorage
import org.tty.dioc.core.basic.ProviderResolver
import org.tty.dioc.core.basic.findInternalComponent
import org.tty.dioc.core.key.NamedComponentKey
import org.tty.dioc.core.key.NamedSingletonKey
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object BasicComponentKeys {
    val configSchemas = ConfigSchemas::class.qualifiedName as String
    val providerResolver = ProviderResolver::class.qualifiedName as String

    const val configModule = "<module>::org.tty.dioc.config"
    const val basicComponentStorage = "<self>::org.tty.dioc.basic.BasicComponentStorage"
}


val BasicComponentStorage.configSchemas: ConfigSchemas get() = this.getComponent(BasicComponentKeys.configSchemas)
val BasicComponentStorage.providerResolver: ProviderResolver get() = this.getComponent(BasicComponentKeys.providerResolver)

val ComponentStorage.configSchemas: ConfigSchemas get() = findInternalComponent(BasicComponentKeys.configSchemas)
val ComponentStorage.providerResolver: ProviderResolver get() = findInternalComponent(BasicComponentKeys.providerResolver)