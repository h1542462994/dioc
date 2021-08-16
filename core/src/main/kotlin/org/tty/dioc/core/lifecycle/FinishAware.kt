package org.tty.dioc.core.lifecycle

import org.tty.dioc.core.declare.Service

/**
 * the lifecycle function [onFinish]
 * the [onFinish] will be called once it is deconstructed
 * @see [Service]
 */
interface FinishAware {
    /**
     * will be called once it is deconstructed
     */
    fun onFinish()
}