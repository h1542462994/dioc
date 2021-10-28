package org.tty.dioc.core.basic

/**
 * to resolve the provider
 */
interface ProviderResolver {
    fun <T : Any> resolveProvider(name: String): T
}