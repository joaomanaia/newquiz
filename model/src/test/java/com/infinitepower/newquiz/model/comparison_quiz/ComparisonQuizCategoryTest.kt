package com.infinitepower.newquiz.model.comparison_quiz

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class ComparisonQuizCategoryTest {
    @Test
    fun `getQuestionDescription returns correct string for GREATER comparison mode`() {
        val questionDescription = ComparisonQuizCategory.QuestionDescription(
            greater = "Which is greater?",
            less = "Which is less?"
        )

        val category = ComparisonQuizCategory(
            id = "1",
            title = "Category Title",
            description = "Category Description",
            imageUrl = "https://example.com/image.png",
            questionDescription = questionDescription,
            formatType = ComparisonQuizFormatType.Number,
            helperValueSuffix = null,
            dataSourceAttribution = null
        )

        val description = category.getQuestionDescription(ComparisonModeByFirst.GREATER)
        assertThat(description).isEqualTo("Which is greater?")
    }

    @Test
    fun `getQuestionDescription returns correct string for LESSER comparison mode`() {
        val questionDescription = ComparisonQuizCategory.QuestionDescription(
            greater = "Which is greater?",
            less = "Which is less?"
        )

        val category = ComparisonQuizCategory(
            id = "1",
            title = "Category Title",
            description = "Category Description",
            imageUrl = "https://example.com/image.png",
            questionDescription = questionDescription,
            formatType = ComparisonQuizFormatType.Number,
            helperValueSuffix = null,
            dataSourceAttribution = null
        )

        val description = category.getQuestionDescription(ComparisonModeByFirst.LESSER)
        assertThat(description).isEqualTo("Which is less?")
    }
}
