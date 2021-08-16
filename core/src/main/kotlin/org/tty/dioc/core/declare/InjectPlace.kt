package org.tty.dioc.core.declare

/**
 * inject type of the property
 * @see [ServiceDeclare]
 * @see [ServiceProperty]
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