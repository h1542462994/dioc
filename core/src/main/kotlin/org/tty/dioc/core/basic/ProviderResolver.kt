package org.tty.dioc.core.basic

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.annotation.NoInfer
import org.tty.dioc.config.schema.ProvidersSchema

/**
 * to resolve the provider
 * @see [ProvidersSchema]
 */
@InternalComponent
interface ProviderResolver {
    fun <@NoInfer T : Any> resolveProvider(name: String): T
}