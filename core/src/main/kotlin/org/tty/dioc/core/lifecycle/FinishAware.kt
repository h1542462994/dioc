package org.tty.dioc.core.lifecycle

import org.tty.dioc.core.declare.Once
import org.tty.dioc.core.declare.Component

/**
 * the lifecycle function [onFinish]
 * the [onFinish] will be called once it is deconstructed
 * @see [Component]
 */
interface FinishAware {
    /**
     * will be called once it is deconstructed
     */
    @Once
    fun onFinish()
}