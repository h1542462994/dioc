package org.tty.dioc.reflect.virtual.operator

import org.tty.dioc.reflect.virtual.Virtual

// @file::TODO("添加注释")

//region compareTo

/**
 * less than.
 * equal to a < b, and return a [Virtual]
 */
infix fun <T: Comparable<T>> Virtual<T>.lt(other: Virtual<T>): Virtual<Boolean> {
    val realResult = real.compareTo(other.real)
    val result = source.createVirtual(realResult < 0)
    eventSource.onVirtualCompare(CompareKind.Lt, this, other, result)
    return result
}

infix fun <T: Comparable<T>> Virtual<T>.gt(other: Virtual<T>): Virtual<Boolean> {
    val realResult = real.compareTo(other.real)
    val result = source.createVirtual(realResult > 0)
    eventSource.onVirtualCompare(CompareKind.Gt, this, other, result)
    return result
}

infix fun <T: Comparable<T>> Virtual<T>.le(other: Virtual<T>): Virtual<Boolean> {
    val realResult = real.compareTo(other.real)
    val result = source.createVirtual(realResult <= 0)
    eventSource.onVirtualCompare(CompareKind.Le, this, other, result)
    return result
}

infix fun <T: Comparable<T>> Virtual<T>.ge(other: Virtual<T>): Virtual<Boolean> {
    val realResult = real.compareTo(other.real)
    val result = source.createVirtual(realResult > 0)
    eventSource.onVirtualCompare(CompareKind.Ge, this, other, result)
    return result
}

infix fun <T: Comparable<T>> Virtual<T>.eq(other: Virtual<T>): Virtual<Boolean> {
    val realResult = real.compareTo(other.real)
    val result = source.createVirtual(realResult == 0)
    eventSource.onVirtualCompare(CompareKind.Eq, this, other, result)
    return result
}

infix fun <T: Comparable<T>> Virtual<T>.neq(other: Virtual<T>): Virtual<Boolean> {
    val realResult = real.compareTo(other.real)
    val result = source.createVirtual(realResult != 0)
    eventSource.onVirtualCompare(CompareKind.Neq, this, other, result)
    return result
}

infix fun <T: Comparable<T>> Virtual<T>.lt(other: T): Virtual<Boolean> {
    return lt(source.createVirtual(other))
}

infix fun <T: Comparable<T>> Virtual<T>.gt(other: T): Virtual<Boolean> {
    return gt(source.createVirtual(other))
}

infix fun <T: Comparable<T>> Virtual<T>.le(other: T): Virtual<Boolean> {
    return le(source.createVirtual(other))
}

infix fun <T: Comparable<T>> Virtual<T>.ge(other: T): Virtual<Boolean> {
    return ge(source.createVirtual(other))
}

infix fun <T: Comparable<T>> Virtual<T>.eq(other: T): Virtual<Boolean> {
    return eq(source.createVirtual(other))
}

infix fun <T: Comparable<T>> Virtual<T>.neq(other: T): Virtual<Boolean> {
    return neq(source.createVirtual(other))
}

//endregion

//region and or xor not

infix fun Virtual<Boolean>.and(other: Virtual<Boolean>): Virtual<Boolean> {
    val realResult = real && other.real
    val result = source.createVirtual(realResult)
    eventSource.onVirtualBool(BoolKind.And, this, other, result)
    return result
}

infix fun Virtual<Boolean>.or(other: Virtual<Boolean>): Virtual<Boolean> {
    val realResult = real.or(other.real)
    val result = source.createVirtual(realResult)
    eventSource.onVirtualBool(BoolKind.Or, this, other, result)
    return result
}

infix fun Virtual<Boolean>.xor(other: Virtual<Boolean>): Virtual<Boolean> {
    val realResult = real xor other.real
    val result = source.createVirtual(realResult)
    eventSource.onVirtualBool(BoolKind.Xor, this, other, result)
    return result
}

operator fun Virtual<Boolean>.not(): Virtual<Boolean> {
    val resultResult = !real
    val result = source.createVirtual(resultResult)
    eventSource.onVirtualBool(BoolKind.Not, this, null, result)
    return result
}

//endregion

//region external

infix fun <T: Comparable<T>> Virtual<T>.within(range: ClosedRange<T>): Virtual<Boolean> {
    val virtualStart = source.createVirtual(range.start)
    val virtualEndInclusive = source.createVirtual(range.endInclusive)

    val leftJudge = source.createVirtual(real >= range.start)
    eventSource.onVirtualCompare(CompareKind.Ge, this, virtualStart, leftJudge)
    val rightJudge = source.createVirtual(real <= range.endInclusive)
    eventSource.onVirtualCompare(CompareKind.Le, this, virtualEndInclusive, rightJudge)
    val realResult = leftJudge.real && rightJudge.real
    val result = leftJudge.source.createVirtual(realResult)
    leftJudge.eventSource.onVirtualBool(BoolKind.And, leftJudge, rightJudge, result)
    return result
}

//endregion