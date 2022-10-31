package com.infinitepower.newquiz.core.util.kotlin

/**
 * Returns the sum of all elements in the collection.
 */
@JvmName("sumOfIntRange")
fun Iterable<IntRange>.sum(): IntRange {
    if (count() == 0) return 0..0
    return reduce { acc, intRange ->
        (acc.first + intRange.first)..(acc.last + intRange.last)
    }
}