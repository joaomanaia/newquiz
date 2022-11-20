package com.infinitepower.newquiz.online_services.data.game.xp

import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.online_services.domain.game.xp.MultiChoiceQuizXPRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * Base random xp is obtained between [randomXPRange].
 * This xp can be multiplied by the [QuestionDifficulty].
 */
@Singleton
class MultiChoiceQuizXPRepositoryImpl @Inject constructor() : MultiChoiceQuizXPRepository {
    override fun randomXPRange(): IntRange = 10..20

    override fun generateRandomXP(range: IntRange): Int {
        return Random.nextInt(range)
    }

    override fun getXpMultiplierFactor(difficulty: QuestionDifficulty): Float {
        return when (difficulty) {
            is QuestionDifficulty.Easy -> 1f
            is QuestionDifficulty.Medium -> 1.5f
            is QuestionDifficulty.Hard -> 2f
        }
    }

    override fun generateRandomXP(difficulty: QuestionDifficulty): Int {
        val randomXPRange = randomXPRange()

        val randomXP = generateRandomXP(randomXPRange)

        val xpMultiplierFactor = getXpMultiplierFactor(difficulty)
        val xpMultiplied = randomXP * xpMultiplierFactor

        return xpMultiplied.roundToInt()
    }

    override fun generateQuestionsRandomXP(
        questionSteps: List<MultiChoiceQuestionStep.Completed>
    ): Int {
        return questionSteps
            .filter(MultiChoiceQuestionStep.Completed::correct)
            .sumOf { step ->
                val difficultyStr = step.question.difficulty
                val difficulty = QuestionDifficulty.from(difficultyStr)

                generateRandomXP(difficulty)
            }
    }
}