package org.tty.dioc.core

import org.tty.dioc.core.basic.ComponentAware
import org.tty.dioc.base.InitializeAware
import org.tty.dioc.core.lifecycle.ScopeAware

/**
 * context for application
 */
interface ApplicationContext : ComponentAware, ScopeAware, InitializeAware

