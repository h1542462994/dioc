
package org.tty.dioc.core.declare

import org.tty.dioc.core.error.ServiceDeclarationException
import kotlin.reflect.KClass

/**
 * a implementation of [MutableServiceDeclares] and [ReadonlyServiceDeclares]
 */
class ServiceDeclares(serviceDeclares: List<ServiceDeclare>) : MutableServiceDeclares, ReadonlyServiceDeclares {
    private val container = ArrayList<ServiceDeclare>()
    init {
        container.addAll(serviceDeclares)
    }

    override fun iterator(): Iterator<ServiceDeclare> {
        return container.iterator()
    }

    override fun <T : Any> addSingleton(type: KClass<T>, lazy: Boolean) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addSingleton(
        declareType: KClass<TD>,
        implementationType: KClass<TI>,
        lazy: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> addScoped(type: KClass<T>) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addScoped(declareType: KClass<TD>, implementationType: KClass<TI>) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> addTransient(type: KClass<T>) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addTransient(declareType: KClass<TD>, implementationType: KClass<TI>) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> replaceImplementation(
        declareType: KClass<TD>,
        implementationType: KClass<TI>,
        lifecycle: Lifecycle?
    ) {
        TODO("Not yet implemented")
    }

    private fun findByDeclareOrNull(declareType: KClass<*>): ServiceDeclare? {
        return this.singleOrNull { it.declarationTypes.contains(declareType) }
    }

    /**
     * find in collection where [ServiceDeclare.declarationTypes] contains [declareType]
     */
    override fun findByDeclare(declareType: KClass<*>): ServiceDeclare {
        return this.single { it.declarationTypes.contains(declareType) }
    }

    /**
     * find in collection where [ServiceDeclare.serviceType] == [serviceType]
     */
    override fun findByService(serviceType: KClass<*>): ServiceDeclare {
        return this.single { it.serviceType == serviceType }
    }

    /**
     * to check the structure of the service on the current [serviceDeclare]
     */
    override fun check(serviceDeclare: ServiceDeclare) {
        if (serviceDeclare.lifecycle == Lifecycle.Transient && !serviceDeclare.isLazyService) {
            throw ServiceDeclarationException("the transient service must be a lazy service.")
        } else {
            serviceDeclare.components.forEach {
                val aDeclare = this.findByDeclareOrNull(it.declareType)
                if (aDeclare != null && aDeclare.lifecycle == Lifecycle.Scoped && !it.injectLazy) {
                    throw ServiceDeclarationException("you must inject a scoped service by @Lazy")
                }
            }
        }
    }

}