
package org.tty.dioc.core.declare

import kotlin.reflect.KClass

/**
 * find in collection where [ServiceDeclare.declarationTypes] contains [declareType]
 */
fun List<ServiceDeclare>.findByDeclare(declareType: KClass<*>): ServiceDeclare {
    return this.single { it.declarationTypes.contains(declareType) }
}

/**
 * find in collection where [ServiceDeclare.serviceType] == [serviceType]
 */
fun List<ServiceDeclare>.findByService(serviceType: KClass<*>): ServiceDeclare {
    return this.single { it.serviceType == serviceType }
}