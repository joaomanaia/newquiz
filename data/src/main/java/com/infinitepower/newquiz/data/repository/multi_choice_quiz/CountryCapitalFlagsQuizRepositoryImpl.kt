package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import android.content.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.infinitepower.newquiz.core.util.android.resources.readRawJson
import com.infinitepower.newquiz.data.R
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.CountryCapitalFlagsQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.FlagQuizRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionType
import com.infinitepower.newquiz.model.multi_choice_quiz.QuestionLanguage
import com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz.CountryFlagQuizBaseItem
import com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz.CountryQuizBaseItem
import com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz.CountryQuizItem
import com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz.toCountryQuizItem
import com.infinitepower.newquiz.model.multi_choice_quiz.logo_quiz.LogoQuizBaseItem
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import java.security.SecureRandom
import kotlin.random.Random

@Singleton
class CountryCapitalFlagsQuizRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : CountryCapitalFlagsQuizRepository {
    private val remoteConfig = Firebase.remoteConfig

    override suspend fun getRandomQuestions(
        amount: Int,
        category: MultiChoiceBaseCategory.CountryCapitalFlags,
        difficulty: String?,
        random: Random
    ): List<MultiChoiceQuestion> {
        val allBaseCountries = getRemoteConfigAllCountries()
        val allCountries = allBaseCountries.map(CountryQuizBaseItem::toCountryQuizItem)

        return allCountries
            .sortedBy { it.countryName }
            .shuffled(random)
            .take(amount)
            .map { country -> country.toQuestion(allCountries, random) }
    }

    private fun getRemoteConfigAllCountries(): List<CountryQuizBaseItem> {
        val allLogosQuizStr = remoteConfig.getString("countries_and_capitals")
        return Json.decodeFromString(allLogosQuizStr)
    }

    private fun CountryQuizItem.toQuestion(
        allCountries: List<CountryQuizItem>,
        random: Random = Random
    ): MultiChoiceQuestion {
        val answerCountries = allCountries.shuffled(random).take(3) + this
        val answers = answerCountries.shuffled(random)
        val answersCapitals = answers.map(CountryQuizItem::capital)

        return MultiChoiceQuestion(
            id = random.nextInt(),
            description = "What is the capital of $countryName?",
            imageUrl = flagUrl,
            answers = answersCapitals,
            correctAns = answers.indexOf(this),
            category = MultiChoiceBaseCategory.CountryCapitalFlags,
            difficulty = QuestionDifficulty.Medium,
            lang = QuestionLanguage.EN,
            type = MultiChoiceQuestionType.MULTIPLE
        )
    }
}