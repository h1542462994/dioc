package org.tty.dioc.core.internal

import org.tty.dioc.base.InitSuperComponent
import org.tty.dioc.core.basic.ComponentDeclareAware
import org.tty.dioc.core.basic.ComponentDeclares
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.error.notProvided
import org.tty.dioc.observable.channel.contract.ChannelFull
import kotlin.reflect.KClass

internal class ComponentDeclaresInternalImpl: ComponentDeclares, InitSuperComponent<ComponentDeclares> {
    lateinit var superComponent: ComponentDeclares

    override fun singleIndexType(indexType: KClass<*>): ComponentDeclare {
        TODO("Not yet implemented")
    }

    override fun singleServiceType(realType: KClass<*>): ComponentDeclare {
        TODO("Not yet implemented")
    }

    override fun singleIndexTypeOrNull(indexType: KClass<*>): ComponentDeclare? {
        TODO("Not yet implemented")
    }

    override fun singleName(name: String): ComponentDeclare {
        TODO("Not yet implemented")
    }

    override fun singleNameOrNull(name: String): ComponentDeclare? {
        TODO("Not yet implemented")
    }

    override fun check(componentDeclare: ComponentDeclare) {
        TODO("Not yet implemented")
    }

    override fun addAll(componentDeclares: List<ComponentDeclare>) {
        TODO("Not yet implemented")
    }

    override val createEvent: ChannelFull<ComponentDeclare>
        get() = superComponent.createEvent as ChannelFull<ComponentDeclare>
    override val removeEvent: ChannelFull<ComponentDeclare>
        get() = superComponent.removeEvent as ChannelFull<ComponentDeclare>

    override fun iterator(): Iterator<ComponentDeclare> {
        TODO("Not yet implemented")
    }

    override fun <T : Any> addSingleton(type: KClass<T>, lazy: Boolean) {
        notProvided("internal support has not provided.")
    }

    override fun <T : Any> addSingleton(name: String, type: KClass<T>, lazy: Boolean) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addSingleton(indexType: KClass<TD>, realType: KClass<TI>, lazy: Boolean) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addSingleton(
        name: String,
        indexType: KClass<TD>,
        realType: KClass<TI>,
        lazy: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> addScoped(type: KClass<T>, lazy: Boolean) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> addScoped(name: String, type: KClass<T>, lazy: Boolean) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addScoped(indexType: KClass<TD>, realType: KClass<TI>, lazy: Boolean) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addScoped(
        name: String,
        indexType: KClass<TD>,
        realType: KClass<TI>,
        lazy: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> addTransient(type: KClass<T>) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> addTransient(name: String, type: KClass<T>) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addTransient(indexType: KClass<TD>, realType: KClass<TI>) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addTransient(name: String, indexType: KClass<TD>, realType: KClass<TI>) {
        TODO("Not yet implemented")
    }

    override fun forceReplace(action: (ComponentDeclareAware) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun initSuper(superComponent: ComponentDeclares) {
        this.superComponent = superComponent
    }
}