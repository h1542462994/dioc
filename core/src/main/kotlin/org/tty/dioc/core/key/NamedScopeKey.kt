package org.tty.dioc.core.key

import org.tty.dioc.annotation.Component
import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.lifecycle.Scope
import kotlin.reflect.KClass

@Component(lifecycle = Lifecycle.Scoped)
data class NamedScopeKey(
    override val indexType: KClass<*>,
    override val name: String,
    override val scope: Scope
): NamedComponentKey, TypedComponentKey, ScopedComponentKey