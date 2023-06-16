package com.infinitepower.newquiz.model.comparison_quiz

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.model.toUiText
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
            name = "Category Title".toUiText(),
            description = "Category Description",
            image = "https://example.com/image.png",
            questionDescription = questionDescription,
            formatType = ComparisonQuizFormatType.Number,
            helperValueSuffix = null,
            dataSourceAttribution = null
        )

        val description = category.getQuestionDescription(ComparisonMode.GREATER)
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
            name = "Category Title".toUiText(),
            description = "Category Description",
            image = "https://example.com/image.png",
            questionDescription = questionDescription,
            formatType = ComparisonQuizFormatType.Number,
            helperValueSuffix = null,
            dataSourceAttribution = null
        )

        val description = category.getQuestionDescription(ComparisonMode.LESSER)
        assertThat(description).isEqualTo("Which is less?")
    }
}
