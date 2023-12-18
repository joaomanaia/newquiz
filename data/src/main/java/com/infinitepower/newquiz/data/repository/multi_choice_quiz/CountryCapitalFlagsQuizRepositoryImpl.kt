package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.CountryCapitalFlagsQuizRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionType
import com.infinitepower.newquiz.model.multi_choice_quiz.QuestionLanguage
import com.infinitepower.newquiz.domain.repository.CountryRepository
import com.infinitepower.newquiz.model.country.Country
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class CountryCapitalFlagsQuizRepositoryImpl @Inject constructor(
    private val countryRepository: CountryRepository
) : CountryCapitalFlagsQuizRepository {
    override suspend fun getRandomQuestions(
        amount: Int,
        category: MultiChoiceBaseCategory.CountryCapitalFlags,
        difficulty: String?,
        random: Random
    ): List<MultiChoiceQuestion> {
        val questionDifficulty = difficulty?.let { QuestionDifficulty.from(it) }

        val allCountries = countryRepository.getAllCountries()
        val allCountriesCapitalNames = allCountries.map(Country::capital)

        // Filter countries by difficulty
        val countriesFiltered = questionDifficulty?.let {
            allCountries.filter { country -> country.difficulty == it }
        } ?: allCountries // if difficulty is null, then return all countries

        return countriesFiltered
            .sortedBy { it.countryName }
            .shuffled(random)
            .take(amount)
            .map { country ->
                country.toQuestion(
                    allCountriesCapitalNames = allCountriesCapitalNames,
                    random = random
                )
            }
    }

    private fun Country.toQuestion(
        allCountriesCapitalNames: List<String>,
        random: Random = Random
    ): MultiChoiceQuestion {
        val answerCountries = allCountriesCapitalNames.shuffled(random).take(3) + capital
        val answers = answerCountries.shuffled(random)

        return MultiChoiceQuestion(
            description = "What is the capital of $countryName?",
            imageUrl = flagUrl,
            answers = answers,
            correctAns = answers.indexOf(capital),
            category = MultiChoiceBaseCategory.CountryCapitalFlags,
            difficulty = difficulty,
            lang = QuestionLanguage.EN,
            type = MultiChoiceQuestionType.MULTIPLE
        )
    }
}
