package com.infinitepower.newquiz.multi_choice_quiz

import android.os.CountDownTimer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.common.viewmodel.NavEvent
import com.infinitepower.newquiz.core.common.viewmodel.NavEventViewModel
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.SettingsDataStoreManager
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import com.infinitepower.newquiz.domain.use_case.question.GetRandomMultiChoiceQuestionUseCase
import com.infinitepower.newquiz.model.multi_choice_quiz.RemainingTime
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.SelectedAnswer
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizResultsScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

const val QUIZ_COUNTDOWN_IN_MILLIS = 30000L

@HiltViewModel
class QuizScreenViewModel @Inject constructor(
    private val getRandomQuestionUseCase: GetRandomMultiChoiceQuestionUseCase,
    @SettingsDataStoreManager private val dataStoreManager: DataStoreManager,
    private val savedQuestionsRepository: SavedMultiChoiceQuestionsRepository,
    savedStateHandle: SavedStateHandle
) : NavEventViewModel() {
    private val _uiState = MutableStateFlow(MultiChoiceQuizScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val timer = object: CountDownTimer(QUIZ_COUNTDOWN_IN_MILLIS, 250) {
        override fun onTick(millisUntilFinished: Long) {
            _uiState.update { currentState ->
                currentState.copy(remainingTime = RemainingTime.fromValue(millisUntilFinished))
            }
        }

        override fun onFinish() {
            verifyQuestion()
        }
    }

    override fun onCleared() {
        timer.cancel()
        super.onCleared()
    }

    fun onEvent(event: MultiChoiceQuizScreenUiEvent) {
        when (event) {
            is MultiChoiceQuizScreenUiEvent.SelectAnswer -> selectAnswer(event.answer)
            is MultiChoiceQuizScreenUiEvent.VerifyAnswer -> verifyQuestion()
            is MultiChoiceQuizScreenUiEvent.SaveQuestion -> saveQuestion()
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val initialQuestions = savedStateHandle
                .get<ArrayList<MultiChoiceQuestion>>(MultiChoiceQuizScreenNavArg::initialQuestions.name)
                .orEmpty()
                .toList()

            if (initialQuestions.isEmpty()) {
                loadByCloudQuestions()
            } else {
                createQuestionSteps(initialQuestions)
            }
        }
    }

    private suspend fun loadByCloudQuestions() {
        val questionSize = dataStoreManager.getPreference(SettingsCommon.QuickQuizQuestionsSize)

        getRandomQuestionUseCase(questionSize).collect { res ->
            if (res is Resource.Success) {
                createQuestionSteps(res.data.orEmpty())
            }
        }
    }

    private fun createQuestionSteps(questions: List<MultiChoiceQuestion>) {
        val questionSteps = questions.map { question -> question.toQuestionStep() }

        _uiState.update { currentState ->
            currentState.copy(questionSteps = questionSteps)
        }

        nextQuestion()
    }

    private fun nextQuestion() {
        _uiState.update { currentState ->
            val nextIndex = currentState.getNextIndex()

            when {
                currentState.isGameEnded -> {
                    endGame(currentState.questionSteps.filterIsInstance<MultiChoiceQuestionStep.Completed>())

                    currentState.copy(currentQuestionIndex = -1)
                }
                nextIndex == -1 -> currentState.copy(currentQuestionIndex = nextIndex)
                else -> {
                    timer.start()

                    val newSteps = currentState
                        .questionSteps
                        .toMutableList()
                        .apply {
                            val step = currentState.questionSteps[nextIndex].asCurrent()
                            set(nextIndex, step)
                        }

                    currentState.copy(
                        questionSteps = newSteps,
                        currentQuestionIndex = nextIndex
                    )
                }
            }
        }
    }

    private fun selectAnswer(answer: SelectedAnswer) {
        _uiState.update { currentState ->
            currentState.copy(selectedAnswer = answer)
        }
    }

    private fun verifyQuestion() {
        timer.cancel()

        _uiState.update { currentState ->
            val steps = currentState
                .questionSteps
                .toMutableList()
                .apply {
                    val currentQuestionIndex = currentState.currentQuestionIndex
                    val currentQuestionStep = currentState.currentQuestionStep

                    if (currentQuestionStep != null) {
                        val questionCorrect = currentState.selectedAnswer isCorrect currentQuestionStep.question

                        val completedQuestionStep = currentQuestionStep.changeToCompleted(questionCorrect, currentState.selectedAnswer)
                        set(currentQuestionIndex, completedQuestionStep)
                    }
                }

            currentState.copy(
                questionSteps = steps,
                selectedAnswer = SelectedAnswer.NONE
            )
        }

        nextQuestion()
    }

    private fun saveQuestion() = viewModelScope.launch(Dispatchers.IO) {
        val currentQuestionStep = uiState.first().currentQuestionStep ?: return@launch
        val currentQuestion = currentQuestionStep.question

        savedQuestionsRepository.insertQuestions(currentQuestion)
    }

    private fun endGame(questionSteps: List<MultiChoiceQuestionStep.Completed>) {
        viewModelScope.launch(Dispatchers.IO) {
            val questionStepsStr = Json.encodeToString(questionSteps)
            delay(1000)
            sendNavEventAsync(NavEvent.Navigate(MultiChoiceQuizResultsScreenDestination(questionStepsStr)))
        }
    }
}