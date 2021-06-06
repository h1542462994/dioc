package org.tty.dioc.core.declare

/**
 * the inject type of the property
 */
enum class InjectPlace {
    /**
     * inject on constructor
     */
    Constructor,

    /**
     * inject on property
     */
    Property,

    /**
     * inject on property with [Inject]
     */
    InjectProperty
}