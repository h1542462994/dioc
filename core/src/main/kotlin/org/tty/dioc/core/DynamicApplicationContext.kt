package org.tty.dioc.core

import org.tty.dioc.core.declare.ServiceDeclareAware

/**
 * represents a container for ability of getting and declare service.
 */
interface DynamicApplicationContext: ApplicationContext, ServiceDeclareAware