package org.tty.dioc.core.declare

import kotlin.reflect.KClass

/**
 * to declare the component of the service
 */
data class PropertyComponent(
    /**
     * propertyName to inject
     */
    val name: String,
    /**
     * type of the component
     */
    val type: KClass<*>,
    /**
     * the place to inject.
     */
    val injectPlace: InjectPlace,
    /**
     * the inject place is annotated [Lazy]
     */
    val injectLazy: Boolean
)