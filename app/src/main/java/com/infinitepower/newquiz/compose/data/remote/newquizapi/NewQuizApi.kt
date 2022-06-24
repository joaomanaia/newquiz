package com.infinitepower.newquiz.compose.data.remote.newquizapi

import com.infinitepower.newquiz.compose.model.question.Question

interface NewQuizApi {
    suspend fun getQuestions(): List<Question>
}