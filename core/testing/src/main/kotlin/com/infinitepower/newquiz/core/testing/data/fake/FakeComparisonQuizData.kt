package com.infinitepower.newquiz.core.testing.data.fake

import com.infinitepower.newquiz.model.NumberFormatType
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory

object FakeComparisonQuizData {
    fun generateCategories(count: Int = 10): List<ComparisonQuizCategory> {
        return List(count, ::generateCategory)
    }

    fun generateCategory(id: Int = 1): ComparisonQuizCategory = ComparisonQuizCategory(
        id = id.toString(),
        name = UiText.DynamicString("Category $id"),
        image = "image_url_$id",
        description = "Description $id",
        questionDescription = ComparisonQuizCategory.QuestionDescription(
            greater = "Greater $id",
            less = "Less $id"
        ),
        formatType = NumberFormatType.DEFAULT,
    )
}
