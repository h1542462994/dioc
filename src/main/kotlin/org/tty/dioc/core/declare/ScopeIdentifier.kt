package org.tty.dioc.core.declare

import org.tty.dioc.core.lifecycle.Scope

data class ScopeIdentifier(
    val type: Class<*>,
    val scope: Scope
)