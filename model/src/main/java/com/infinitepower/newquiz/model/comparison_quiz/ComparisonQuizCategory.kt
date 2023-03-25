package com.infinitepower.newquiz.model.comparison_quiz

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * A category of comparison quizzes.
 * @param id The id of the category.
 * @param title The title of the category.
 * @param imageUrl The url of the image of the category.
 * @param questionDescription The description of the question.
 * @param helperValueSuffix The suffix of the question value.
 */
@Keep
@Serializable
data class ComparisonQuizCategory(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val questionDescription: QuestionDescription,
    val helperValueSuffix: String? = null,
    val dataSourceAttribution: DataSourceAttribution? = null
) : java.io.Serializable {
    @Keep
    @Serializable
    data class QuestionDescription(
        val greater: String,
        val less: String
    ) : java.io.Serializable

    /**
     * The data source attribution of the category.
     */
    @Keep
    @Serializable
    data class DataSourceAttribution(
        val text: String,
        val logo: String? = null
    ) : java.io.Serializable
}
