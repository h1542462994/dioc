package org.tty.dioc.core.lifecycle

import org.tty.dioc.observable.channel.contract.Channel

class SingletonScopeTrace: ScopeAbility {
    override fun currentScope(): Scope? {
        TODO("Not yet implemented")
    }

    override fun beginScope(): Scope {
        TODO("Not yet implemented")
    }

    override fun beginScope(scope: Scope) {
        TODO("Not yet implemented")
    }

    override fun endScope() {
        TODO("Not yet implemented")
    }

    override fun endScope(scope: Scope) {
        TODO("Not yet implemented")
    }

    override fun withScope(action: (Scope) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun createChannel(): Channel<Scope> {
        TODO("Not yet implemented")
    }

    override fun removeChannel(): Channel<Scope> {
        TODO("Not yet implemented")
    }

    override fun currentChannel(): Channel<Scope> {
        TODO("Not yet implemented")
    }
}