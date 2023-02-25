package com.infinitepower.newquiz.data.repository.numbers

import com.infinitepower.newquiz.core.util.kotlin.generateIncorrectNumberAnswers
import com.infinitepower.newquiz.domain.repository.numbers.NumberTriviaQuestionApi
import com.infinitepower.newquiz.domain.repository.numbers.NumberTriviaQuestionRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionType
import com.infinitepower.newquiz.model.multi_choice_quiz.QuestionLanguage
import com.infinitepower.newquiz.model.number.NumberTriviaQuestion
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleWord
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class NumberTriviaQuestionRepositoryImpl @Inject constructor(
    private val numberTriviaQuestionApi: NumberTriviaQuestionApi
) : NumberTriviaQuestionRepository {
    override suspend fun generateRandomQuestions(
        size: Int,
        minNumber: Int,
        maxNumber: Int,
        random: Random
    ): List<NumberTriviaQuestion> {
        val questionEntity = numberTriviaQuestionApi.getRandomQuestion(size, minNumber, maxNumber)
        return questionEntity.toNumberTriviaQuestions()
    }

    override suspend fun generateWordleQuestion(random: Random): WordleWord {
        val randomQuestion = generateRandomQuestions(1, 100, 10000, random).first()

        return WordleWord(
            word = randomQuestion.number.toString(),
            textHelper = randomQuestion.question
        )
    }

    override suspend fun generateMultiChoiceQuestion(size: Int, random: Random): List<MultiChoiceQuestion> {
        val randomQuestion = generateRandomQuestions(size, 100, 10000, random)

        return randomQuestion.map { question ->
            val incorrectAnswers = generateIncorrectNumberAnswers(3, question.number)
            val answers = (incorrectAnswers + question.number).shuffled(random)

            MultiChoiceQuestion(
                id = Random.nextInt(),
                description = question.question,
                answers = answers.map(Int::toString),
                correctAns = answers.indexOf(question.number),
                lang = QuestionLanguage.EN,
                category = MultiChoiceBaseCategory.NumberTrivia,
                type = MultiChoiceQuestionType.MULTIPLE,
                difficulty = QuestionDifficulty.Easy
            )
        }
    }
}