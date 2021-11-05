package org.tty.dioc.core.lifecycle

import org.tty.dioc.core.basic.ScopeAbility
import org.tty.dioc.core.basic.ScopeFactory
import org.tty.dioc.observable.channel.Channels

/**
 * the trace of the [Scope],
 * the scope is placed by stack.
 */
class StackScopeTrace(private val scopeFactory: ScopeFactory): ScopeAbility {

    private val currentScope: ThreadLocal<Scope?> = ThreadLocal()
    private val scopeRecords: ThreadLocal<ArrayList<Scope>?> = ThreadLocal<ArrayList<Scope>?>()
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
        val records = fetchRecords()
        var existed = true

        if (!records.contains(scope)) {
            existed = false
            records.add(scope)
        } else {
            records.remove(scope)
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
            val scope = records.last()
            records.removeLast()
            currentScope.set(records.lastOrNull())

            if (!records.contains(scope)) {
                removeChannel.emit(scope)
            }
        }
    }

    override fun endScope(scope: Scope) {
        throw IllegalStateException("function not supported.")
    }

    private fun ensureInitialized() {
        if (scopeRecords.get() == null) {
            scopeRecords.set(arrayListOf())
        }
    }

    private fun fetchRecords(): ArrayList<Scope> {
        ensureInitialized()
        return scopeRecords.get()!!
    }
}