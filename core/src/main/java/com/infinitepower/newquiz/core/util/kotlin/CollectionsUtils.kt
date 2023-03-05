package com.infinitepower.newquiz.core.util.kotlin

import kotlin.random.Random

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
    itemCount: Int,
    generator: suspend () -> T,
    maxIterations: Int = Int.MAX_VALUE,
    exclusions: List<T> = emptyList()
): Iterable<T> {
    val items = HashSet<T>()
    var iterations = 0

    while (items.size < itemCount && iterations < maxIterations) {
        val generatedItem = generator() // Generate new item
        if (generatedItem in exclusions) continue // Checks if generated item is not in items
        items.add(generatedItem)
        iterations++
    }

    return items
}

/**
 * Generates a list of [answerCount] number of incorrect answers for a given [correctSolution] integer.
 * @param answerCount The number of incorrect answers to generate.
 * @param correctSolution The correct solution to the question.
 * @param fromRange The minimum range of values from which to generate incorrect answers. Default value is 10.
 * @param toRange The maximum range of values from which to generate incorrect answers. Default value is 10.
 * @param random The random number generator used to generate the incorrect answers.
*/
suspend fun generateIncorrectNumberAnswers(
    answerCount: Int,
    correctSolution: Int,
    fromRange: Int = 10,
    toRange: Int = 10,
    random: Random = Random
): List<Int> = generateRandomUniqueItems(
    itemCount = answerCount,
    exclusions = listOf(correctSolution),
    generator = {
        random.nextInt(correctSolution - fromRange, correctSolution + toRange)
    }
).toList()
