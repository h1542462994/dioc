package org.tty.dioc.core.key

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.declare.ComponentDeclareType
import org.tty.dioc.core.scope.Scope
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
    val declareType: ComponentDeclareType
}