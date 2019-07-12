package com.cxyzy.note.ext

import org.koin.core.KoinComponent
import org.koin.core.scope.Scope

object KoinInject {
    inline fun <reified T : Any> getFromKoin(): T {
        return getKoin().get(null, Scope.GLOBAL, null)
    }
}

/**
 * Get Koin context
 */
fun Any.getKoin() = when (this) {
    is KoinComponent -> this.getKoin()
    else -> org.koin.core.context.GlobalContext.get().koin
}