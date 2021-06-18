package org.tty.dioc.core.advice

import kotlin.reflect.KClass

/**
 * descriptor for resolving extension interface type.
 */
interface KernelInterfaceResolver {
    /**
     * define dependent components class
     * the component should be declare type
     */
    fun dependentComponents(): List<KClass<*>>

    /**
     * resolve the interface to real component
     */
    fun resolve(components: List<Any>): Any
}