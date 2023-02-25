package com.infinitepower.newquiz.core.util.kotlin

fun <T> Set<T>.removeFirst(): Set<T> {
    return this - first()
}

fun <T> Set<T>.removeLast(): Set<T> {
    return this - last()
}
