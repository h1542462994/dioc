package org.tty.dioc.core.declare

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
    val type: Class<*>,
    /**
     * the place to inject.
     */
    val injectPlace: InjectPlace
)