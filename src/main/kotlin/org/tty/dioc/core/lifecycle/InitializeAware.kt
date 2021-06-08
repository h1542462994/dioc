package org.tty.dioc.core.lifecycle

/**
 * the lifecycle function onInit
 * if is is on service
 * the onInit will be called once it is constructed
 * @see [org.tty.dioc.core.declare.Service]
 */
interface InitializeAware {
    fun onInit()
}