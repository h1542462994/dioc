package org.tty.dioc.annotation

/**
 * the lifecycle of the service.
 * @see [Component]
 */
enum class Lifecycle {
    /**
     * the service will be equal any time.
     */
    Singleton,

    /**
     * the service will be a new instance once been called.
     */
    Transient,

    /**
     * the service will be equal in same scope, but not equal in different scope.
     */
    Scoped
}