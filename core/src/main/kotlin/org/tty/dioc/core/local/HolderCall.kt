package org.tty.dioc.core.local

/**
 * data class representing [holder] and [caller]
 */
data class HolderCall<TH: Any, T: Any>(
    val holder: TH,
    val caller: (TH) -> T
)