package org.tty.dioc.core.internal

import org.tty.dioc.base.InitSuperComponent
import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.core.basic.ComponentDeclareAware
import org.tty.dioc.core.basic.ComponentDeclares
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.error.notProvided
import org.tty.dioc.observable.channel.contract.ChannelFull
import org.tty.dioc.util.factoryOfNotProvided
import kotlin.reflect.KClass

internal class ComponentDeclaresInternalImpl(
    private val applicationConfig: ApplicationConfig
): ComponentDeclares, InitSuperComponent<ComponentDeclares>
,ComponentDeclareAware by factoryOfNotProvided("componentDeclareImpl not support."){
    lateinit var superComponent: ComponentDeclares

    override fun singleIndexType(indexType: KClass<*>): ComponentDeclare {
        notProvided("componentDeclareImpl not support.")
    }

    override fun singleServiceType(realType: KClass<*>): ComponentDeclare {
        notProvided("componentDeclareImpl not support.")
    }

    override fun singleIndexTypeOrNull(indexType: KClass<*>): ComponentDeclare? {
        notProvided("componentDeclareImpl not support.")
    }

    override fun singleName(name: String): ComponentDeclare {
        notProvided("componentDeclareImpl not support.")
    }

    override fun singleNameOrNull(name: String): ComponentDeclare? {
        notProvided("componentDeclareImpl not support.")
    }

    override fun check(componentDeclare: ComponentDeclare) {
        notProvided("componentDeclareImpl not support.")
    }

    override fun addAll(componentDeclares: List<ComponentDeclare>) {
        notProvided("componentDeclareImpl not support.")
    }

    override val createEvent: ChannelFull<ComponentDeclare>
        get() = superComponent.createEvent as ChannelFull<ComponentDeclare>
    override val removeEvent: ChannelFull<ComponentDeclare>
        get() = superComponent.removeEvent as ChannelFull<ComponentDeclare>

    override fun iterator(): Iterator<ComponentDeclare> {
        notProvided("componentDeclareImpl not support.")
    }

    override fun initSuper(superComponent: ComponentDeclares) {
        this.superComponent = superComponent
    }
}