package org.tty.dioc.core.lifecycle

import org.tty.dioc.annotation.Once
import org.tty.dioc.annotation.Component

/**
 * the lifecycle function [onInit]
 * the [onInit] will be called once it is constructed
 * @see [Component]
 */
interface InitializeAware {
    /**
     * will be called once it is constructed
     */
    @Once
    fun onInit()
}