package com.infinitepower.newquiz.compose.ui.quiz.results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.compose.core.util.coroutines.getStateFlow
import com.infinitepower.newquiz.compose.core.util.quiz.QuizXPUtil
import com.infinitepower.newquiz.compose.data.local.question.QuestionStep
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class QuizResultsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val quizXPUtil: QuizXPUtil,
    private val authUserApi: AuthUserApi
) : ViewModel() {
    private val questionStepsString = savedStateHandle
        .getStateFlow<String>(
            key = "quizStepsString",
            scope = viewModelScope
        ).filterNotNull()

    val questionSteps = questionStepsString.map { stepsString ->
        Json.decodeFromString<List<QuestionStep.Completed>>(stepsString)
    }

    val correctQuestionsRatio = questionSteps.map { steps ->
        val totalQuestions = steps.size
        val correctQuestionNum = steps.count { step -> step.correct }
        correctQuestionNum.toFloat() / totalQuestions.toFloat()
    }

    val correctQuestionRatioString = questionSteps.map { steps ->
        val totalQuestions = steps.size
        val correctQuestionNum = steps.count { step -> step.correct }
        "$correctQuestionNum/$totalQuestions"
    }

    val quizNewXP = savedStateHandle
        .getStateFlow<Long>(
            key = "newXP",
            scope = viewModelScope
        ).filterNotNull()

    val bonusAllQuestionsCorrect = correctQuestionsRatio.map { ratio ->
        if (ratio == 1f) (quizXPUtil.getBonusAllQuestionsCorrectXp()) else 0
    }

    val userIsSignedIn = flow {
        emit(authUserApi.isSignedIn)
    }
}