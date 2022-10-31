package com.infinitepower.newquiz.core.common.database

object DatabaseCommon {
    object UserCollection {
        const val NAME = "users"

        const val FIELD_DIAMONDS = "data.diamonds"
        const val FIELD_TOTAL_XP = "data.totalXp"
        const val FIELD_LAST_QUIZ_TIMES = "data.multiChoiceQuizData.lastQuizTimes"
        const val FIELD_TOTAL_QUESTIONS_PLAYED = "data.multiChoiceQuizData.totalQuestionsPlayed"
        const val FIELD_CORRECT_ANSWERS = "data.multiChoiceQuizData.totalCorrectAnswers"

        const val FIELD_WORDLE_WORDS_PLAYED = "data.wordleData.wordsPlayed"
        const val FIELD_WORDLE_WORDS_CORRECT = "data.wordleData.wordsCorrect"
    }
}