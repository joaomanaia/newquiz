package com.infinitepower.newquiz.core.testing.data.fake

import com.infinitepower.newquiz.model.NumberFormatType
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizQuestion
import java.net.URI
import kotlin.random.Random

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

    fun generateQuestion(
        categoryId: String = "1",
        comparisonMode: ComparisonMode = ComparisonMode.GREATER
    ): ComparisonQuizQuestion {
        val question1 = generateQuestionItem()
        val question2 = generateQuestionItem()

        return ComparisonQuizQuestion(
            questions = question1 to question2,
            categoryId = categoryId,
            comparisonMode = comparisonMode
        )
    }

    fun generateQuestionItem(): ComparisonQuizItem = ComparisonQuizItem(
        title = "Title",
        value = Random.nextDouble(),
        imgUri = URI.create("")
    )
}
