package org.tty.dioc.core.lifecycle

import org.tty.dioc.util.Builder
import java.util.*

class DefaultScopeFactory: Builder<Scope> {
    override fun create(): Scope {
        return object: Scope {
            override val id: String
                get() = UUID.randomUUID().toString()
        }
    }
}