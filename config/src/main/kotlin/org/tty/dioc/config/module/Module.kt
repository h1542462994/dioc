package org.tty.dioc.config.module

import org.tty.dioc.base.InitializeAware

/**
 * module, you should register all components on [onInit]
 * @sample [org.tty.dioc.config.ConfigModule]
 */
interface Module: InitializeAware