package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.LogoQuizRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionType
import com.infinitepower.newquiz.model.multi_choice_quiz.QuestionLanguage
import com.infinitepower.newquiz.model.multi_choice_quiz.logo_quiz.LogoQuizBaseItem
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class LogoQuizRepositoryImpl @Inject constructor(
    private val remoteConfig: RemoteConfig
) : LogoQuizRepository {
    override suspend fun getRandomQuestions(
        amount: Int,
        category: MultiChoiceBaseCategory.Logo,
        difficulty: String?,
        random: Random
    ): List<MultiChoiceQuestion> {
        val allLogos = getRemoteConfigAllLogos()

        val filteredByDifficulty = if (difficulty != null) {
            allLogos.filter { item -> item.difficulty == difficulty }
        } else allLogos

        return filteredByDifficulty
            .sortedBy { it.name }
            .shuffled(random)
            .take(amount)
            .map { item -> item.toQuestion() }
    }

    private fun getRemoteConfigAllLogos(): List<LogoQuizBaseItem> {
        val allLogosQuizStr = remoteConfig.getString("all_logos_quiz")
        return Json.decodeFromString(allLogosQuizStr)
    }

    private fun LogoQuizBaseItem.toQuestion(
        random: Random = Random
    ): MultiChoiceQuestion {

        val answerCountries = incorrectAnswers.shuffled(random) + name
        val answers = answerCountries.shuffled(random)

        return MultiChoiceQuestion(
            description = description,
            imageUrl = imgUrl,
            answers = answers,
            correctAns = answers.indexOf(name),
            category = MultiChoiceBaseCategory.Logo,
            difficulty = QuestionDifficulty.Medium,
            lang = QuestionLanguage.EN,
            type = MultiChoiceQuestionType.MULTIPLE
        )
    }
}