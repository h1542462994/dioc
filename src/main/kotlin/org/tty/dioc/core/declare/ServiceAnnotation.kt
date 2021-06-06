package org.tty.dioc.core.declare

import org.tty.dioc.core.lifecycle.LifeCycle

/**
 * the real data for [Service]
 */
data class ServiceAnnotation(
    val lifeCycle: LifeCycle = LifeCycle.Singleton,
    val lazy: Boolean = true
) {
    companion object {
        fun fromService(service: Service): ServiceAnnotation {
            return ServiceAnnotation(lifeCycle = service.lifecycle, lazy = service.lazy)
        }

        fun <T> fromClass(clazz: Class<T>): ServiceAnnotation {
            val service = clazz.annotations.filterIsInstance<Service>().firstOrNull()
            requireNotNull(service)

            return fromService(service)
        }
    }
}