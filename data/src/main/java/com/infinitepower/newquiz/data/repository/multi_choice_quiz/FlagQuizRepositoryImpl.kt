package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import android.content.Context
import com.infinitepower.newquiz.core.util.android.resources.readRawJson
import com.infinitepower.newquiz.data.R
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.FlagQuizRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz.CountryFlagQuizBaseItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import java.security.SecureRandom
import kotlin.random.Random

@Singleton
class FlagQuizRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FlagQuizRepository {
    override suspend fun getRandomQuestions(
        amount: Int,
        category: Int?,
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
                country.toQuestion(allCountriesName, random)
            }
    }

    private fun CountryFlagQuizBaseItem.toQuestion(
        allCountriesName: List<String>,
        random: Random = Random
    ): MultiChoiceQuestion {
        val answerCountries = allCountriesName.shuffled(random).take(3) + name
        val answers = answerCountries.shuffled(random)

        return MultiChoiceQuestion(
            id = random.nextInt(),
            description = "What is the country of this flag?",
            imageUrl = flagUrl,
            answers = answers,
            correctAns = answers.indexOf(name),
            category = "Flag Quiz",
            difficulty = "medium",
            lang = "en",
            type = "multiple"
        )
    }
}