package com.infinitepower.newquiz.model.comparison_quiz

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.GameMode
import com.infinitepower.newquiz.model.NumberFormatType
import com.infinitepower.newquiz.model.UiText
import kotlinx.serialization.Serializable

/**
 * A category of comparison quizzes.
 * @param id The id of the category.
 * @param name The title of the category.
 * @param image The url of the image of the category.
 * @param isMazeDisabled Whether the category is not available for the maze.
 * @param questionDescription The description of the question.
 * @param helperValueSuffix The suffix of the question value.
 * @param generateQuestionsLocally Whether the questions should be generated locally.
 */
@Keep
@Serializable
data class ComparisonQuizCategory(
    override val id: String,
    override val name: UiText,
    override val image: String,
    override val requireInternetConnection: Boolean = true,
    val generateQuestionsLocally: Boolean = false,
    val isMazeDisabled: Boolean = false,
    val description: String,
    val questionDescription: QuestionDescription,
    val formatType: NumberFormatType,
    val helperValueSuffix: String? = null,
    val dataSourceAttribution: DataSourceAttribution? = null
) : BaseCategory, java.io.Serializable, Comparable<ComparisonQuizCategory> {
    override val gameMode: GameMode = GameMode.COMPARISON_QUIZ

    fun getQuestionDescription(
        comparisonMode: ComparisonMode
    ): String = when (comparisonMode) {
        ComparisonMode.GREATER -> questionDescription.greater
        ComparisonMode.LESSER -> questionDescription.less
    }

    override fun compareTo(other: ComparisonQuizCategory): Int {
        return id.compareTo(other.id)
    }

    override fun equals(other: Any?): Boolean {
        // The categories are equal if they have the same id.
        return other is ComparisonQuizCategory && id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

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

    fun toEntity(): ComparisonQuizCategoryEntity = ComparisonQuizCategoryEntity(
        id = id,
        name = name.toString(),
        image = image,
        requireInternetConnection = requireInternetConnection,
        generateQuestionsLocally = generateQuestionsLocally,
        description = description,
        questionDescription = questionDescription,
        formatType = formatType.name.lowercase(),
        helperValueSuffix = helperValueSuffix,
        dataSourceAttribution = dataSourceAttribution
    )
}
