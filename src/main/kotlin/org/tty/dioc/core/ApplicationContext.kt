package org.tty.dioc.core

import org.tty.dioc.core.lifecycle.ScopeAware
import kotlin.reflect.KType
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

/**
 * the container for ioc.
 *
 */
interface ApplicationContext : ServiceAware, ScopeAware {

}

