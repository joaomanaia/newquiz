package com.infinitepower.newquiz.model.multi_choice_quiz

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlinx.serialization.Serializable

/**
 * Multi choice question entity, used to save items in database.
 */
@Keep
@Serializable
@Entity(tableName = "saved_multi_choice_questions")
data class MultiChoiceQuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String? = null,
    val answers: List<String>,
    val lang: String,
    val category: String,
    @ColumnInfo(name = "correct_ans") val correctAns: Int,
    val type: String,
    val difficulty: String
) : java.io.Serializable {
    override fun equals(other: Any?): Boolean {
        if (other !is MultiChoiceQuestionEntity) return false

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

fun MultiChoiceQuestionEntity.toQuestion(): MultiChoiceQuestion = MultiChoiceQuestion(
    id = id,
    description = description,
    imageUrl = imageUrl,
    answers = answers,
    lang = QuestionLanguage.EN,
    category = MultiChoiceBaseCategory.fromKey(category),
    correctAns = correctAns,
    type = MultiChoiceQuestionType.MULTIPLE,
    difficulty = QuestionDifficulty.from(difficulty)
)
