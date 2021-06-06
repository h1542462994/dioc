package org.tty.dioc.core.declare

/**
 * the declaration of the service
 */
class ServiceDeclare(
    val serviceElement: ServiceElement,
    val singletonComponents: List<PropertyComponent>,
    val transientComponents: List<PropertyComponent>,
    val scopedComponents: List<PropertyComponent>
) {

}