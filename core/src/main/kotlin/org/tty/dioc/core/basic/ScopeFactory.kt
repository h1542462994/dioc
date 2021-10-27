package org.tty.dioc.core.basic

import org.tty.dioc.base.Builder
import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.core.lifecycle.Scope

/**
 * factory of [Scope]
 */
@InternalComponent
interface ScopeFactory: Builder<Scope>