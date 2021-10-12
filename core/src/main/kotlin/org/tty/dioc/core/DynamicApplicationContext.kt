package org.tty.dioc.core

import org.tty.dioc.core.declare.ComponentDeclareAware

/**
 * represents a container for ability of getting and declare service.
 */
sealed interface DynamicApplicationContext: ApplicationContext, ComponentDeclareAware