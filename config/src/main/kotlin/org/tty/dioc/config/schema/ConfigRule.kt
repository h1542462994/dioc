package org.tty.dioc.config.schema

/**
 * rule for config
 */
@Suppress("unused")
enum class ConfigRule {
    /**
     * not assigned config rule. a placeholder for padding.
     * real configRule **must be assigned**.
     */
    NoAssigned,
    /**
     * config can only initialize on [ConfigSchema].
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