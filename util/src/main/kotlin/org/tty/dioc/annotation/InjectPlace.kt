package org.tty.dioc.annotation

/**
 * inject type of the property
 * @see [ComponentDeclare]
 * @see [ComponentProperty]
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