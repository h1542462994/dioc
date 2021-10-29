package org.tty.dioc.core.basic

import org.tty.dioc.annotation.InternalComponent

/**
 * to resolve the provider
 */
@InternalComponent
interface ProviderResolver {
    fun <T : Any> resolveProvider(name: String): T
}