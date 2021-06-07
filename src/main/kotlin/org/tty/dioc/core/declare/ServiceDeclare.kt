package org.tty.dioc.core.declare

import kotlin.reflect.KFunction

/**
 * the declaration of the service
 */
class ServiceDeclare(
    val serviceElement: ServiceElement,
    val constructor: KFunction<*>,
    val singletonComponents: List<PropertyComponent>,
    val transientComponents: List<PropertyComponent>,
    val scopedComponents: List<PropertyComponent>


) {
    val lifeCycle = serviceElement.serviceAnnotation.lifeCycle
    val lazy = serviceElement.serviceAnnotation.lazy
    fun componentsOn(injectPlace: InjectPlace): List<PropertyComponent> {
        return singletonComponents.plus(transientComponents).plus(scopedComponents)
            .filter { it.injectPlace == injectPlace }
    }
}