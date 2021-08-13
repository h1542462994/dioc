package org.tty.dioc.core.lifecycle

import org.tty.dioc.observable.channel.contract.Channel

/**
 * ability to handle scoped services.
 * the scope between thread are never equal
 */
interface ScopeAbility {

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

    /**
     * the channel for create the scope.
     */
    fun createChannel(): Channel<Scope>

    /**
     * the channel for remove the scope.
     */
    fun removeChannel(): Channel<Scope>
}