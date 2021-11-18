package org.tty.dioc.base

/**
 * declare the explicit super class for dynamic proxy.
 */
interface InitSuperComponent<T> {
    fun initSuper(superComponent: T)
}