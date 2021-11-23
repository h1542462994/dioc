package org.tty.dioc.core.basic

import org.tty.dioc.core.scope.Scope

internal object InternalScope: Scope {
    override val id: String
        get() = "internalScope"
}