package com.infinitepower.newquiz.data.worker.maze

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.core.analytics.logging.CoreLoggingAnalytics
import com.infinitepower.newquiz.core.multi_choice_quiz.MultiChoiceQuizType
import com.infinitepower.newquiz.core.util.kotlin.generateRandomUniqueItems
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.FlagQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.GuessMathSolutionRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.LogoQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

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
    private val coreLoggingAnalytics: CoreLoggingAnalytics
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val INPUT_SEED = "INPUT_SEED"
        const val INPUT_QUESTION_SIZE = "INPUT_QUESTION_SIZE"
        const val INPUT_GAME_MODES = "INPUT_GAME_MODES"

        enum class GameModes {
            MULTI_CHOICE,
            LOGO,
            FLAG,
            WORDLE,
            GUESS_NUMBER,
            GUESS_MATH_FORMULA,
            GUESS_MATH_SOLUTION;

            companion object {
                private fun offlineGameModes(): List<GameModes> = listOf(WORDLE, GUESS_NUMBER, GUESS_MATH_FORMULA, GUESS_MATH_SOLUTION)

                fun offlineGameModesKeys(): List<Int> = offlineGameModes().map { gameMode ->
                    GameModes.values().indexOf(gameMode)
                }
            }
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val seed = inputData.getInt(INPUT_SEED, Random.nextInt())
        val questionSize = inputData.getInt(INPUT_QUESTION_SIZE, 50)

        val inputGameModes = inputData.getIntArray(INPUT_GAME_MODES)
        val gameModes = inputGameModes.toGameModes()

        // Random to use in all of the generators
        val random = Random(seed)

        val questionSizePerMode = questionSize / gameModes.size

        val allMazeItemsAsync = gameModes.map { mode ->
            when (mode) {
                GameModes.MULTI_CHOICE -> generateMultiChoiceMazeItems(
                    mazeSeed = seed,
                    questionSize = questionSizePerMode,
                    multiChoiceQuizType = MultiChoiceQuizType.NORMAL,
                    random = random
                )
                GameModes.LOGO -> generateMultiChoiceMazeItems(
                    mazeSeed = seed,
                    questionSize = questionSizePerMode,
                    multiChoiceQuizType = MultiChoiceQuizType.LOGO,
                    random = random
                )
                GameModes.FLAG -> generateMultiChoiceMazeItems(
                    mazeSeed = seed,
                    questionSize = questionSizePerMode,
                    multiChoiceQuizType = MultiChoiceQuizType.FLAG,
                    random = random
                )
                GameModes.WORDLE -> generateWordleMazeItems(
                    mazeSeed = seed,
                    questionSize = questionSizePerMode,
                    wordleQuizType = WordleQuizType.TEXT,
                    random = random
                )
                GameModes.GUESS_NUMBER -> generateWordleMazeItems(
                    mazeSeed = seed,
                    questionSize = questionSizePerMode,
                    wordleQuizType = WordleQuizType.NUMBER,
                    random = random
                )
                GameModes.GUESS_MATH_FORMULA -> generateWordleMazeItems(
                    mazeSeed = seed,
                    questionSize = questionSizePerMode,
                    wordleQuizType = WordleQuizType.MATH_FORMULA,
                    random = random
                )
                GameModes.GUESS_MATH_SOLUTION -> generateMultiChoiceMazeItems(
                    mazeSeed = seed,
                    questionSize = questionSizePerMode,
                    multiChoiceQuizType = MultiChoiceQuizType.GUESS_MATH_SOLUTION,
                    random = random
                )
            }
        }

        // Await for all the items to be generated and converts all to one list and finally shuffle all the list
        val allMazeItems = allMazeItemsAsync
            .flatten()
            .shuffled(random)

        mazeMathQuizRepository.insertItems(allMazeItems)

        val questionsCount = mazeMathQuizRepository.countAllItems()

        if (questionsCount != allMazeItems.count())
            throw RuntimeException("Maze saved questions: $questionsCount is not equal to generated questions: ${allMazeItems.count()}")

        coreLoggingAnalytics.logCreateMaze(seed, questionsCount, inputGameModes?.toList().orEmpty())

        Result.success()
    }

    private fun IntArray?.toGameModes(): List<GameModes> {
        if (this == null) return GameModes.values().toList()

        val gameModes = GameModes.values()

        return map { key ->
            gameModes.getOrNull(key)
        }.filterNotNull()
    }

    private suspend fun generateMultiChoiceMazeItems(
        mazeSeed: Int,
        questionSize: Int,
        multiChoiceQuizType: MultiChoiceQuizType,
        difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
        random: Random = Random
    ): List<MazeQuiz.MazeItem> {
        val questions = when (multiChoiceQuizType) {
            MultiChoiceQuizType.NORMAL -> multiChoiceQuestionRepository.getRandomQuestions(
                amount = questionSize,
                random = random
            )
            MultiChoiceQuizType.LOGO -> logoQuizRepository.getRandomQuestions(
                amount = questionSize,
                random = random
            )
            MultiChoiceQuizType.FLAG -> flagQuizRepository.getRandomQuestions(
                amount = questionSize,
                random = random
            )
            MultiChoiceQuizType.GUESS_MATH_SOLUTION -> guessMathSolutionRepository.getRandomQuestions(
                amount = questionSize,
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

                    formula.fullFormulaWithoutSpaces
                }
            }
        }
    ).map { word ->
        MazeQuiz.MazeItem.Wordle(
            mazeSeed = mazeSeed,
            word = word,
            wordleQuizType = wordleQuizType,
            difficulty = difficulty
        )
    }
}