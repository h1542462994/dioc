package org.tty.dioc.core

import org.tty.dioc.core.declare.Lifecycle
import org.tty.dioc.core.lifecycle.Scope
import kotlin.reflect.KClass

class DefaultDynamicApplicationContext: DynamicApplicationContext {
    override fun <T : Any> getService(declareType: KClass<T>): T {
        TODO("Not yet implemented")
    }

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

    override fun <T : Any> addSingleton(type: KClass<T>, lazy: Boolean) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addSingleton(
        declareType: KClass<TD>,
        implementationType: KClass<TI>,
        lazy: Boolean,
    ) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> addScoped(type: KClass<T>) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addScoped(declareType: KClass<TD>, implementationType: KClass<TI>) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> addTransient(type: KClass<T>) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addTransient(declareType: KClass<TD>, implementationType: KClass<TI>) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> replaceImplementation(
        declareType: KClass<TD>,
        implementationType: KClass<TI>,
        lifecycle: Lifecycle?,
    ) {
        TODO("Not yet implemented")
    }
}