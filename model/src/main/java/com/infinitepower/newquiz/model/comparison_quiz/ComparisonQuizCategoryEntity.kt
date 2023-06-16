package com.infinitepower.newquiz.model.comparison_quiz

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.toUiText
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ComparisonQuizCategoryEntity(
    val id: String,
    val name: String,
    val image: String,
    val requireInternetConnection: Boolean = true,
    val description: String,
    val questionDescription: ComparisonQuizCategory.QuestionDescription,
    val formatType: ComparisonQuizFormatType,
    val helperValueSuffix: String? = null,
    val dataSourceAttribution: ComparisonQuizCategory.DataSourceAttribution? = null
) : java.io.Serializable {
    fun toModel(): ComparisonQuizCategory = ComparisonQuizCategory(
        id = id,
        name = name.toUiText(),
        image = image,
        requireInternetConnection = requireInternetConnection,
        description = description,
        questionDescription = questionDescription,
        formatType = formatType,
        helperValueSuffix = helperValueSuffix,
        dataSourceAttribution = dataSourceAttribution
    )
}
