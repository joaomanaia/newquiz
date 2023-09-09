package com.infinitepower.newquiz.core.database.model

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infinitepower.newquiz.core.database.util.mappers.toEntity
import com.infinitepower.newquiz.core.database.util.mappers.toModel
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleWord

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
    val wordleItem: WordleEntity? = null
) {
    @Keep
    data class WordleEntity(
        val wordleWord: String,
        val wordleQuizType: WordleQuizType,
        val textHelper: String? = null
    )

    enum class Type { WORDLE, MULTI_CHOICE }
}

fun MazeQuizItemEntity.toMazeQuizItem(): MazeQuiz.MazeItem = when (type) {
    MazeQuizItemEntity.Type.WORDLE -> {
        val wordleItem = this.wordleItem ?: throw NullPointerException("Wordle word is null")
        MazeQuiz.MazeItem.Wordle(
            wordleWord = WordleWord(
                word = wordleItem.wordleWord
            ),
            wordleQuizType = wordleItem.wordleQuizType,
            id = id,
            mazeSeed = mazeSeed,
            difficulty = difficulty,
            played = played
        )
    }
    MazeQuizItemEntity.Type.MULTI_CHOICE -> {
        val questionEntity = this.multiChoiceQuestion ?: throw NullPointerException("Question is null")
        MazeQuiz.MazeItem.MultiChoice(questionEntity.toModel(), id, mazeSeed, difficulty, played)
    }
}

fun MazeQuiz.MazeItem.toEntity(): MazeQuizItemEntity = when (this) {
    is MazeQuiz.MazeItem.Wordle -> MazeQuizItemEntity(
        id = id,
        mazeSeed = mazeSeed,
        difficulty = difficulty,
        played = played,
        type = MazeQuizItemEntity.Type.WORDLE,
        wordleItem = MazeQuizItemEntity.WordleEntity(
            wordleWord = wordleWord.word,
            wordleQuizType = wordleQuizType,
            textHelper = wordleWord.textHelper
        )
    )
    is MazeQuiz.MazeItem.MultiChoice -> MazeQuizItemEntity(
        id = id,
        mazeSeed = mazeSeed,
        difficulty = difficulty,
        played = played,
        type = MazeQuizItemEntity.Type.MULTI_CHOICE,
        multiChoiceQuestion = question.toEntity()
    )
}