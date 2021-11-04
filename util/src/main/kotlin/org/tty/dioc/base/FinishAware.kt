package org.tty.dioc.base

import org.tty.dioc.annotation.Once
import org.tty.dioc.annotation.Component

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