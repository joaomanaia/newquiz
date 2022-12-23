package com.infinitepower.newquiz.data.worker.math_quiz.maze

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.core.util.kotlin.generateRandomUniqueItems
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.FlagQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.LogoQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    private val multiChoiceQuestionRepository: MultiChoiceQuestionRepository
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
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val seed = inputData.getInt(INPUT_SEED, Random.nextInt())
        val questionSize = inputData.getInt(INPUT_QUESTION_SIZE, 50)
        val gameModes = inputData.getIntArray(INPUT_GAME_MODES).toGameModes()

        // Random to use in all of the generators
        val random = Random(seed)

        val questionSizePerMode = questionSize / gameModes.size

        val allMazeItemsAsync = gameModes.map { mode ->
            async(Dispatchers.IO) {
                when (mode) {
                    GameModes.MULTI_CHOICE -> generateMultiChoiceMazeItems(
                        questionSize = questionSizePerMode,
                        random = random
                    )
                    GameModes.LOGO -> generateLogoMazeItems(
                        questionSize = questionSizePerMode,
                        random = random
                    )
                    GameModes.FLAG -> generateFlagMazeItems(
                        questionSize = questionSizePerMode,
                        random = random
                    )
                    GameModes.WORDLE -> generateWordleMazeItems(
                        questionSize = questionSizePerMode,
                        wordleQuizType = WordleQuizType.TEXT,
                        random = random
                    )
                    GameModes.GUESS_NUMBER -> generateWordleMazeItems(
                        questionSize = questionSizePerMode,
                        wordleQuizType = WordleQuizType.NUMBER,
                        random = random
                    )
                    GameModes.GUESS_MATH_FORMULA -> generateWordleMazeItems(
                        questionSize = questionSizePerMode,
                        wordleQuizType = WordleQuizType.MATH_FORMULA,
                        random = random
                    )
                }
            }
        }

        // Await for all the items to be generated and converts all to one list and finally shuffle all the list
        val allMazeItems = allMazeItemsAsync
            .awaitAll()
            .flatten()
            .shuffled(random)

        mazeMathQuizRepository.insertItems(allMazeItems)

        val questionsCount = mazeMathQuizRepository.countAllItems()

        if (questionsCount != allMazeItems.count())
            throw RuntimeException("Maze saved questions: $questionsCount is not equal to generated questions: ${allMazeItems.count()}")

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
        questionSize: Int,
        difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
        random: Random = Random
    ): List<MazeQuiz.MazeItem> = multiChoiceQuestionRepository.getRandomQuestions(
        amount = questionSize,
        random = random
    ).map { question ->
        MazeQuiz.MazeItem.MultiChoice(
            question = question,
            difficulty = difficulty
        )
    }

    private suspend fun generateLogoMazeItems(
        questionSize: Int,
        difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
        random: Random = Random
    ): List<MazeQuiz.MazeItem> = flagQuizRepository.getRandomQuestions(
        amount = questionSize,
        random = random
    ).map { question ->
        MazeQuiz.MazeItem.MultiChoice(
            question = question,
            difficulty = difficulty
        )
    }

    private suspend fun generateFlagMazeItems(
        questionSize: Int,
        difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
        random: Random = Random
    ): List<MazeQuiz.MazeItem> = logoQuizRepository.getRandomQuestions(
        amount = questionSize,
        random = random
    ).map { question ->
        MazeQuiz.MazeItem.MultiChoice(
            question = question,
            difficulty = difficulty
        )
    }

    private suspend fun generateWordleMazeItems(
        questionSize: Int,
        wordleQuizType: WordleQuizType,
        difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
        random: Random = Random
    ): List<MazeQuiz.MazeItem> = generateRandomUniqueItems(
        questionSize = questionSize,
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
            word = word,
            wordleQuizType = wordleQuizType,
            difficulty = difficulty
        )
    }
}