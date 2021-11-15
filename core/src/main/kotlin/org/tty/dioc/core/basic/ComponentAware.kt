package org.tty.dioc.core.basic

import org.tty.dioc.core.ApplicationContext
import kotlin.reflect.KClass
import org.tty.dioc.core.declare.ComponentDeclare

/**
 * aware for getting component
 * @see [ApplicationContext]
 */
interface ComponentAware {
    /**
     * get the anonymous component by [indexType]
     * @param indexType **index type**, index type can be found by [ComponentAware].
     * @see ComponentDeclare
     */
    fun <T: Any> getComponent(indexType: KClass<T>): T

    /**
     * get the component by [name] and [indexType]
     * @param name identify of the component.
     * @param indexType **index type**, index type can be found by [ComponentAware].
     * @see ComponentDeclare
     */
    fun <T: Any> getComponent(name: String, indexType: KClass<T>): T
}