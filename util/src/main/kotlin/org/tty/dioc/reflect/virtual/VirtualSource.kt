package org.tty.dioc.reflect.virtual

interface VirtualSource {
    fun <T> createVirtual(real: T): Virtual<T>
}