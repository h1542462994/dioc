package org.tty.dioc.advice

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * the interface resolver for no component related service
 */
data class EmptyDependentResolver(
    val componentType: KClass<*>
): KernelInterfaceResolver {
    override fun dependentComponents(): List<KClass<*>> {
        return listOf()
    }

    override fun resolve(components: List<Any>): Any {
        return componentType.createInstance()
    }
}
