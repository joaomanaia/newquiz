package com.infinitepower.newquiz.multi_choice_quiz

import android.os.CountDownTimer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.infinitepower.newquiz.core.analytics.logging.CoreLoggingAnalytics
import com.infinitepower.newquiz.core.analytics.logging.maze.MazeLoggingAnalytics
import com.infinitepower.newquiz.core.analytics.logging.multi_choice_quiz.MultiChoiceQuizLoggingAnalytics
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.common.viewmodel.NavEvent
import com.infinitepower.newquiz.core.common.viewmodel.NavEventViewModel
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.SettingsDataStoreManager
import com.infinitepower.newquiz.data.worker.UpdateGlobalEventDataWorker
import com.infinitepower.newquiz.data.worker.maze.EndGameMazeQuizWorker
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import com.infinitepower.newquiz.domain.use_case.question.GetRandomMultiChoiceQuestionUseCase
import com.infinitepower.newquiz.domain.use_case.question.IsQuestionSavedUseCase
import com.infinitepower.newquiz.model.RemainingTime
import com.infinitepower.newquiz.model.global_event.GameEvent
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.SelectedAnswer
import com.infinitepower.newquiz.model.multi_choice_quiz.isAllCorrect
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizResultsScreenDestination
import com.infinitepower.newquiz.online_services.core.worker.multichoicequiz.MultiChoiceQuizEndGameWorker
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import com.infinitepower.newquiz.translation_dynamic_feature.TranslatorUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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
    private val multiChoiceQuestionsRepository: MultiChoiceQuestionRepository,
    private val savedStateHandle: SavedStateHandle,
    private val multiChoiceQuizLoggingAnalytics: MultiChoiceQuizLoggingAnalytics,
    private val translationUtil: TranslatorUtil,
    private val workManager: WorkManager,
    private val userRepository: UserRepository,
    private val coreLoggingAnalytics: CoreLoggingAnalytics,
    private val mazeLoggingAnalytics: MazeLoggingAnalytics,
    private val authUserRepository: AuthUserRepository,
    private val isQuestionSavedUseCase: IsQuestionSavedUseCase
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
                    currentState.copy(userDiamonds = -1)
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

        _uiState.update { currentState ->
            currentState.copy(userSignedIn = authUserRepository.isSignedIn)
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

        userRepository.addLocalUserDiamonds(-1)

        coreLoggingAnalytics.logSpendDiamonds(1, "skip_multichoicequestion")
    }

    private suspend fun loadByCloudQuestions() {
        val questionSize =
            settingsDataStoreManager.getPreference(SettingsCommon.MultiChoiceQuizQuestionsSize)

        val category = savedStateHandle
            .get<MultiChoiceBaseCategory>(MultiChoiceQuizScreenNavArg::category.name)
            ?: MultiChoiceBaseCategory.Normal()

        val difficulty = savedStateHandle.get<String>(MultiChoiceQuizScreenNavArg::difficulty.name)

        if (category.hasCategory) {
            multiChoiceQuestionsRepository.addCategoryToRecent(category)
        }

        getRandomQuestionUseCase(questionSize, category, difficulty).collect { res ->
            if (res is Resource.Success) {
                createQuestionSteps(res.data.orEmpty(), category, difficulty)
            }
        }
    }

    private suspend fun List<MultiChoiceQuestion>.getOrTranslateQuestions(): List<MultiChoiceQuestion> {
        val translationEnabled =
            settingsDataStoreManager.getPreference(SettingsCommon.MultiChoiceQuiz.TranslationEnabled)
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
            answers = answers.map { answer ->
                translationUtil.translate(answer)
            }
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
            currentState.copy(questionSteps = questionSteps)
        }

        viewModelScope.launch(Dispatchers.IO) {
            // Update global event data, for daily challenge and achievements
            val event = if (category.hasCategory) {
                GameEvent.MultiChoice.PlayQuizWithCategory(category.key)
            } else {
                GameEvent.MultiChoice.PlayRandomQuiz
            }

            UpdateGlobalEventDataWorker.enqueueWork(workManager = workManager, event)

            // Log game start
            multiChoiceQuizLoggingAnalytics.logGameStart(
                questionsSize = questionSteps.size,
                category = category,
                difficulty = difficulty
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
                        MultiChoiceQuizEndGameWorker.INPUT_SAVE_NEW_XP to initialQuestions.isEmpty(),
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
                    mazeLoggingAnalytics.logMazeItemPlayed(allCorrect)
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
        val user = try {
            userRepository.getLocalUser() ?: throw NullPointerException()
        } catch (e: Exception) {
            e.printStackTrace()
            return@launch
        }

        _uiState.update { currentState ->
            currentState.copy(userDiamonds = user.data.diamonds.toInt())
        }
    }
}