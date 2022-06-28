package com.infinitepower.newquiz.domain.repository.question

import com.infinitepower.newquiz.model.question.Question

interface QuestionRepository {
    suspend fun getRandomQuestions(amount: Int = 5): List<Question>
}