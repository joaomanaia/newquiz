package com.infinitepower.newquiz.data.worker.maze

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.util.kotlin.generateRandomUniqueItems
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.data.local.wordle.WordleCategories
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.CountryCapitalFlagsQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.FlagQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.GuessMathSolutionRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.LogoQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizQuestion
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleCategory
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleWord
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.random.Random
import com.infinitepower.newquiz.core.R as CoreR

private val multiChoiceBaseCategoriesIds: List<String> = listOf(
    MultiChoiceBaseCategory.Random.id,
    MultiChoiceBaseCategory.Logo.id,
    MultiChoiceBaseCategory.Flag.id,
    MultiChoiceBaseCategory.CountryCapitalFlags.id,
    MultiChoiceBaseCategory.GuessMathSolution.id
)

@HiltWorker
class GenerateMazeQuizWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val mazeMathQuizRepository: MazeQuizRepository,
    private val mathQuizCoreRepository: MathQuizCoreRepository,
    private val flagQuizRepository: FlagQuizRepository,
    private val logoQuizRepository: LogoQuizRepository,
    private val wordleRepository: WordleRepository,
    private val multiChoiceQuestionRepository: MultiChoiceQuestionRepository,
    private val guessMathSolutionRepository: GuessMathSolutionRepository,
    private val countryCapitalFlagsQuizRepository: CountryCapitalFlagsQuizRepository,
    private val comparisonQuizRepository: ComparisonQuizRepository,
    private val remoteConfig: RemoteConfig,
    private val analyticsHelper: AnalyticsHelper,
) : CoroutineWorker(appContext, workerParams) {
    /**
     * Game modes available to generate the maze quiz.
     *
     * Number trivia category is not supported because of api limitations
     *
     * @param name Name of the game mode
     * @param categories Categories available to generate the maze quiz
     */
    sealed class GameModes<CategoryT : BaseCategory>(
        val name: UiText,
        val categories: List<CategoryT>
    ) {
        /**
         * Get the offline game mode categories.
         */
        fun getOfflineCategories(): List<CategoryT> = categories.filterNot { category ->
            category.requireInternetConnection
        }

        data object MultiChoice : GameModes<MultiChoiceCategory>(
            name = UiText.StringResource(CoreR.string.multi_choice_quiz),
            // Get only the categories that are in the multi choice base categories ids
            categories = multiChoiceQuestionCategories.filter { category ->
                multiChoiceBaseCategoriesIds.contains(category.id)
            } + MultiChoiceCategory( // Add the normal categories to the options
                id = MultiChoiceBaseCategory.Normal().categoryId,
                name = UiText.StringResource(CoreR.string.others),
                image = ""
            )
        )

        data object Wordle : GameModes<WordleCategory>(
            name = UiText.StringResource(CoreR.string.wordle),
            categories = WordleCategories.allCategories.filter { category ->
                category.wordleQuizType != WordleQuizType.NUMBER_TRIVIA
            }
        )
    }

    companion object {
        private const val TAG = "GenerateMazeQuizWorker"

        internal const val INPUT_SEED = "INPUT_SEED"
        internal const val INPUT_QUESTION_SIZE = "INPUT_QUESTION_SIZE"
        internal const val INPUT_MULTI_CHOICE_CATEGORIES = "INPUT_MULTI_CHOICE_CATEGORIES"
        internal const val INPUT_WORDLE_QUIZ_TYPES = "INPUT_WORDLE_QUIZ_TYPES"
        internal const val INPUT_COMPARISON_QUIZ_CATEGORIES = "INPUT_COMPARISON_QUIZ_CATEGORIES"

        /**
         * Enqueue a new work to generate a maze quiz.
         *
         * First it will clean the saved maze and then it will generate a new one.
         *
         * @param workManager WorkManager instance
         * @param seed Seed to use in the random generator
         * @param questionSize Question size to generate the maze
         * @return The work id
         */
        fun enqueue(
            workManager: WorkManager,
            seed: Int?,
            multiChoiceCategories: List<BaseCategory>,
            wordleCategories: List<BaseCategory>,
            comparisonQuizCategories: List<BaseCategory>,
            questionSize: Int? = null
        ): UUID {
            val cleanSavedMazeRequest = OneTimeWorkRequestBuilder<CleanMazeQuizWorker>().build()

            val multiChoiceCategoriesId = multiChoiceCategories.map { category ->
                category.id
            }.toTypedArray()

            val wordleCategoriesIds = wordleCategories.map { category ->
                category.id
            }.toTypedArray()

            val comparisonQuizCategoriesIds = comparisonQuizCategories.map { category ->
                category.id
            }.toTypedArray()

            val generateMazeRequest = OneTimeWorkRequestBuilder<GenerateMazeQuizWorker>()
                .setInputData(
                    workDataOf(
                        INPUT_SEED to seed,
                        INPUT_MULTI_CHOICE_CATEGORIES to multiChoiceCategoriesId,
                        INPUT_WORDLE_QUIZ_TYPES to wordleCategoriesIds,
                        INPUT_COMPARISON_QUIZ_CATEGORIES to comparisonQuizCategoriesIds,
                        INPUT_QUESTION_SIZE to questionSize
                    )
                ).build()

            workManager
                .beginWith(cleanSavedMazeRequest)
                .then(generateMazeRequest)
                .enqueue()

            return generateMazeRequest.id
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val seed = inputData.getInt(INPUT_SEED, Random.nextInt())

        val multiChoiceCategoriesIds = inputData.getStringArray(INPUT_MULTI_CHOICE_CATEGORIES)
        requireNotNull(multiChoiceCategoriesIds)
        Log.i(TAG, "Multi choice categories: $multiChoiceCategoriesIds")

        val wordleCategoriesIds = inputData.getStringArray(INPUT_WORDLE_QUIZ_TYPES)
        requireNotNull(wordleCategoriesIds)
        Log.i(TAG, "Wordle quiz types: $wordleCategoriesIds")

        val comparisonQuizCategoriesIds = inputData.getStringArray(INPUT_COMPARISON_QUIZ_CATEGORIES)
        requireNotNull(comparisonQuizCategoriesIds)
        Log.i(TAG, "Comparison quiz categories: $comparisonQuizCategoriesIds")

        val questionSize = inputData.getInt(
            INPUT_QUESTION_SIZE,
            remoteConfig.get(RemoteConfigValue.MAZE_QUIZ_GENERATED_QUESTIONS)
        )

        // Random to use in all of the generators
        val random = Random(seed)

        // Get the questions size per mode, this is the size of the questions that will be generated per mode
        val allCategoryCount = multiChoiceCategoriesIds.count() +
                wordleCategoriesIds.count() +
                comparisonQuizCategoriesIds.count()
        val questionSizePerMode = questionSize / allCategoryCount

        Log.i(
            TAG,
            "Generating maze with seed: $seed, question size: $questionSize, question size per mode: $questionSizePerMode"
        )

        val multiChoiceMazeQuestions = multiChoiceCategoriesIds.map { categoryId ->
            val quizType = MultiChoiceBaseCategory.fromId(categoryId)

            generateMultiChoiceMazeItems(
                mazeSeed = seed,
                questionSize = questionSizePerMode,
                multiChoiceQuizType = quizType,
                random = random
            )
        }

        val wordleMazeQuestions = wordleCategoriesIds.map { categoryId ->
            val wordleQuizType = WordleQuizType.valueOf(categoryId)

            generateWordleMazeItems(
                mazeSeed = seed,
                questionSize = questionSizePerMode,
                wordleQuizType = wordleQuizType,
                random = random
            )
        }

        val comparisonQuizCategories = comparisonQuizRepository.getCategories()
        val comparisonMazeQuestions = comparisonQuizCategoriesIds.mapNotNull { categoryId ->
            val category = comparisonQuizCategories.find { it.id == categoryId }
            if (category == null) return@mapNotNull null

            generateComparisonMazeItems(
                category = category,
                mazeSeed = seed,
                questionSize = questionSizePerMode,
                random = random
            )
        }

        val allMazeQuestions = (multiChoiceMazeQuestions + wordleMazeQuestions + comparisonMazeQuestions)
            .flatten()
            .shuffled(random)

        mazeMathQuizRepository.insertItems(allMazeQuestions)

        val questionsCount = mazeMathQuizRepository.countAllItems()

        check(questionsCount == allMazeQuestions.count()) {
            "Maze saved questions: $questionsCount is not equal to generated questions: ${allMazeQuestions.count()}"
        }

        Log.d(TAG, "Generated $questionsCount questions")

        analyticsHelper.logEvent(AnalyticsEvent.CreateMaze(seed, questionsCount))

        Result.success()
    }

    private suspend fun generateMultiChoiceMazeItems(
        mazeSeed: Int,
        questionSize: Int,
        multiChoiceQuizType: MultiChoiceBaseCategory,
        difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
        random: Random = Random
    ): List<MazeQuiz.MazeItem> {
        val questions = when (multiChoiceQuizType) {
            is MultiChoiceBaseCategory.Normal -> multiChoiceQuestionRepository.getRandomQuestions(
                amount = questionSize,
                random = random,
                category = multiChoiceQuizType
            )

            is MultiChoiceBaseCategory.Logo -> logoQuizRepository.getRandomQuestions(
                amount = questionSize,
                random = random,
                category = multiChoiceQuizType
            )

            is MultiChoiceBaseCategory.Flag -> flagQuizRepository.getRandomQuestions(
                amount = questionSize,
                random = random,
                category = multiChoiceQuizType
            )

            is MultiChoiceBaseCategory.GuessMathSolution -> guessMathSolutionRepository.getRandomQuestions(
                amount = questionSize,
                random = random,
                category = multiChoiceQuizType
            )

            is MultiChoiceBaseCategory.CountryCapitalFlags -> countryCapitalFlagsQuizRepository.getRandomQuestions(
                amount = questionSize,
                random = random,
                category = multiChoiceQuizType
            )

            // Temporary disabled because of api limitations
            is MultiChoiceBaseCategory.NumberTrivia -> error("Number trivia is not supported")
        }

        return questions.map { question ->
            MazeQuiz.MazeItem.MultiChoice(
                mazeSeed = mazeSeed,
                question = question,
                difficulty = difficulty
            )
        }
    }

    private suspend fun generateWordleMazeItems(
        mazeSeed: Int,
        questionSize: Int,
        wordleQuizType: WordleQuizType,
        difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
        random: Random = Random
    ): List<MazeQuiz.MazeItem> {
        require(wordleQuizType != WordleQuizType.NUMBER_TRIVIA) {
            "Number trivia is not supported"
        }

        if (wordleQuizType == WordleQuizType.TEXT) {
            return wordleRepository.generateRandomTextWords(
                count = questionSize,
                random = random
            ).map { word ->
                MazeQuiz.MazeItem.Wordle(
                    mazeSeed = mazeSeed,
                    wordleWord = word,
                    wordleQuizType = wordleQuizType,
                    difficulty = difficulty
                )
            }
        }

        return generateRandomUniqueItems(
            itemCount = questionSize,
            generator = {
                when (wordleQuizType) {
                    WordleQuizType.NUMBER -> wordleRepository.generateRandomNumberWord(random = random)
                    WordleQuizType.MATH_FORMULA -> {
                        val formula = mathQuizCoreRepository.generateMathFormula(
                            difficulty = difficulty,
                            random = random
                        )

                        WordleWord(formula.fullFormula)
                    }

                    else -> error("Wordle quiz type not supported")
                }
            }
        ).map { word ->
            MazeQuiz.MazeItem.Wordle(
                mazeSeed = mazeSeed,
                wordleWord = word,
                wordleQuizType = wordleQuizType,
                difficulty = difficulty
            )
        }
    }

    private suspend fun generateComparisonMazeItems(
        category: ComparisonQuizCategory,
        mazeSeed: Int,
        questionSize: Int,
        random: Random = Random
    ): List<MazeQuiz.MazeItem> {
        // Each question has two items, so we need to request twice as many questions
        val requestSize = questionSize * 2

        return comparisonQuizRepository.getQuestions(
            category = category,
            size = requestSize,
            random = random
        ).chunked(2) { items ->
            val firstQuestion = items.first()
            val secondQuestion = items.last()

            MazeQuiz.MazeItem.ComparisonQuiz(
                mazeSeed = mazeSeed,
                question = ComparisonQuizQuestion(
                    questions = firstQuestion to secondQuestion,
                    categoryId = category.id,
                    comparisonMode = ComparisonMode.GREATER // In future we can make this random
                ),
            )
        }
    }
}
