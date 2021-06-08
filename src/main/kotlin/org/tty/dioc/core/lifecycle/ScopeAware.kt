package org.tty.dioc.core.lifecycle

/**
 * ability to handle scoped services.
 * the scope between thread are never equal
 */
interface ScopeAware {
    /**
     * current Scope
     * @return null means not under scope
     */
    fun currentScope(): Scope?

    /**
     * to begin a Scope
     */
    fun beginScope(): Scope

    /**
     * to begin a Scope
     */
    fun beginScope(scope: Scope)

    /**
     * to end the currentScope
     */
    fun endScope()

    /**
     * to end the scope
     */
    fun endScope(scope: Scope)

    /**
     * to run the scope and then end it.
     */
    fun withScope(action: (Scope) -> Unit)
}