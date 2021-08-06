package org.tty.dioc.core.lifecycle

import org.tty.dioc.core.declare.Service

/**
 * the lifecycle function onInit
 * if is is on service
 * the onInit will be called once it is constructed
 * @see [Service]
 */
interface InitializeAware {
    fun onInit()
}