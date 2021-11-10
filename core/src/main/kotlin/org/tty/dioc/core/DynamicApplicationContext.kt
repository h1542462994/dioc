package org.tty.dioc.core

import org.tty.dioc.core.declare.ComponentDeclareAware

/**
 * represents a container for ability of getting and declare service.
 */
@Deprecated("not ready to use.")
sealed interface DynamicApplicationContext: ApplicationContext, ComponentDeclareAware