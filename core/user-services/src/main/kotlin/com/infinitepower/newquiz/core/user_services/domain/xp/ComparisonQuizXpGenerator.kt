package com.infinitepower.newquiz.core.user_services.domain.xp

interface ComparisonQuizXpGenerator : XpGenerator {
    fun getDefaultXpForAnswer(): UInt

    /**
     * Generates the XP for the user based on the [endPosition] they reached in the quiz
     * and the number of [skippedAnswers], this number of [skippedAnswers]
     * will be deducted from the [endPosition].
     *
     * If the [endPosition] is 1, the user has answered incorrectly, so no XP is awarded.
     *
     * @param endPosition The position the user reached in the quiz
     * @param skippedAnswers The number of questions the user skipped
     */
    fun generateXp(
        endPosition: UInt,
        skippedAnswers: UInt,
    ): UInt
}
