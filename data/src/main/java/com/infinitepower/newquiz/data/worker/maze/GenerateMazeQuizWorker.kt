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
import com.infinitepower.newquiz.core.util.kotlin.generateRandomUniqueItems
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.data.local.wordle.WordleCategories
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.CountryCapitalFlagsQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.FlagQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.GuessMathSolutionRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.LogoQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.domain.repository.numbers.NumberTriviaQuestionRepository
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.config.RemoteConfigApi
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.toBaseCategory
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleCategory
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleWord
import com.infinitepower.newquiz.core.R as CoreR
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import kotlin.random.Random

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
    private val numberTriviaQuestionRepository: NumberTriviaQuestionRepository,
    private val countryCapitalFlagsQuizRepository: CountryCapitalFlagsQuizRepository,
    private val remoteConfigApi: RemoteConfigApi,
    private val analyticsHelper: AnalyticsHelper
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

        private const val INPUT_SEED = "INPUT_SEED"
        private const val INPUT_QUESTION_SIZE = "INPUT_QUESTION_SIZE"
        private const val INPUT_MULTI_CHOICE_CATEGORIES = "INPUT_MULTI_CHOICE_CATEGORIES"
        private const val INPUT_WORDLE_QUIZ_TYPES = "INPUT_WORDLE_QUIZ_TYPES"

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
            multiChoiceCategories: List<MultiChoiceCategory>,
            wordleCategories: List<WordleCategory>,
            questionSize: Int? = null
        ): UUID {
            val cleanSavedMazeRequest = OneTimeWorkRequestBuilder<CleanMazeQuizWorker>().build()

            val multiChoiceBaseCategories = multiChoiceCategories.map { category ->
                category.toBaseCategory()
            }
            val multiChoiceBaseCategoriesStr = Json.encodeToString(multiChoiceBaseCategories)

            val wordleQuizTypes = wordleCategories.map { category ->
                category.wordleQuizType
            }
            val wordleQuizTypesStr = Json.encodeToString(wordleQuizTypes)

            val generateMazeRequest = OneTimeWorkRequestBuilder<GenerateMazeQuizWorker>()
                .setInputData(
                    workDataOf(
                        INPUT_SEED to seed,
                        INPUT_MULTI_CHOICE_CATEGORIES to multiChoiceBaseCategoriesStr,
                        INPUT_WORDLE_QUIZ_TYPES to wordleQuizTypesStr,
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

        val remoteConfigQuestionSize = remoteConfigApi.getInt("maze_quiz_generated_questions")

        val questionSize = inputData.getInt(INPUT_QUESTION_SIZE, remoteConfigQuestionSize)

        val multiChoiceCategoriesStr = inputData.getString(INPUT_MULTI_CHOICE_CATEGORIES)
            ?: throw RuntimeException("Multi choice categories is null")
        val multiChoiceCategories =
            Json.decodeFromString<List<MultiChoiceBaseCategory>>(multiChoiceCategoriesStr)

        Log.i(TAG, "Multi choice categories: $multiChoiceCategoriesStr")

        val wordleQuizTypesStr = inputData.getString(INPUT_WORDLE_QUIZ_TYPES)
            ?: throw RuntimeException("Wordle categories is null")
        val wordleQuizTypes = Json.decodeFromString<List<WordleQuizType>>(wordleQuizTypesStr)

        Log.i(TAG, "Wordle quiz types: $wordleQuizTypesStr")

        // Random to use in all of the generators
        val random = Random(seed)

        // Get the questions size per mode, this is the size of the questions that will be generated per mode
        val allCategoryCount = multiChoiceCategories.count() + wordleQuizTypes.count()
        val questionSizePerMode = questionSize / allCategoryCount

        Log.i(
            TAG,
            "Generating maze with seed: $seed, question size: $questionSize, question size per mode: $questionSizePerMode"
        )

        // Generate all the items async
        val multiChoiceMazeQuestions = multiChoiceCategories.map { category ->
            async {
                generateMultiChoiceMazeItems(
                    mazeSeed = seed,
                    questionSize = questionSizePerMode,
                    multiChoiceQuizType = category,
                    random = random
                )
            }
        }

        val wordleMazeQuestions = wordleQuizTypes.map { wordleQuizType ->
            async {
                generateWordleMazeItems(
                    mazeSeed = seed,
                    questionSize = questionSizePerMode,
                    wordleQuizType = wordleQuizType,
                    random = random
                )
            }
        }

        // Await for all the items to be generated and converts all to one list and finally shuffle all the list
        val allMazeQuestions = (multiChoiceMazeQuestions + wordleMazeQuestions)
            .awaitAll()
            .flatten()
            .shuffled(random)

        mazeMathQuizRepository.insertItems(allMazeQuestions)

        val questionsCount = mazeMathQuizRepository.countAllItems()

        if (questionsCount != allMazeQuestions.count())
            throw RuntimeException("Maze saved questions: $questionsCount is not equal to generated questions: ${allMazeQuestions.count()}")

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
            is MultiChoiceBaseCategory.NumberTrivia -> numberTriviaQuestionRepository.generateMultiChoiceQuestion(
                size = questionSize,
                random = random
            )
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
    ): List<MazeQuiz.MazeItem> = generateRandomUniqueItems(
        itemCount = questionSize,
        generator = {
            when (wordleQuizType) {
                WordleQuizType.TEXT -> wordleRepository.generateRandomTextWord(random = random)
                WordleQuizType.NUMBER -> wordleRepository.generateRandomNumberWord(random = random)
                WordleQuizType.MATH_FORMULA -> {
                    val formula = mathQuizCoreRepository.generateMathFormula(
                        difficulty = difficulty,
                        random = random
                    )

                    WordleWord(formula.fullFormula)
                }
                // Temporary disabled because of api limitations
                WordleQuizType.NUMBER_TRIVIA -> numberTriviaQuestionRepository.generateWordleQuestion(
                    random
                )
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