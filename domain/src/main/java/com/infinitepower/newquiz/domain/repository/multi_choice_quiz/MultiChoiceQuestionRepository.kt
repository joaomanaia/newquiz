package com.infinitepower.newquiz.domain.repository.multi_choice_quiz

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion

interface MultiChoiceQuestionRepository {
    suspend fun getRandomQuestions(amount: Int = 5): List<MultiChoiceQuestion>
}