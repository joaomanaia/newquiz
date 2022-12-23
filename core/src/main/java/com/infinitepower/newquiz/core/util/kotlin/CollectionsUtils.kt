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

infix fun ClosedFloatingPointRange<Float>.increaseEndBy(
    other: Float
): ClosedFloatingPointRange<Float> {
    return endInclusive..(endInclusive + other)
}

suspend fun <T> generateRandomUniqueItems(
    questionSize: Int,
    generator: suspend () -> T,
    maxIterations: Int = Int.MAX_VALUE
): Iterable<T> {
    val items = HashSet<T>()
    var iterations = 0

    while (items.size < questionSize && iterations < maxIterations) {
        val generatedItem = generator()
        items.add(generatedItem)
        iterations++
    }

    return items
}