package com.infinitepower.newquiz.compose.ui.quiz

import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.infinitepower.newquiz.compose.core.common.UiEvent
import com.infinitepower.newquiz.compose.core.countdown.CountDownFlow
import com.infinitepower.newquiz.compose.core.util.quiz.QuizXPUtil
import com.infinitepower.newquiz.compose.data.local.question.Question
import com.infinitepower.newquiz.compose.data.local.question.QuestionStep
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import com.infinitepower.newquiz.compose.domain.use_case.saved_questions.SaveQuestionUseCase
import com.infinitepower.newquiz.compose.ui.destinations.QuizResultsScreenDestination
import com.infinitepower.newquiz.compose.worker.quiz.GetQuizDataWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

const val QUIZ_COUNTDOWN_IN_MILLIS = 30000L

fun Int.toMinuteSecond(): String {
    val minutes = (this / 1000) / 60
    val seconds = (this / 1000) % 60
    return if (minutes == 0) seconds.toString() else "$minutes:$seconds"
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val xpUtil: QuizXPUtil,
    private val workManager: WorkManager,
    private val authUserApi: AuthUserApi,
    private val saveQuestionUseCase: SaveQuestionUseCase
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: QuizScreenEvent) = viewModelScope.launch(Dispatchers.IO) {
        when (event) {
            is QuizScreenEvent.OnVerifyQuestionClick -> verifyQuestion()
            is QuizScreenEvent.OnOptionClick -> updateSelectedPosition(event.position)
            is QuizScreenEvent.OnSaveButtonClick -> saveQuestion()
            is QuizScreenEvent.UpdateDataAndStartQuiz -> {
                quizOption.emit(event.quizOption)
                initialQuestions.emit(event.defaultQuestionsString)
                loadQuestions()
            }
        }
    }

    private suspend fun sendUiEvent(event: UiEvent) {
        _uiEvent.send(event)
    }

    private val quizOption = MutableStateFlow(QuizOption.QUICK_QUIZ)
    private val initialQuestions = MutableStateFlow("")

    private suspend fun loadQuestions() {
        val inputData = workDataOf(
            GetQuizDataWorker.TEST_QUESTIONS_ENABLED_PARAM to true,
            GetQuizDataWorker.INITIAL_QUESTIONS_PARAM to initialQuestions.first()
        )

        val getQuizDataWork = OneTimeWorkRequestBuilder<GetQuizDataWorker>()
            .setInputData(inputData)
            .build()

        workManager.enqueue(getQuizDataWork)
        workManager
            .getWorkInfoByIdLiveData(getQuizDataWork.id)
            .asFlow()
            .collect { info ->
                if (info != null && info.state.isFinished) {
                    val questionsResult = info.outputData.getString(GetQuizDataWorker.OUT_QUESTIONS_PARAM)
                    val questionStepsResult = info.outputData.getString(GetQuizDataWorker.OUT_QUESTIONS_STEP_PARAM)

                    val questionsFormatted = Json.decodeFromString<List<Question>>(questionsResult.orEmpty())
                    val questionStepsFormatted = Json.decodeFromString<List<QuestionStep.NotCurrent>>(questionStepsResult.orEmpty())

                    _questions.emit(questionsFormatted)
                    _questionSteps.emit(questionStepsFormatted)
                    start()
                }
            }
    }

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions = _questions.asStateFlow()

    private val _questionSteps = MutableStateFlow<List<QuestionStep>>(emptyList())
    val questionSteps = _questionSteps.asStateFlow()

    private val countDownFlow = CountDownFlow(QUIZ_COUNTDOWN_IN_MILLIS, 250)

    val remainingTime = countDownFlow.countDownFlow

    private val onCountDownFinish = countDownFlow.onFinish

    val currentQuestion = MutableStateFlow<Question?>(null)

    val questionPosition = MutableStateFlow(0)

    val selectedPosition = MutableStateFlow(-1)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            onCountDownFinish.collect { finish ->
                if (finish) verifyQuestion()
            }
        }
    }

    private suspend fun updateSelectedPosition(position: Int) {
        selectedPosition.emit(position)
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

    private suspend fun verifyQuestion() {
        var position = questionPosition.first()
        val question = questions.first()[position]

        updateQuizStep(position = position, pCompleted = true, pCurrent = false, question = question)

        clean()
        delay(1000)

        if (isEndOfQuestions(position + 1)) {
            finishGame()
        } else {
            position++
            nextQuestion(position)
        }
    }

    private suspend fun isEndOfQuestions(
        position: Int
    ) = questions.first().getOrNull(position) == null

    private suspend fun updateQuizStep(
        position: Int,
        pCompleted: Boolean,
        pCurrent: Boolean,
        question: Question
    ) {
        questionSteps.first().toMutableList().apply {
            val step = when {
                pCompleted -> QuestionStep.Completed(
                    question = question,
                    correct = question.correctAns == selectedPosition.first()
                )
                pCurrent -> QuestionStep.Current(question = question)
                else -> QuestionStep.NotCurrent(question = question)
            }
            set(position, step)
        }.also {
            _questionSteps.emit(it)
        }
    }

    private suspend fun clean() {
        countDownFlow.reset()
        currentQuestion.emit(null)
        selectedPosition.emit(-1)
    }

    private suspend fun finishGame() {
        val steps = questionSteps.first().filterIsInstance<QuestionStep.Completed>()
        val resultXp = xpUtil.getNewUserXPByQuizSteps(steps)
        val stepsString = Json.encodeToString(steps)

        /*
        if (authUserApi.isSignedIn) {
            val inputData = workDataOf(
                UpdateUserQuizXPWorker.NEW_XP_PARAM to resultXp,
                UpdateUserQuizXPWorker.QUESTION_STEPS_STRING to stepsString
            )

            val updateUserQuizXPWork = OneTimeWorkRequestBuilder<UpdateUserQuizXPWorker>()
                .setInputData(inputData)
                .build()

            workManager.enqueue(updateUserQuizXPWork)
        }

         */

        sendUiEvent(UiEvent.Navigate(QuizResultsScreenDestination(stepsString, resultXp)))
    }

    private suspend fun saveQuestion() {
        currentQuestion.first()?.let { question ->
            saveQuestionUseCase(question)
        }
    }
}