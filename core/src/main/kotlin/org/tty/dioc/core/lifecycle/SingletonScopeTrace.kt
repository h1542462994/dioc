package org.tty.dioc.core.lifecycle

import org.tty.dioc.base.Builder
import org.tty.dioc.observable.channel.Channels

/**
 * the trace of the [Scope],
 * the scope is placed by singleton.
 */
class SingletonScopeTrace(private val scopeFactory: Builder<Scope>): ScopeAbility {
    private val currentScope: ThreadLocal<Scope?> = ThreadLocal()
    private val scopeRecords: ThreadLocal<ArrayList<Scope>?> = ThreadLocal()
    override val createChannel = Channels.create<Scope>()
    override val removeChannel = Channels.create<Scope>()
    override val currentChannel = Channels.create<Scope?>()

    override fun currentScope(): Scope? {
        return currentScope.get()
    }

    override fun beginScope(): Scope {
        val scope = scopeFactory.create()
        beginScope(scope)
        return scope
    }

    override fun beginScope(scope: Scope) {
        var existed = true

        val records = fetchRecords()
        if (!records.contains(scope)) {
            existed = false
            records.add(scope)
        }
        currentScope.set(scope)

        if (!existed) {
            createChannel.emit(scope)
        }
        currentChannel.emit(scope)
    }

    override fun endScope() {
        val records = fetchRecords()

        if (records.isEmpty()) {
            throw IllegalStateException("the scope is empty.")
        } else {
            val scope = records.removeLast()
            currentScope.set(null)

            removeChannel.emit(scope)
            currentChannel.emit(null)
        }
    }

    override fun endScope(scope: Scope) {
        val records = fetchRecords()
        var removed = false
        if (records.contains(scope)) {
            removed = true
            records.remove(scope)
        }
        currentScope.set(null)

        if (removed) {
            removeChannel.emit(scope)
        }
        currentChannel.emit(null)
    }

    private fun ensureInitialized() {
        if (scopeRecords.get() != null) {
            scopeRecords.set(arrayListOf())
        }
    }

    private fun fetchRecords(): ArrayList<Scope> {
        return scopeRecords.get()!!
    }
}