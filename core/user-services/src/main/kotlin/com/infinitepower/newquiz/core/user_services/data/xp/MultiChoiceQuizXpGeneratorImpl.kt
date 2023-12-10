package com.infinitepower.newquiz.core.user_services.data.xp

import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.user_services.domain.xp.MultiChoiceQuizXpGenerator
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MultiChoiceQuizXpGeneratorImpl @Inject constructor(
    private val remoteConfig: RemoteConfig
) : MultiChoiceQuizXpGenerator {
    override fun getDefaultXpReward(): Map<QuestionDifficulty, UInt> {
        val xpRewardStr = remoteConfig.get(RemoteConfigValue.MULTICHOICE_QUIZ_DEFAULT_XP_REWARD)
        val xpReward: Map<String, Int> = Json.decodeFromString(xpRewardStr)

        return xpReward.map { (difficultyStr, reward) ->
            QuestionDifficulty.from(difficultyStr) to reward.toUInt()
        }.toMap()
    }

    override fun generateXp(
        questionSteps: List<MultiChoiceQuestionStep.Completed>
    ): UInt {
        val defaultXpReward = getDefaultXpReward()

        return questionSteps
            .filter { step ->
                step.correct && !step.skipped
            }.sumOf { step ->
                val difficulty = step.question.difficulty

                defaultXpReward[difficulty] ?: 0u
            }
    }
}