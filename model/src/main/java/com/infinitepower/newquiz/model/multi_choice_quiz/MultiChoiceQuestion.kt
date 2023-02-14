package com.infinitepower.newquiz.model.multi_choice_quiz

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.SecureRandom

@Keep
@Serializable
@Entity(tableName = "saved_multi_choice_questions")
data class MultiChoiceQuestion(
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
    fun toQuestionStep() = MultiChoiceQuestionStep.NotCurrent(this)

    override fun toString(): String = Json.encodeToString(this)

    companion object {
        fun fromString(value: String): MultiChoiceQuestion = Json.decodeFromString(value)
    }

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
}

fun getBasicMultiChoiceQuestion() = MultiChoiceQuestion(
    id = SecureRandom().nextInt(),
    description = "New Social is the best social network?",
    imageUrl = null,
    answers = listOf(
        "No",
        "The Best",
        "Yes",
        "The Worst",
    ),
    lang = QuestionLanguage.EN.name,
    category = "",
    correctAns = (0..3).random(),
    type = "multiple",
    difficulty = "easy"
)