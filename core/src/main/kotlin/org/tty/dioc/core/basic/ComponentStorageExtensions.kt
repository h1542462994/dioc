package org.tty.dioc.core.basic

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.core.declare.ServiceCreated
import org.tty.dioc.core.key.NamedSingletonKey
import org.tty.dioc.core.launcher.BasicComponentKeys
import kotlin.reflect.full.hasAnnotation

inline fun <reified T: Any> ComponentStorage.findInternalComponent(name: String ?= null): T {
    return if (name == null) {
        this.findComponent(T::class) as T
    } else {
        this.findComponent(NamedSingletonKey(name, T::class)) as T
    }
}

inline fun <reified T: Any> ComponentStorage.addInternalComponent(name: String, component: T) {
    require(T::class.hasAnnotation<InternalComponent>()) {
        "you could only add InternalComponent by this way."
    }
    this.withTransaction {
        addFull(
            NamedSingletonKey(name, T::class),
            ServiceCreated(component,
                ComponentDeclare.fromInternalComponentType(T::class, component::class)
            )
        )
    }
}

