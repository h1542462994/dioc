package org.tty.dioc.core.internal

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.base.Init
import org.tty.dioc.core.basic.BasicComponentStorage
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

@InternalComponent
class BasicComponentStorageImpl : BasicComponentStorage {
    private val store = HashMap<String, Any>()
    private val nameTypeRef = HashMap<KClass<*>, String>()

    override fun <T : Any> addComponent(name: String, component: T) {
        require(!store.containsKey(name)) {
           "component $name is already added."
        }
        require(component::class.hasAnnotation<InternalComponent>()) {
            "you could only add InternalComponent to Basic ComponentStorage"
        }
        // init the component with Init.
        if (component is Init) {
            // initialize the component.
            component.init()
        }
        store[name] = component
        nameTypeRef[component::class] = name
    }

    override fun <T : Any> addComponent(name: String, interfaceType: KClass<T>, component: T) {
        TODO("Not yet implemented")
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getComponent(name: String): T {
        require (store.containsKey(name)) {
            "component $name is not found."
        }
        return store[name] as T
    }

    override fun <T : Any> getComponent(type: KClass<T>): T {
        require (nameTypeRef.containsKey(type)) {
            "component $type is not found."
        }
        return getComponent(nameTypeRef.getValue(type))
    }
}