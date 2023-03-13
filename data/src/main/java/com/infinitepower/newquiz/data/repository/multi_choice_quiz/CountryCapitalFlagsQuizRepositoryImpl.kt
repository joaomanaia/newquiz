package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.infinitepower.newquiz.core.util.base_urls.getFlagBaseUrl
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.CountryCapitalFlagsQuizRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionType
import com.infinitepower.newquiz.model.multi_choice_quiz.QuestionLanguage
import com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz.Continent
import com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz.CountryQuizBaseItem
import com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz.CountryQuizItem
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

private fun CountryQuizBaseItem.toCountryQuizItem(
    remoteConfig: FirebaseRemoteConfig
) = CountryQuizItem(
    countryCode = countryCode,
    countryName = countryName,
    capital = capital,
    continent = Continent.fromName(continent),
    difficulty = QuestionDifficulty.from(difficulty),
    flagUrl = remoteConfig.getFlagBaseUrl(countryCode)
)

@Singleton
class CountryCapitalFlagsQuizRepositoryImpl @Inject constructor() : CountryCapitalFlagsQuizRepository {
    private val remoteConfig = Firebase.remoteConfig

    override suspend fun getRandomQuestions(
        amount: Int,
        category: MultiChoiceBaseCategory.CountryCapitalFlags,
        difficulty: String?,
        random: Random
    ): List<MultiChoiceQuestion> {
        val allBaseCountries = getRemoteConfigAllCountries()
        val allCountries = allBaseCountries.map { it.toCountryQuizItem(remoteConfig) }

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