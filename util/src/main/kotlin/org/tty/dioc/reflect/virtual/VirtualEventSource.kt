package org.tty.dioc.reflect.virtual

import org.tty.dioc.reflect.virtual.operator.BoolKind
import org.tty.dioc.reflect.virtual.operator.CompareKind
import org.tty.dioc.reflect.virtual.operator.NumberKind
import kotlin.reflect.KClass

interface VirtualEventSource<T> {
    fun onVirtualCompare(kind: CompareKind, left: Virtual<T>, right: Virtual<T>, result: Virtual<Boolean>)
    fun onVirtualBool(kind: BoolKind, left: Virtual<T>, right: Virtual<T>?, result: Virtual<Boolean>)
    fun onVirtualNumber(kind: NumberKind, left: Virtual<T>, right: Virtual<T>, result: Virtual<T>)
    fun onVirtualGetProperty(property: String, call: Virtual<T>, result: Virtual<*>, resultType: KClass<*>)
    fun onVirtualSetProperty(property: String, call: Virtual<T>, arg: Virtual<*>, argType: KClass<*>)
}