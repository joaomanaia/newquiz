package com.infinitepower.newquiz.compose.data.remote.opentdb

import com.infinitepower.newquiz.compose.model.quiz.Question

interface NewQuizApi {
    suspend fun getQuestions(): List<Question>
}