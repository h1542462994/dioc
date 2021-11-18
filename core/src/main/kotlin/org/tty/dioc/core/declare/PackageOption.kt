package org.tty.dioc.core.declare

/**
 * the option of the scanning on package.
 */
data class PackageOption(
    /**
     * package name, must be a unified name.
     */
    val name: String,
    /**
     * whether to open the inclusive option to scan.
     */
    val inclusive: Boolean
)