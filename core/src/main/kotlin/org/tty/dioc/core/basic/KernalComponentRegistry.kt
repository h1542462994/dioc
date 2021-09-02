package org.tty.dioc.core.basic

import org.tty.dioc.config.keys.ConfigKeys
import org.tty.dioc.config.keys.ProviderKeySchema
import org.tty.dioc.core.storage.CombinedServiceStorage
import org.tty.dioc.core.storage.ServiceStorage
import org.tty.dioc.core.util.ServiceEntry

// this file declares the basic service used.
//region keys
private const val storageKey = "org.tty.dioc.storage"
private const val resolverKey = "org.tty.dioc.resolver"
//endregion

//region schemas
/**
 * the storage components. (immutable)
 * @see [ServiceStorage]
 */
val ConfigKeys.storage: ProviderKeySchema get() = config(ProviderKeySchema(storageKey, ServiceStorage::class, CombinedServiceStorage::class, mutable = false))

/**
 * the resolver components. (immutable)
 */
val ConfigKeys.resolver: ProviderKeySchema get() = config(ProviderKeySchema(resolverKey, ComponentResolver::class, ServiceEntry::class, mutable = false))
//endregion