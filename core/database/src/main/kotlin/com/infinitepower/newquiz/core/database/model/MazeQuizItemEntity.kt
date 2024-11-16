package com.infinitepower.newquiz.core.database.model

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItemEntity
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType

@Keep
@Entity(tableName = "mazeItems")
data class MazeQuizItemEntity(
    // Shared columns
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val difficulty: QuestionDifficulty,
    val played: Boolean,
    val type: Type,
    val mazeSeed: Int,

    // Multi choice quiz
    @Embedded("maze_question_")
    val multiChoiceQuestion: MultiChoiceQuestionEntity? = null,

    // Wordle
    @Embedded("maze_wordle_")
    val wordleItem: WordleEntity? = null,

    // Comparison quiz
    @Embedded("comparison_quiz_")
    val comparisonQuizQuestion: ComparisonQuizEntity? = null
) {
    @Keep
    data class WordleEntity(
        val wordleWord: String,
        val wordleQuizType: WordleQuizType,
        val textHelper: String? = null
    )

    @Keep
    data class ComparisonQuizEntity(
        val category: String,
        val comparisonMode: ComparisonMode,
        @Embedded("first_question_")
        val firstQuestion: ComparisonQuizItemEntity,
        @Embedded("second_question_")
        val secondQuestion: ComparisonQuizItemEntity
    )

    // TODO: Change this to GameMode enum
    enum class Type { WORDLE, MULTI_CHOICE, COMPARISON_QUIZ }
}
