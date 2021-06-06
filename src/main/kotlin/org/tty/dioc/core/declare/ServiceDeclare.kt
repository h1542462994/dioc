package org.tty.dioc.core.declare

import java.lang.reflect.Constructor

/**
 * the declaration of the service
 */
class ServiceDeclare(
    val serviceElement: ServiceElement,
    val constructor: Constructor<*>,
    val singletonComponents: List<PropertyComponent>,
    val transientComponents: List<PropertyComponent>,
    val scopedComponents: List<PropertyComponent>
) {
    val lifeCycle = serviceElement.serviceAnnotation.lifeCycle
    val lazy = serviceElement.serviceAnnotation.lazy
}