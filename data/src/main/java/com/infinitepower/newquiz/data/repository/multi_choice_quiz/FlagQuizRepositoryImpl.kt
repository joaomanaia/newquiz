package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import com.infinitepower.newquiz.domain.repository.CountryRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.FlagQuizRepository
import com.infinitepower.newquiz.model.country.Country
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionType
import com.infinitepower.newquiz.model.multi_choice_quiz.QuestionLanguage
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FlagQuizRepositoryImpl @Inject constructor(
    private val countryRepository: CountryRepository
) : FlagQuizRepository {
    override suspend fun getRandomQuestions(
        amount: Int,
        category: MultiChoiceBaseCategory.Flag,
        difficulty: String?,
        random: Random
    ): List<MultiChoiceQuestion> {
        val questionDifficulty = difficulty?.let { QuestionDifficulty.from(it) }

        val allCountries = countryRepository.getAllCountries()
        val allCountriesNames = allCountries.map(Country::countryName)

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
                    allCountriesNames = allCountriesNames,
                    random = random
                )
            }
    }

    private fun Country.toQuestion(
        allCountriesNames: List<String>,
        random: Random = Random
    ): MultiChoiceQuestion {
        val answerCountriesNames = allCountriesNames.shuffled(random).take(3) + countryName
        val answers = answerCountriesNames.shuffled(random)

        return MultiChoiceQuestion(
            description = "What is the country of this flag?",
            image = flagImage,
            answers = answers,
            correctAns = answers.indexOf(countryName),
            category = MultiChoiceBaseCategory.Flag,
            difficulty = difficulty,
            lang = QuestionLanguage.EN,
            type = MultiChoiceQuestionType.MULTIPLE
        )
    }
}
