package org.tty.dioc.reflect.virtual.operator

import org.tty.dioc.reflect.virtual.Virtual

operator fun Virtual<Int>.plus(other: Virtual<Int>): Virtual<Int> {
    val realResult = real + other.real
    val result = source.createVirtual(realResult)
    eventSource.onVirtualNumber(NumberKind.Plus, this, other, result)
    return result
}

operator fun Virtual<Int>.minus(other: Virtual<Int>): Virtual<Int> {
    val realResult = real - other.real
    val result = source.createVirtual(realResult)
    eventSource.onVirtualNumber(NumberKind.Minus, this, other, result)
    return result
}

operator fun Virtual<Int>.times(other: Virtual<Int>): Virtual<Int> {
    val realResult = real * other.real
    val result = source.createVirtual(realResult)
    eventSource.onVirtualNumber(NumberKind.Times, this, other, result)
    return result
}

operator fun Virtual<Int>.div(other: Virtual<Int>): Virtual<Int> {
    val realResult = real / other.real
    val result = source.createVirtual(realResult)
    eventSource.onVirtualNumber(NumberKind.Div, this, other, result)
    return result
}

operator fun Virtual<Int>.plus(other: Int): Virtual<Int> {
    return plus(source.createVirtual(other))
}

operator fun Virtual<Int>.minus(other: Int): Virtual<Int> {
    return minus(source.createVirtual(other))
}

operator fun Virtual<Int>.times(other: Int): Virtual<Int> {
    return times(source.createVirtual(other))
}

operator fun Virtual<Int>.div(other: Int): Virtual<Int> {
    return div(source.createVirtual(other))
}

