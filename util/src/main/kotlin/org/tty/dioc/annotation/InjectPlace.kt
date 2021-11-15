package org.tty.dioc.annotation

/**
 * inject type of the property
 */
enum class InjectPlace {
    /**
     * inject on constructor
     */
    Constructor,

    /**
     * inject on property with [Inject]
     */
    InjectProperty
}