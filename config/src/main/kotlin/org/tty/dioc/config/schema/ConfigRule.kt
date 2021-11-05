package org.tty.dioc.config.schema

/**
 * rule for config
 */
@Suppress("unused")
enum class ConfigRule {
    /**
     * not assigned config rule.
     */
    NoAssigned,

    /**
     * config can only write on declare.
     */
    Declare,

    /**
     * config can write on declare and runtime(boot only).
     */
    CodeReadOnly,

    /**
     * config can write on declare and runtime.
     */
    CodeMutable,

    /**
     * config can write on declare, runtime, annotation and file (boot only).
     */
    Readonly,

    /**
     * config can write on declare, runtime, annotation and file.
     */
    Mutable,
}