package org.tty.dioc.core.key

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.declare.ComponentDeclareType
import org.tty.dioc.core.scope.Scope
import kotlin.reflect.KClass

/**
 * the identifier of the service which lifecycle is [Lifecycle.Scoped]
 * @see [Lifecycle]
 */
data class ScopeKey(
    /**
     * the type of service (runtime)
     */
    override val indexType: KClass<*>,
    override val name: String,
    /**
     * the scope
     */
    override val scope: Scope,

): ComponentKey {
    override val lifecycle: Lifecycle
        get() = Lifecycle.Scoped
    override val declareType: ComponentDeclareType
        get() = ComponentDeclareType.TypeDeclare
}