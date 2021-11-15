package org.tty.dioc.core.key

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.error.ServiceConstructException
import org.tty.dioc.core.lifecycle.Scope
import kotlin.reflect.KClass

/**
 * the key for component
 */
interface ComponentKey {
    val name: String
    val scope: Scope?
    val lifecycle: Lifecycle
    val indexType: KClass<*>

    /**
     * whether has [InternalComponent]
     */
    val internal: Boolean
}