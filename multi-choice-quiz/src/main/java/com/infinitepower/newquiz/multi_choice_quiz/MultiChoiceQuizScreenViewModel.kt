package com.infinitepower.newquiz.multi_choice_quiz

import android.os.CountDownTimer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.common.viewmodel.NavEvent
import com.infinitepower.newquiz.core.common.viewmodel.NavEventViewModel
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.datastore.di.SettingsDataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.translation.TranslatorUtil
import com.infinitepower.newquiz.core.user_services.UserService
import com.infinitepower.newquiz.core.user_services.workers.MultiChoiceQuizEndGameWorker
import com.infinitepower.newquiz.data.worker.UpdateGlobalEventDataWorker
import com.infinitepower.newquiz.data.worker.maze.EndGameMazeQuizWorker
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import com.infinitepower.newquiz.domain.use_case.question.GetRandomMultiChoiceQuestionUseCase
import com.infinitepower.newquiz.domain.use_case.question.IsQuestionSavedUseCase
import com.infinitepower.newquiz.model.RemainingTime
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.model.global_event.GameEvent
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.SelectedAnswer
import com.infinitepower.newquiz.model.multi_choice_quiz.isAllCorrect
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizResultsScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

const val QUIZ_COUNTDOWN_IN_MILLIS = 30000L

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class QuizScreenViewModel @Inject constructor(
    private val getRandomQuestionUseCase: GetRandomMultiChoiceQuestionUseCase,
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager,
    private val savedQuestionsRepository: SavedMultiChoiceQuestionsRepository,
    private val recentCategoriesRepository: RecentCategoriesRepository,
    private val savedStateHandle: SavedStateHandle,
    private val translationUtil: TranslatorUtil,
    private val workManager: WorkManager,
    private val isQuestionSavedUseCase: IsQuestionSavedUseCase,
    private val analyticsHelper: AnalyticsHelper,
    private val userService: UserService
) : NavEventViewModel() {
    private val _uiState = MutableStateFlow(MultiChoiceQuizScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val timer = object : CountDownTimer(QUIZ_COUNTDOWN_IN_MILLIS, 250) {
        override fun onTick(millisUntilFinished: Long) {
            _uiState.update { currentState ->
                currentState.copy(remainingTime = RemainingTime.fromMilliseconds(millisUntilFinished))
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
            is MultiChoiceQuizScreenUiEvent.GetUserSkipQuestionDiamonds -> getUserDiamonds()
            is MultiChoiceQuizScreenUiEvent.CleanUserSkipQuestionDiamonds -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        userDiamonds = -1,
                        userDiamondsLoading = false
                    )
                }
            }

            is MultiChoiceQuizScreenUiEvent.SkipQuestion -> skipQuestion()
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
                createQuestionSteps(initialQuestions, MultiChoiceBaseCategory.Normal())
            }
        }

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(skipsAvailable = userService.userAvailable())
            }
        }

        uiState
            .distinctUntilChangedBy { it.currentQuestionStep?.question }
            .filter { it.currentQuestionStep?.question != null }
            .flatMapLatest { state ->
                val question = state.currentQuestionStep?.question ?: return@flatMapLatest emptyFlow()

                isQuestionSavedUseCase(question)
            }.onEach { res ->
                if (res.isSuccess()) {
                    val questionSaved = res.data == true

                    _uiState.update { currentState ->
                        currentState.copy(questionSaved = questionSaved)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun skipQuestion() = viewModelScope.launch(Dispatchers.IO) {
        val state = uiState.first()

        if (state.userDiamonds < 1) return@launch

        val currentQuestionStep = state.currentQuestionStep
        val correctAnswer = currentQuestionStep?.question?.correctAns ?: return@launch

        selectAnswer(SelectedAnswer.fromIndex(correctAnswer))
        verifyQuestion()

        _uiState.update { currentState ->
            currentState.copy(userDiamonds = -1)
        }

        userService.addRemoveDiamonds(-1)

        analyticsHelper.logEvent(AnalyticsEvent.SpendDiamonds(1, "skip_multichoicequestion"))
    }

    private suspend fun loadByCloudQuestions() {
        val questionSize =
            settingsDataStoreManager.getPreference(SettingsCommon.MultiChoiceQuizQuestionsSize)

        val category = savedStateHandle
            .get<MultiChoiceBaseCategory>(MultiChoiceQuizScreenNavArg::category.name)
            ?: MultiChoiceBaseCategory.Normal()

        val difficulty = savedStateHandle.get<String>(MultiChoiceQuizScreenNavArg::difficulty.name)

        if (category.hasCategory) {
            recentCategoriesRepository.addMultiChoiceCategory(category.id)
        }

        getRandomQuestionUseCase(questionSize, category, difficulty).collect { res ->
            if (res is Resource.Success) {
                createQuestionSteps(res.data.orEmpty(), category, difficulty)
            }
        }
    }

    private suspend fun List<MultiChoiceQuestion>.getOrTranslateQuestions(): List<MultiChoiceQuestion> {
        val translationEnabled =
            settingsDataStoreManager.getPreference(SettingsCommon.Translation.Enabled)
        val translationModelDownloaded = translationUtil.isModelDownloaded()
        val translateQuestions = translationEnabled && translationModelDownloaded

        if (!translateQuestions) return this

        return map { question ->
            question translateQuestionWith translationUtil
        }
    }

    private suspend infix fun MultiChoiceQuestion.translateQuestionWith(translationUtil: TranslatorUtil): MultiChoiceQuestion {
        return copy(
            description = translationUtil.translate(description),
            answers = translationUtil.translate(answers)
        )
    }

    private suspend fun createQuestionSteps(
        questions: List<MultiChoiceQuestion>,
        category: MultiChoiceBaseCategory,
        difficulty: String? = null
    ) {
        val questionSteps = questions
            .getOrTranslateQuestions()
            .map { question -> question.toQuestionStep() }

        _uiState.update { currentState ->
            currentState.copy(
                questionSteps = questionSteps,
                loading = false
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            // Update global event data, for daily challenge and achievements
            val event = if (category.hasCategory) {
                GameEvent.MultiChoice.PlayQuizWithCategory(category.id)
            } else {
                GameEvent.MultiChoice.PlayRandomQuiz
            }

            UpdateGlobalEventDataWorker.enqueueWork(workManager = workManager, event)

            // Log game start
            analyticsHelper.logEvent(
                AnalyticsEvent.MultiChoiceGameStart(
                    questionsSize = questionSteps.size,
                    category = category.toString(),
                    difficulty = difficulty
                )
            )
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

                    val questionTime = currentState.remainingTime.getElapsedSeconds(
                        MULTI_CHOICE_QUIZ_COUNTDOWN_TIME
                    )

                    if (currentQuestionStep != null) {
                        val questionCorrect =
                            currentState.selectedAnswer isCorrect currentQuestionStep.question

                        // Update play question and get answers correct for global event data
                        viewModelScope.launch(Dispatchers.IO) {
                            val events = if (questionCorrect) {
                                arrayOf(GameEvent.MultiChoice.PlayQuestions, GameEvent.MultiChoice.GetAnswersCorrect)
                            } else {
                                arrayOf(GameEvent.MultiChoice.PlayQuestions)
                            }

                            UpdateGlobalEventDataWorker.enqueueWork(
                                workManager = workManager,
                                event = events
                            )
                        }

                        val completedQuestionStep = currentQuestionStep.changeToCompleted(
                            questionCorrect,
                            currentState.selectedAnswer,
                            questionTime
                        )
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

        analyticsHelper.logEvent(AnalyticsEvent.MultiChoiceSaveQuestion)
    }

    private fun endGame(questionSteps: List<MultiChoiceQuestionStep.Completed>) {
        viewModelScope.launch(Dispatchers.IO) {
            val questionStepsStr = Json.encodeToString(questionSteps)
            val category = savedStateHandle.get<MultiChoiceBaseCategory?>(MultiChoiceQuizScreenNavArg::category.name)
                ?: MultiChoiceBaseCategory.Normal()

            val initialQuestions = savedStateHandle
                .get<ArrayList<MultiChoiceQuestion>>(MultiChoiceQuizScreenNavArg::initialQuestions.name)
                .orEmpty()

            val difficulty = savedStateHandle.get<String>(MultiChoiceQuizScreenNavArg::difficulty.name)

            val endGameWorkRequest = OneTimeWorkRequestBuilder<MultiChoiceQuizEndGameWorker>()
                .setInputData(
                    workDataOf(
                        MultiChoiceQuizEndGameWorker.INPUT_QUESTION_STEPS to questionStepsStr,
                        // Only generate XP if is not initial questions (saved questions)
                        MultiChoiceQuizEndGameWorker.INPUT_GENERATE_XP to initialQuestions.isEmpty(),
                    )
                ).build()


            val allCorrect = questionSteps.isAllCorrect()
            val mazeItemId = savedStateHandle
                .get<String?>(MultiChoiceQuizScreenNavArg::mazeItemId.name)
                ?.toIntOrNull()

            // Update end quiz for global event data
            viewModelScope.launch(Dispatchers.IO) {
                UpdateGlobalEventDataWorker.enqueueWork(
                    workManager = workManager,
                    GameEvent.MultiChoice.EndQuiz
                )

                if (mazeItemId != null) {
                    analyticsHelper.logEvent(AnalyticsEvent.MazeItemPlayed(allCorrect))
                }
            }

            if (mazeItemId != null && allCorrect) {
                // Runs the end game maze worker if is maze game mode and the question is correct
                val endGameMazeQuizWorkerRequest =
                    OneTimeWorkRequestBuilder<EndGameMazeQuizWorker>()
                        .setInputData(workDataOf(EndGameMazeQuizWorker.INPUT_MAZE_ITEM_ID to mazeItemId))
                        .build()

                workManager
                    .beginWith(endGameMazeQuizWorkerRequest)
                    .then(endGameWorkRequest)
                    .enqueue()
            } else {
                workManager.enqueue(endGameWorkRequest)
            }

            delay(1000)

            sendNavEventAsync(
                NavEvent.Navigate(
                    MultiChoiceQuizResultsScreenDestination(
                        questionStepsStr = questionStepsStr,
                        byInitialQuestions = initialQuestions.isNotEmpty(),
                        category = category,
                        difficulty = difficulty
                    )
                )
            )
        }
    }

    private fun getUserDiamonds() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update { currentState ->
            currentState.copy(userDiamondsLoading = true)
        }

        val userDiamonds = userService.getUserDiamonds()

        _uiState.update { currentState ->
            currentState.copy(
                userDiamonds = userDiamonds.toInt(),
                userDiamondsLoading = false
            )
        }
    }
}