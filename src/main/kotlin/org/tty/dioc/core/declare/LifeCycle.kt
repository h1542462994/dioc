package org.tty.dioc.core.declare

/**
 * the lifecycle of the service.
 * if [Singleton], means the service will be equal any time.
 * if [Transient], means the service will be a new instance once been called.
 * if [Scoped], means the service will be equal in same scope, but not equal in different scope.
 * @see [Service]
 */
enum class LifeCycle(var id: Int) {
    Singleton(0),
    Transient(1),
    Scoped(2)
}