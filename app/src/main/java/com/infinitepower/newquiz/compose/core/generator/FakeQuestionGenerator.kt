package com.infinitepower.newquiz.compose.core.generator

import com.infinitepower.newquiz.compose.data.local.question.Question
import com.infinitepower.newquiz.compose.data.local.quiz.QuestionDifficulty
import com.infinitepower.newquiz.compose.data.local.quiz.QuestionLanguage
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
import kotlin.random.nextInt

@Singleton
class FakeQuestionGenerator @Inject constructor() {
    private val id = AtomicInteger(0)

    fun getRandomQuestions(n: Int) = (1..n).map {
        val questionId = id.getAndIncrement()

        Question(
            id = questionId,
            description = "Question description $questionId",
            category = "a",
            correctAns = Random.nextInt(0..4),
            difficulty = QuestionDifficulty.Medium.keyName,
            lang = QuestionLanguage.EN.name,
            type = "multiple",
            options = listOf(
                "Answer A",
                "Answer B",
                "Answer C",
                "Answer D"
            )
        )
    }
}