package com.infinitepower.newquiz.core.util.collections

inline fun <T> Iterable<T>.indexOfFirstOrNull(predicate: (T) -> Boolean) = indexOfFirst(predicate).takeIf { it >= 0 }