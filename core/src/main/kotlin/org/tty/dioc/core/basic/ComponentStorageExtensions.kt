package org.tty.dioc.core.basic

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.schema.ConfigSchema
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.core.declare.ServiceCreated
import org.tty.dioc.core.key.NamedSingletonKey
import kotlin.reflect.full.hasAnnotation

inline fun <reified T: Any> ComponentStorage.findInternalComponent(configSchema: ConfigSchema<T>): T? {
    return this.findInternalComponent(configSchema.name)
}

inline fun <reified T: Any> ComponentStorage.findInternalComponent(name: String): T? {
    require(T::class.hasAnnotation<InternalComponent>()) {
        "you could only get InternalComponent by this way."
    }
    val component = this.findComponent(NamedSingletonKey(name, T::class))
    return component as T?
}

inline fun <reified T: Any> ComponentStorage.getInternalComponent(configSchema: ConfigSchema<T>): T{
    return this.getInternalComponent(configSchema.name)
}

inline fun <reified T: Any> ComponentStorage.getInternalComponent(name: String): T {
    val component: T? = findInternalComponent(name)
    require(component != null) {
        "component is not found"
    }
    return component
}

inline fun <reified T: Any> ComponentStorage.findInternalComponent(): T? {
    require(T::class.hasAnnotation<InternalComponent>()) {
        "you could only get InternalComponent by this way."
    }
    val component = findComponent(T::class)
    return component
}

inline fun <reified T: Any> ComponentStorage.getInternalComponent(): T {
    val component: T? = findInternalComponent()
    require(component != null) {
        "component is not found"
    }
    return component
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

