package org.tty.dioc.core.scope

import org.tty.dioc.core.basic.ScopeFactory
import java.util.*

class DefaultScopeFactory: ScopeFactory {
    override fun create(): Scope {
        return object: Scope {
            override val id: String
                get() = UUID.randomUUID().toString()

            override fun toString(): String {
                return "default:$id"
            }
        }
    }
}