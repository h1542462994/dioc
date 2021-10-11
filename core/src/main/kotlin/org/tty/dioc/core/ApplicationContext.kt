package org.tty.dioc.core

import org.tty.dioc.core.declare.ComponentAware
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.core.lifecycle.ScopeAware

/**
 * represents a container for ability of getting service.
 *
 */
interface ApplicationContext : ComponentAware, ScopeAware, InitializeAware
