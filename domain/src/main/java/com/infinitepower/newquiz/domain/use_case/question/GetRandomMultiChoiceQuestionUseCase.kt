package com.infinitepower.newquiz.domain.use_case.question

import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.CountryCapitalFlagsQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.FlagQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.GuessMathSolutionRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.LogoQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.domain.repository.numbers.NumberTriviaQuestionRepository
import com.infinitepower.newquiz.model.FlowResource
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRandomMultiChoiceQuestionUseCase @Inject constructor(
    private val normalQuestionRepository: MultiChoiceQuestionRepository,
    private val flagQuizRepository: FlagQuizRepository,
    private val logoQuizRepository: LogoQuizRepository,
    private val guessMathSolutionRepository: GuessMathSolutionRepository,
    private val numberTriviaQuestionRepository: NumberTriviaQuestionRepository,
    private val countryCapitalFlagsQuizRepository: CountryCapitalFlagsQuizRepository
) {
    operator fun invoke(
        amount: Int = 5,
        category: MultiChoiceBaseCategory? = MultiChoiceBaseCategory.Normal(),
        difficulty: String? = null
    ): FlowResource<List<MultiChoiceQuestion>> = flow {
        try {
            emit(Resource.Loading())

            val questions = when (category) {
                is MultiChoiceBaseCategory.Normal -> normalQuestionRepository.getRandomQuestions(
                    amount,
                    category,
                    difficulty
                )

                is MultiChoiceBaseCategory.Flag -> flagQuizRepository.getRandomQuestions(
                    amount,
                    category,
                    difficulty
                )

                is MultiChoiceBaseCategory.Logo -> logoQuizRepository.getRandomQuestions(
                    amount,
                    category,
                    difficulty
                )

                is MultiChoiceBaseCategory.GuessMathSolution -> guessMathSolutionRepository.getRandomQuestions(
                    amount,
                    category,
                    difficulty
                )

                is MultiChoiceBaseCategory.CountryCapitalFlags -> countryCapitalFlagsQuizRepository.getRandomQuestions(
                    amount,
                    category,
                    difficulty
                )

                is MultiChoiceBaseCategory.NumberTrivia -> numberTriviaQuestionRepository.generateMultiChoiceQuestion(
                    size = amount
                )

                null -> throw IllegalArgumentException("Quiz type is null")
            }

            emit(Resource.Success(questions))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "Error while loading questions"))
        }
    }
}
