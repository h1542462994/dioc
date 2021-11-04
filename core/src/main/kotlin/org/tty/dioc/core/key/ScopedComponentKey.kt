package org.tty.dioc.core.key

import org.tty.dioc.core.lifecycle.Scope

interface ScopedComponentKey {
    val scope: Scope
}