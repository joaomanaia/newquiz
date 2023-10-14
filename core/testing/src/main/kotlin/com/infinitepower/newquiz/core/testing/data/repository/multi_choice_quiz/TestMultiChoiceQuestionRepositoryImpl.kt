package com.infinitepower.newquiz.core.testing.data.repository.multi_choice_quiz

import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionType
import com.infinitepower.newquiz.model.multi_choice_quiz.QuestionLanguage
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class TestMultiChoiceQuestionRepositoryImpl @Inject constructor() : MultiChoiceQuestionRepository {
    override suspend fun getRandomQuestions(
        amount: Int,
        category: MultiChoiceBaseCategory.Normal,
        difficulty: String?,
        random: Random
    ): List<MultiChoiceQuestion> = List(amount) {
        val questionDifficulty = difficulty?.let { difficultyStr ->
            QuestionDifficulty.from(difficultyStr)
        } ?: QuestionDifficulty.random(random)

        val answers =  List(4) { answerNum ->
            "Answer $answerNum"
        }

        MultiChoiceQuestion(
            description = "Question $it",
            answers = answers,
            lang = QuestionLanguage.EN,
            category = category,
            correctAns = random.nextInt(4),
            type = MultiChoiceQuestionType.MULTIPLE,
            difficulty = questionDifficulty
        )
    }
}