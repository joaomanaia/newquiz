package com.infinitepower.newquiz.data.util.mappers.maze

import com.infinitepower.newquiz.core.database.model.MazeQuizItemEntity
import com.infinitepower.newquiz.core.database.util.mappers.toEntity
import com.infinitepower.newquiz.core.database.util.mappers.toModel
import com.infinitepower.newquiz.data.util.mappers.comparisonquiz.toEntity
import com.infinitepower.newquiz.data.util.mappers.comparisonquiz.toModel
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizQuestion
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.wordle.WordleWord

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

    is MazeQuiz.MazeItem.ComparisonQuiz -> MazeQuizItemEntity(
        id = id,
        mazeSeed = mazeSeed,
        difficulty = difficulty,
        played = played,
        type = MazeQuizItemEntity.Type.COMPARISON_QUIZ,
        comparisonQuizQuestion = MazeQuizItemEntity.ComparisonQuizEntity(
            category = question.categoryId,
            comparisonMode = question.comparisonMode,
            firstQuestion = question.questions.first.toEntity(),
            secondQuestion = question.questions.second.toEntity()
        )
    )
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
        val questionEntity = this.multiChoiceQuestion
            ?: throw NullPointerException("Question is null")
        MazeQuiz.MazeItem.MultiChoice(questionEntity.toModel(), id, mazeSeed, difficulty, played)
    }

    MazeQuizItemEntity.Type.COMPARISON_QUIZ -> {
        val questionEntity = this.comparisonQuizQuestion
            ?: throw NullPointerException("Question is null")

        val firstQuestion = questionEntity.firstQuestion.toModel()
        val secondQuestion = questionEntity.secondQuestion.toModel()

        MazeQuiz.MazeItem.ComparisonQuiz(
            question = ComparisonQuizQuestion(
                questions = firstQuestion to secondQuestion,
                categoryId = questionEntity.category,
                comparisonMode = questionEntity.comparisonMode
            ),
            id = id,
            mazeSeed = mazeSeed,
            difficulty = difficulty,
            played = played
        )
    }
}