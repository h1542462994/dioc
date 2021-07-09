package org.tty.dioc.core.lifecycle

interface ScopeAware {
    fun scopeAbility(): ScopeAbility

    fun currentScope(): Scope? {
        return scopeAbility().currentScope()
    }

    fun beginScope(): Scope {
        return scopeAbility().beginScope()
    }

    fun endScope() {
        scopeAbility().endScope()
    }

    fun withScope(action: (Scope) -> Unit) {
        scopeAbility().withScope(action)
    }
}