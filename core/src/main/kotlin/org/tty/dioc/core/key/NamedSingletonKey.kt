package org.tty.dioc.core.key

import org.tty.dioc.annotation.Component
import org.tty.dioc.annotation.Lifecycle
import kotlin.reflect.KClass

@Component(lifecycle = Lifecycle.Singleton)
data class NamedSingletonKey(
    override val name: String,
    override val indexType: KClass<*>
): NamedComponentKey, TypedComponentKey