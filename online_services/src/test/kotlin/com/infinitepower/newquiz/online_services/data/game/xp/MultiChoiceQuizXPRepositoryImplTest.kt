package com.infinitepower.newquiz.online_services.data.game.xp

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.util.kotlin.sum
import com.infinitepower.newquiz.data.local.question.QuestionDifficulty
import com.infinitepower.newquiz.model.multi_choice_quiz.*
import com.infinitepower.newquiz.online_services.domain.game.xp.MultiChoiceQuizXPRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import kotlin.math.roundToInt
import kotlin.random.Random


class MultiChoiceQuizXPRepositoryImplTest {
    private lateinit var multiChoiceQuizXPRepository: MultiChoiceQuizXPRepository

    @BeforeEach
    fun setUp() {
        multiChoiceQuizXPRepository = MultiChoiceQuizXPRepositoryImpl()
    }

    @Test
    @DisplayName("Should return 20..20 when the difficulty is medium")
    fun randomXPRange() {
        val expectedRange = 10..20

        val actualRange = multiChoiceQuizXPRepository.randomXPRange()

        assertThat(actualRange).isEqualTo(expectedRange)
    }

    @Test
    @DisplayName("Should return 1 when the difficulty is easy")
    fun getXpMultiplierFactorWhenDifficultyIsEasy() {
        val xpMultiplierFactor =
            multiChoiceQuizXPRepository.getXpMultiplierFactor(QuestionDifficulty.Easy)

        assertThat(xpMultiplierFactor).isEqualTo(1f)
    }

    @Test
    @DisplayName("Should return 1.5 when the difficulty is medium")
    fun getXpMultiplierFactorWhenDifficultyIsMedium() {
        val difficulty = QuestionDifficulty.Medium
        val expected = 1.5f

        val actual = multiChoiceQuizXPRepository.getXpMultiplierFactor(difficulty)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    @DisplayName("Should return a random number between 10 and 20 when difficulty is easy")
    fun generateRandomXPShouldReturnARandomNumberBetween10And20WhenTheDifficultyIsEasy() {
        val randomXP = multiChoiceQuizXPRepository.generateRandomXP(QuestionDifficulty.Easy)

        assertThat(randomXP).isIn(10..20)
    }

    @Test
    @DisplayName("Should return a random number between 10 and 30 when difficulty is medium")
    fun generateRandomXPShouldReturnARandomNumberBetween10And30WhenTheDifficultyIsEasy() {
        val randomXP = multiChoiceQuizXPRepository.generateRandomXP(QuestionDifficulty.Easy)

        assertThat(randomXP).isIn(10..30)
    }

    @Test
    @DisplayName("Should return a random number between 10 and 40 when difficulty is hard")
    fun generateRandomXPShouldReturnARandomNumberBetween10And40WhenTheDifficultyIsHard() {
        val randomXP = multiChoiceQuizXPRepository.generateRandomXP(QuestionDifficulty.Hard)

        assertThat(randomXP).isIn(10..40)
    }


    // Random xp from question steps

    @Test
    @RepeatedTest(10)
    @DisplayName("Should return 0 when the question steps is empty")
    fun generateQuestionsRandomXPWhenQuestionStepsIsEmptyThenReturnZero() {
        val questionSteps = emptyList<MultiChoiceQuestionStep.Completed>()

        val randomXP = multiChoiceQuizXPRepository.generateQuestionsRandomXP(questionSteps)

        assertThat(randomXP).isEqualTo(0)
    }

    @Test
    @RepeatedTest(10)
    @DisplayName("Should return 0 when the question steps is not empty but all of them are incorrect")
    fun generateQuestionsRandomXPWhenQuestionStepsIsNotEmptyButAllOfThemAreIncorrectThenReturnZero() {
        val questionSteps = List(5) {
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = false,
                selectedAnswer = SelectedAnswer.NONE
            )
        }

        val randomXP = multiChoiceQuizXPRepository.generateQuestionsRandomXP(questionSteps)

        assertThat(randomXP).isEqualTo(0)
    }

    @Test
    @DisplayName("Should return 0 when the question steps size is 5 and all is incorrect")
    fun generateQuestionsRandomXPWhenQuestionStepsSizeIs5AndAllIncorrectThen0() {
        val questionSteps = List(5) {
            MultiChoiceQuestionStep.Completed(
                question = MultiChoiceQuestion(
                    id = it,
                    description = "",
                    imageUrl = null,
                    answers = emptyList(),
                    lang = QuestionLanguage.EN.name,
                    category = "",
                    correctAns = 0,
                    type = "multiple",
                    difficulty = "easy"
                ),
                correct = false,
                selectedAnswer = SelectedAnswer.NONE
            )
        }

        val randomXP = multiChoiceQuizXPRepository.generateQuestionsRandomXP(questionSteps)

        assertThat(randomXP).isEqualTo(0)
    }

    @Test
    @RepeatedTest(10)
    @DisplayName("Should return a random number between 50 and 100 when the question steps size is 5 and difficulty is easy")
    fun generateQuestionsRandomXPWhenQuestionStepsSizeIs5ThenReturns50To100() {
        val questionSteps = List(5) {
            MultiChoiceQuestionStep.Completed(
                question = MultiChoiceQuestion(
                    id = it,
                    description = "",
                    imageUrl = null,
                    answers = emptyList(),
                    lang = QuestionLanguage.EN.name,
                    category = "",
                    correctAns = 0,
                    type = "multiple",
                    difficulty = "easy"
                ),
                correct = true,
                selectedAnswer = SelectedAnswer.NONE
            )
        }

        val randomXP = multiChoiceQuizXPRepository.generateQuestionsRandomXP(questionSteps)

        assertThat(randomXP).isIn(50..100)
    }

    @Test
    @RepeatedTest(10)
    @DisplayName("Should return a random number when the question steps size is 5 and difficulty is random")
    fun generateQuestionsRandomXPWhenQuestionStepsSizeIs5ThenReturnsRandomNumber() {
        val questionSteps = List(5) {
            MultiChoiceQuestionStep.Completed(
                question = MultiChoiceQuestion(
                    id = it,
                    description = "",
                    imageUrl = null,
                    answers = emptyList(),
                    lang = QuestionLanguage.EN.name,
                    category = "",
                    correctAns = 0,
                    type = "multiple",
                    difficulty = QuestionDifficulty.items().random().id
                ),
                correct = Random.nextBoolean(),
                selectedAnswer = SelectedAnswer.NONE
            )
        }

        val randomXP = multiChoiceQuizXPRepository.generateQuestionsRandomXP(questionSteps)

        val xpObtainable = questionSteps
            .filter(MultiChoiceQuestionStep.Completed::correct)
            .map { step ->
                val difficultyStr = step.question.difficulty
                val difficulty = QuestionDifficulty.from(difficultyStr)

                val multiplier = multiChoiceQuizXPRepository.getXpMultiplierFactor(difficulty)

                val minXPObtainable = (10 * multiplier).roundToInt()
                val maxXPObtainable = (20 * multiplier).roundToInt()

                minXPObtainable..maxXPObtainable
            }.sum()

        assertThat(randomXP).isIn(xpObtainable)
    }
}