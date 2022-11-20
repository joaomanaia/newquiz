package com.infinitepower.newquiz.domain.repository.math_quiz

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.model.math_quiz.MathQuiz
import com.infinitepower.newquiz.model.question.QuestionDifficulty

interface MathQuizItemLocatorRepository {
    suspend fun getRandomQuestions(
        difficulty: QuestionDifficulty
    ): FlowResource<MathQuiz.HiddenItemLocator>
}