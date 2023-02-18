package com.infinitepower.newquiz.domain.use_case.question

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.core.multi_choice_quiz.MultiChoiceQuizType
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.FlagQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.GuessMathSolutionRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.LogoQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.domain.repository.numbers.NumberTriviaQuestionRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRandomMultiChoiceQuestionUseCase @Inject constructor(
    private val normalQuestionRepository: MultiChoiceQuestionRepository,
    private val flagQuizRepository: FlagQuizRepository,
    private val logoQuizRepository: LogoQuizRepository,
    private val guessMathSolutionRepository: GuessMathSolutionRepository,
    private val numberTriviaQuestionRepository: NumberTriviaQuestionRepository
) {
    operator fun invoke(
        amount: Int = 5,
        category: Int? = null,
        difficulty: String? = null,
        type: MultiChoiceQuizType? = MultiChoiceQuizType.NORMAL
    ): FlowResource<List<MultiChoiceQuestion>> = flow {
        try {
            emit(Resource.Loading())

            val questions = when (type) {
                MultiChoiceQuizType.NORMAL -> normalQuestionRepository.getRandomQuestions(
                    amount,
                    category,
                    difficulty
                )
                MultiChoiceQuizType.FLAG -> flagQuizRepository.getRandomQuestions(
                    amount,
                    category,
                    difficulty
                )
                MultiChoiceQuizType.LOGO -> logoQuizRepository.getRandomQuestions(
                    amount,
                    category,
                    difficulty
                )
                MultiChoiceQuizType.GUESS_MATH_SOLUTION -> guessMathSolutionRepository.getRandomQuestions(
                    amount,
                    category,
                    difficulty
                )
                MultiChoiceQuizType.NUMBER_TRIVIA -> numberTriviaQuestionRepository.generateMultiChoiceQuestion(size = amount)
                null -> throw IllegalArgumentException("Quiz type is null")
            }

            emit(Resource.Success(questions))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "Error while loading questions"))
        }
    }
}