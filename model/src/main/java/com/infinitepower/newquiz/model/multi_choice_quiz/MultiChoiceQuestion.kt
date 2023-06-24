package com.infinitepower.newquiz.model.multi_choice_quiz

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.SecureRandom

@Keep
@Serializable
data class MultiChoiceQuestion(
    val id: Int,
    val description: String,
    val imageUrl: String? = null,
    val answers: List<String>,
    val lang: QuestionLanguage,
    val category: MultiChoiceBaseCategory,
    val correctAns: Int,
    val type: MultiChoiceQuestionType,
    val difficulty: QuestionDifficulty
) : java.io.Serializable {
    fun toQuestionStep() = MultiChoiceQuestionStep.NotCurrent(this)

    override fun toString(): String = Json.encodeToString(this)

    override fun equals(other: Any?): Boolean {
        if (other !is MultiChoiceQuestion) return false

        val idEquals = id == other.id
        val descriptionEquals = description == other.description
        val imageUrlEquals = imageUrl == other.imageUrl
        val answersEquals = answers == other.answers
        val langEquals = lang == other.lang
        val categoryEquals = category == other.category
        val correctAnsEquals = correctAns == other.correctAns
        val typeEquals = type == other.type
        val difficultyEquals = difficulty == other.difficulty

        return idEquals
                && descriptionEquals
                && imageUrlEquals
                && answersEquals
                && langEquals
                && categoryEquals
                && correctAnsEquals
                && typeEquals
                && difficultyEquals
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + description.hashCode()
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + answers.hashCode()
        result = 31 * result + lang.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + correctAns
        result = 31 * result + type.hashCode()
        result = 31 * result + difficulty.hashCode()
        return result
    }
}

fun getBasicMultiChoiceQuestion(
    correctAns: Int = (0..3).random()
): MultiChoiceQuestion {
    return MultiChoiceQuestion(
        id = SecureRandom().nextInt(),
        description = "Question description",
        imageUrl = null,
        answers = listOf(
            "Answer 1",
            "Answer 2",
            "Answer 3",
            "Answer 4"
        ),
        lang = QuestionLanguage.EN,
        category = MultiChoiceBaseCategory.Random,
        correctAns = correctAns,
        type = MultiChoiceQuestionType.MULTIPLE,
        difficulty = QuestionDifficulty.Medium
    )
}
