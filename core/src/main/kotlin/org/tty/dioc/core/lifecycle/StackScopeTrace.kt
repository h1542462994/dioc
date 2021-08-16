package org.tty.dioc.core.lifecycle

import org.tty.dioc.observable.channel.contract.Channel
import org.tty.dioc.observable.channel.Channels
import org.tty.dioc.base.Builder

/**
 * the trace of the [Scope],
 * the scope is placed by stack.
 * TODO: add scope creation callback.
 */
class StackScopeTrace(private val scopeFactory: Builder<Scope>): ScopeAbility {

    private val currentScope: ThreadLocal<Scope?> = ThreadLocal()
    private val scopeRecords: ThreadLocal<ArrayList<Scope>?> = ThreadLocal<ArrayList<Scope>?>()
    private val createChannel = Channels.create<Scope>()
    private val removeChannel = Channels.create<Scope>()
    private val currentChannel = Channels.create<Scope>()

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
        }

        currentScope.set(scope)
        if (!existed) {
            createChannel.emit(scope)
        }
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

    override fun createChannel(): Channel<Scope> {
        return createChannel.next()
    }

    override fun removeChannel(): Channel<Scope> {
        return removeChannel.next()
    }

    override fun currentChannel(): Channel<Scope> {
        return currentChannel.next()
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

    private fun setCurrentScope(scope: Scope) {
        currentScope.set(scope)
        currentChannel.emit(scope)
    }
}