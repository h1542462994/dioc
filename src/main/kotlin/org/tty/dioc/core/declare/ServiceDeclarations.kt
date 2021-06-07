package org.tty.dioc.core.declare

import org.tty.dioc.core.lifecycle.LifeCycle
import org.tty.dioc.core.error.ServiceDeclarationException
import org.tty.dioc.core.util.ServiceUtil
import kotlin.reflect.KClass

/**
 * the declaration of the services
 */
class ServiceDeclarations(): MutableCollection<ServiceDeclare> {
    private val collection = ArrayList<ServiceDeclare>()

    override val size: Int
        get() = collection.size

    override fun contains(element: ServiceDeclare): Boolean {
        return collection.contains(element)
    }

    override fun containsAll(elements: Collection<ServiceDeclare>): Boolean {
        return collection.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return collection.isEmpty()
    }

    override fun add(element: ServiceDeclare): Boolean {
        return collection.add(element)
    }

    override fun addAll(elements: Collection<ServiceDeclare>): Boolean {
        return collection.addAll(elements)
    }

    override fun clear() {
        return collection.clear()
    }

    override fun iterator(): MutableIterator<ServiceDeclare> {
        return collection.iterator()
    }

    override fun remove(element: ServiceDeclare): Boolean {
        return collection.remove(element)
    }

    override fun removeAll(elements: Collection<ServiceDeclare>): Boolean {
        return collection.removeAll(elements);
    }

    override fun retainAll(elements: Collection<ServiceDeclare>): Boolean {
        return collection.retainAll(elements);
    }

    override fun toString(): String {
        return collection.toString()
    }

    /**
     * to check the declarations
     */
    fun check() {

    }

    fun findByDeclare(declareType: KClass<*>): ServiceDeclare? {
        return collection.find { it.serviceElement.declarationTypes.contains(declareType) }
    }

    fun findByService(serviceType: KClass<*>): ServiceDeclare? {
        return collection.find { it.serviceElement.serviceType == serviceType }
    }

    companion object {
        fun fromServiceElements(serviceElements: List<ServiceElement>): ServiceDeclarations {
            val serviceDeclarations = ArrayList<ServiceDeclare>()

            for (i in serviceElements.indices) {
                val current = serviceElements[i]
                // group the component by lifecycle
                val components = ServiceUtil.getComponents(current.serviceType).groupBy { propertyComponent ->
                    // if the component is not a declare type, then throw.
                    val componentElement = serviceElements.find { it.declarationTypes.contains(propertyComponent.type) }
                        ?: throw ServiceDeclarationException("component is not be declared as a service.")

                    componentElement.serviceAnnotation.lifeCycle
                }


                serviceDeclarations.add(ServiceDeclare(
                    serviceElement = current,
                    constructor = ServiceUtil.getInjectConstructor(current.serviceType),
                    singletonComponents = components.getOrDefault(LifeCycle.Singleton, listOf()),
                    transientComponents = components.getOrDefault(LifeCycle.Transient, listOf()),
                    scopedComponents = components.getOrDefault(LifeCycle.Scoped, listOf())
                ))
            }

            return ServiceDeclarations().apply {
                this.addAll(serviceDeclarations)
            }
        }
    }

}