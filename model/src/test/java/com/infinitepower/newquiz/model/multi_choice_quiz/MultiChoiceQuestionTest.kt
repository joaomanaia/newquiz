package com.infinitepower.newquiz.model.multi_choice_quiz

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlin.test.Test

/**
 * Tests for [MultiChoiceQuestion].
 */
internal class MultiChoiceQuestionTest {
    @Test
    fun `test question generated id is based in the fields`() {
        val question = MultiChoiceQuestion(
            description = "description",
            answers = listOf("a", "b", "c"),
            lang = QuestionLanguage.EN,
            category = MultiChoiceBaseCategory.Random,
            correctAns = 0,
            type = MultiChoiceQuestionType.MULTIPLE,
            difficulty = QuestionDifficulty.Easy
        )

        assertThat(question.id).isEqualTo(376535089)

        val questionWithShuffledAnswers = MultiChoiceQuestion(
            description = "description",
            answers = listOf("c", "b", "a"),
            lang = QuestionLanguage.EN,
            category = MultiChoiceBaseCategory.Random,
            correctAns = 0,
            type = MultiChoiceQuestionType.MULTIPLE,
            difficulty = QuestionDifficulty.Easy
        )

        assertThat(questionWithShuffledAnswers.id).isEqualTo(376535089)
    }
}