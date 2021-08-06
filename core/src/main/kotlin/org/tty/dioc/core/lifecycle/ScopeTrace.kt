package org.tty.dioc.core.lifecycle

import org.tty.dioc.util.Builder

/**
 * the trace of the [Scope],
 * the scope is placed by stack.
 * TODO: add scope creation callback.
 */
class ScopeTrace(private val scopeFactory: Builder<Scope>): ScopeAbility {

    private val currentScope: ThreadLocal<Scope?> = ThreadLocal()
    private val scopeRecords: ThreadLocal<ArrayList<Scope>?> = ThreadLocal<ArrayList<Scope>?>()

    private fun ensureInitialized() {
        if (scopeRecords.get() == null) {
            scopeRecords.set(arrayListOf())
        }
    }

    private fun fetchRecords(): ArrayList<Scope> {
        ensureInitialized()
        return scopeRecords.get()!!
    }

    override fun currentScope(): Scope? {
        return currentScope.get()
    }

    override fun beginScope(): Scope {
        val scope = scopeFactory.create()
        fetchRecords().add(scope)
        currentScope.set(scope)
        return scope
    }

    override fun beginScope(scope: Scope) {
        val records = fetchRecords()
        if (!records.contains(scope)) {
            records.add(scope)
        }
        currentScope.set(scope)
    }

    override fun endScope() {
        val records = fetchRecords()
        records.removeLast()
        currentScope.set(records.lastOrNull())
    }

    override fun endScope(scope: Scope) {
        val records = fetchRecords()
        records.remove(scope)
        currentScope.set(null)
    }

    override fun withScope(action: (Scope) -> Unit) {
        val scope = beginScope()
        action.invoke(scope)
        endScope()
    }
}