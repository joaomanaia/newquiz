package com.infinitepower.newquiz.compose.ui.quiz

import androidx.lifecycle.*
import com.infinitepower.newquiz.compose.core.countdown.CountDownFlow
import com.infinitepower.newquiz.compose.core.util.coroutines.getStateFlow
import com.infinitepower.newquiz.compose.domain.use_case.question.GetQuestions
import com.infinitepower.newquiz.compose.model.quiz.Question
import com.infinitepower.newquiz.compose.model.quiz.QuizStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

const val QUIZ_COUNTDOWN_IN_MILLIS = 30000L

fun Int.toMinuteSecond(): String {
    val minutes = (this / 1000) / 60
    val seconds = (this / 1000) % 60
    return if (minutes == 0) seconds.toString() else "$minutes:$seconds"
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuestions: GetQuestions,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val quizOption = savedStateHandle.getStateFlow<Int>("quizOptions", viewModelScope)

    val questions = quizOption.map {
        getQuestions(true)
    }

    val quizSteps = MutableStateFlow<List<QuizStep>>(emptyList())

    private val countDownFlow = CountDownFlow(QUIZ_COUNTDOWN_IN_MILLIS, 250)

    val remainingTime = countDownFlow.countDownFlow

    private val onCountDownFinish = countDownFlow.onFinish

    val currentQuestion = MutableStateFlow<Question?>(null)

    val questionPosition = MutableStateFlow(0)

    val selectedPosition = MutableStateFlow(-1)

    fun updateSelectedPosition(
        position: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        selectedPosition.emit(position)
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            questions.collect { qList ->
                qList.map { question ->
                    QuizStep(question = question)
                }.also {
                    quizSteps.emit(it)
                    start()
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            onCountDownFinish.collect { finish ->
                if (finish) verifyQuestion()
            }
        }
    }

    private suspend fun start() {
        nextQuestion(0)
    }

    private suspend fun nextQuestion(position: Int) {
        val question = questions.first().getOrNull(position) ?: return
        currentQuestion.emit(question)
        questionPosition.emit(position)
        updateQuizStep(position = position, pCompleted = false, pCurrent = true, question = question)
        countDownFlow.start()
    }

    fun verifyQuestion() = viewModelScope.launch(Dispatchers.IO) {
        var position = questionPosition.first()
        val question = questions.first()[position]

        updateQuizStep(position = position, pCompleted = true, pCurrent = false, question = question)

        clean()
        delay(1000)

        position++
        nextQuestion(position)
    }

    private suspend fun updateQuizStep(
        position: Int,
        pCompleted: Boolean,
        pCurrent: Boolean,
        question: Question
    ) {
        quizSteps.first().toMutableList().apply {
            val newQuizStep = get(position).copy(
                completed = pCompleted,
                current = pCurrent,
                correct = question.correctAns == selectedPosition.first()
            )
            set(position, newQuizStep)
        }.also {
            quizSteps.emit(it)
        }
    }

    private suspend fun clean() {
        countDownFlow.reset()
        currentQuestion.emit(null)
        selectedPosition.emit(-1)
    }
}