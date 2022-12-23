package com.infinitepower.newquiz.model.maze

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infinitepower.newquiz.model.math_quiz.MathFormula
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
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

    // Multi choice quiz
    @Embedded("maze_question_")
    val multiChoiceQuestion: MultiChoiceQuestion? = null,

    // Wordle
    @Embedded("maze_wordle_")
    val wordleItem: WordleEntity? = null
) {
    @Keep
    data class WordleEntity(
        val wordleWord: String,
        val wordleQuizType: WordleQuizType
    )

    enum class Type { WORDLE, MULTI_CHOICE }
}

fun MazeQuizItemEntity.toMazeQuizItem(): MazeQuiz.MazeItem = when (type) {
    MazeQuizItemEntity.Type.WORDLE -> {
        val wordleItem = this.wordleItem ?: throw NullPointerException("Wordle word is null")
        MazeQuiz.MazeItem.Wordle(wordleItem.wordleWord, wordleItem.wordleQuizType, id, difficulty, played)
    }
    MazeQuizItemEntity.Type.MULTI_CHOICE -> {
        val question = this.multiChoiceQuestion ?: throw NullPointerException("Question is null")
        MazeQuiz.MazeItem.MultiChoice(question, id, difficulty, played)
    }
}

fun MazeQuiz.MazeItem.toEntity(): MazeQuizItemEntity = when (this) {
    is MazeQuiz.MazeItem.Wordle -> MazeQuizItemEntity(
        id = id,
        difficulty = difficulty,
        played = played,
        type = MazeQuizItemEntity.Type.WORDLE,
        wordleItem = MazeQuizItemEntity.WordleEntity(
            wordleWord = word,
            wordleQuizType = wordleQuizType
        )
    )
    is MazeQuiz.MazeItem.MultiChoice -> MazeQuizItemEntity(
        id = id,
        difficulty = difficulty,
        played = played,
        type = MazeQuizItemEntity.Type.MULTI_CHOICE,
        multiChoiceQuestion = question
    )
}