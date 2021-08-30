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
     * to end the specified scope.
     */
    fun endScope(scope: Scope)

    /**
     * to run the scope and then end it.
     */
    fun withScope(action: (Scope) -> Unit) {
        val scope = beginScope()
        action(scope)
        if (currentScope() != scope) {
            throw IllegalStateException("the scope is not equal to the scope before action.")
        }
        endScope()
    }

    /**
     * to run the scope and then end it
     */
    fun withScope(scope: Scope, action: () -> Unit) {
        beginScope(scope)
        action()
        if (currentScope() != scope) {
            throw IllegalStateException("the scope is not equal to the scope before action.")
        }
        endScope()
    }

    /**
     * the [Channel] for create the [Scope].
     */
    val createChannel: Channel<Scope>


    /**
     * the [Channel] for remove the [Scope].
     */
    val removeChannel: Channel<Scope>

    /**
     * current [Channel] of the [Scope].
     */
    val currentChannel: Channel<Scope?>
}