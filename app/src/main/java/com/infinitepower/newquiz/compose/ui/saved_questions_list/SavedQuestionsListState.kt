package com.infinitepower.newquiz.compose.ui.saved_questions_list

import androidx.annotation.Keep
import androidx.paging.PagingData
import com.infinitepower.newquiz.compose.data.local.question.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Keep
data class SavedQuestionsListState(
    val isLoading: Boolean = false,
    val savedQuestions: Flow<PagingData<Question>> = flow {
        emit(PagingData.empty())
    },
    val error: String = ""
)