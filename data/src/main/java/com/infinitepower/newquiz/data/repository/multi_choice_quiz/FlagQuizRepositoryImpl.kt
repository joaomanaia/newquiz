package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import android.content.Context
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.util.android.resources.readRawJson
import com.infinitepower.newquiz.data.R
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.FlagQuizRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionType
import com.infinitepower.newquiz.model.multi_choice_quiz.QuestionLanguage
import com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz.CountryFlagQuizBaseItem
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FlagQuizRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteConfig: RemoteConfig
) : FlagQuizRepository {
    override suspend fun getRandomQuestions(
        amount: Int,
        category: MultiChoiceBaseCategory.Flag,
        difficulty: String?,
        random: Random
    ): List<MultiChoiceQuestion> {
        val allCountries = context
            .resources
            .readRawJson<List<CountryFlagQuizBaseItem>>(R.raw.all_countries_with_code)

        val allCountriesName = allCountries.map(CountryFlagQuizBaseItem::name)

        return allCountries
            .sortedBy { it.name }
            .shuffled(random)
            .take(amount)
            .map { country ->
                val flagUrl = remoteConfig.getFlagUrl(country.code)

                country.toQuestion(allCountriesName, flagUrl, random)
            }
    }

    private fun CountryFlagQuizBaseItem.toQuestion(
        allCountriesName: List<String>,
        flagUrl: String,
        random: Random = Random
    ): MultiChoiceQuestion {
        val answerCountries = allCountriesName.shuffled(random).take(3) + name
        val answers = answerCountries.shuffled(random)

        return MultiChoiceQuestion(
            description = "What is the country of this flag?",
            imageUrl = flagUrl,
            answers = answers,
            correctAns = answers.indexOf(name),
            category = MultiChoiceBaseCategory.Flag,
            difficulty = QuestionDifficulty.Medium,
            lang = QuestionLanguage.EN,
            type = MultiChoiceQuestionType.MULTIPLE
        )
    }
}