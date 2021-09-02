package org.tty.dioc.core.basic

import org.tty.dioc.core.declare.ServiceDeclare

interface ComponentResolver {
    fun <T> resolve(declare: ServiceDeclare): T
}