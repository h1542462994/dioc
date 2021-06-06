package org.tty.dioc.core.storage

import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.declare.ServiceElement
import org.tty.dioc.core.util.ServiceUtil

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

    companion object {
        fun fromServiceElements(serviceElements: List<ServiceElement>): ServiceDeclarations {
            val serviceDeclarations = ArrayList<ServiceDeclare>()

            for (i in serviceElements.indices) {
                serviceDeclarations.add(ServiceDeclare(
                    serviceElements[i],

                ))
            }

        }
    }

}