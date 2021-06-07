package org.tty.dioc.core.declare

import org.tty.dioc.core.lifecycle.Scope
import kotlin.reflect.KClass

data class ScopeIdentifier(
    val type: KClass<*>,
    val scope: Scope
)