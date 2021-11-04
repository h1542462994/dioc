package org.tty.dioc.core.internal

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.base.InitializeAware
import org.tty.dioc.core.basic.BasicComponentStorage
import org.tty.dioc.core.launcher.BasicComponentKeys
import org.tty.dioc.util.formatTable
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

@Deprecated("use CombinedComponentStorage instead.")
class BasicComponentStorageImpl : BasicComponentStorage {
    private val store = HashMap<String, Any>()
    private val nameTypeRef = HashMap<KClass<*>, String>()

    override fun <T : Any> addComponent(name: String, component: T) {
        addComponent(name, component::class, component = component)
    }

    override fun <T : Any> addComponent(name: String, interfaceType: KClass<out T>, component: T) {
        require(!store.containsKey(name)) {
            "component $name is already added."
        }
        require(interfaceType.hasAnnotation<InternalComponent>()) {
            "you could only add InternalComponent to BasicComponentStorage"
        }
        // init the component with Init
        if (component is InitializeAware) {
            // initialize the component.
            component.onInit()
        }
        store[name] = component
        nameTypeRef[interfaceType] = name
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getComponent(name: String): T {
        require (store.containsKey(name)) {
            "component $name is not found."
        }
        return store[name] as T
    }

    override fun <T : Any> getComponent(interfaceType: KClass<T>): T {
        require (nameTypeRef.containsKey(interfaceType)) {
            "component $interfaceType is not found."
        }
        return getComponent(nameTypeRef.getValue(interfaceType))
    }

    override fun toString(): String {
        val realList = nameTypeRef.map {
            listOf(it.value, it.key.qualifiedName,
                // expect self to miss recursive kFunction call.
                if (store[it.value] === this) {
                    BasicComponentKeys.basicComponentStorage
                } else {
                    store[it.value]
                }
            )
        }.sortedBy { it[0].toString() }
        return formatTable("${BasicComponentStorageImpl::class.simpleName}", realList, title = listOf("name", "type", "content")) {
            it
        }.toString()
    }
}