package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import com.infinitepower.newquiz.core.util.kotlin.generateIncorrectNumberAnswers
import com.infinitepower.newquiz.core.util.kotlin.generateRandomUniqueItems
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.GuessMathSolutionRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class GuessMathSolutionRepositoryImpl @Inject constructor(
    private val mathQuizCoreRepository: MathQuizCoreRepository
) : GuessMathSolutionRepository {
    override suspend fun getRandomQuestions(
        amount: Int,
        category: Int?,
        difficulty: String?,
        random: Random
    ): List<MultiChoiceQuestion> {
        val questionDifficulty = if (difficulty == null) {
            QuestionDifficulty.Easy
        } else {
            QuestionDifficulty.from(difficulty)
        }

        return generateRandomUniqueItems(
            itemCount = amount,
            generator = {
                mathQuizCoreRepository.generateMathFormula(
                    difficulty = questionDifficulty,
                    operatorSize = 2,
                    random = random
                )
            }
        ).map { formula ->
            val incorrectAnswers = generateIncorrectNumberAnswers(3, formula.solution)
            val answers = incorrectAnswers + formula.solution
            val answersShuffled = answers.shuffled(random)

            MultiChoiceQuestion(
                id = random.nextInt(),
                description = "What is the solution of ${formula.leftFormula} ?",
                answers = answersShuffled.map(Int::toString),
                category = "Guess Math Solution",
                correctAns = answersShuffled.indexOf(formula.solution),
                difficulty = questionDifficulty.id,
                lang = "en",
                type = "multiple"
            )
        }
    }
}