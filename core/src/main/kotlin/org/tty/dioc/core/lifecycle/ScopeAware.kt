package org.tty.dioc.core.lifecycle

/**
 * provide the scope ability
 */
interface ScopeAware {
    /**
     * the implementation of the [Scope]
     * @see [ScopeAbility]
     */
    fun scopeAbility(): ScopeAbility

    /**
     * get the current scope
     * @return the current scope, null means not under scope
     */
    fun currentScope(): Scope? {
        return scopeAbility().currentScope()
    }

    /**
     * to begin a scope
     * usually it is generated by [org.tty.dioc.base.Builder&lt;Scope&gt;]
     */
    fun beginScope(): Scope {
        return scopeAbility().beginScope()
    }

    /**
     * to end the scope,
     * the current scope will be changed to last pushed.
     */
    fun endScope() {
        scopeAbility().endScope()
    }

    /**
     * to enter a scope and do [action] then finish it.
     */
    fun withScope(action: (Scope) -> Unit) {
        scopeAbility().withScope(action)
    }
}