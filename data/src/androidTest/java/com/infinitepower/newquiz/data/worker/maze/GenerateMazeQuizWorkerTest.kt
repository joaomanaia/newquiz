package com.infinitepower.newquiz.data.worker.maze

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.database.AppDatabase
import com.infinitepower.newquiz.core.database.dao.MazeQuizDao
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.toBaseCategory
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Test for [GenerateMazeQuizWorker].
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class GenerateMazeQuizWorkerTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var mazeQuizDao: MazeQuizDao

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var wordleRepository: WordleRepository

    private lateinit var context: Context

    @BeforeTest
    fun setup() {
        hiltRule.inject()

        context = ApplicationProvider.getApplicationContext()
    }

    @AfterTest
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun testGenerateMazeQuizWorker() = runTest {
        val seed = 0
        val questionSize = 50

        val multiChoiceCategories = GenerateMazeQuizWorker.GameModes.MultiChoice.categories

        val multiChoiceBaseCategories = multiChoiceCategories.map { category ->
            category.toBaseCategory()
        }
        val multiChoiceBaseCategoriesStr = Json.encodeToString(multiChoiceBaseCategories)

        val wordleCategories = GenerateMazeQuizWorker.GameModes.Wordle.categories

        val wordleQuizTypes = wordleCategories.map { category ->
            category.wordleQuizType
        }
        val wordleQuizTypesStr = Json.encodeToString(wordleQuizTypes)

        val generateMazeQuizRequest = TestListenableWorkerBuilder<GenerateMazeQuizWorker>(context)
            .setWorkerFactory(workerFactory)
            .setInputData(
                workDataOf(
                    GenerateMazeQuizWorker.INPUT_SEED to seed,
                    GenerateMazeQuizWorker.INPUT_MULTI_CHOICE_CATEGORIES to multiChoiceBaseCategoriesStr,
                    GenerateMazeQuizWorker.INPUT_WORDLE_QUIZ_TYPES to wordleQuizTypesStr,
                    GenerateMazeQuizWorker.INPUT_QUESTION_SIZE to questionSize
                )
            ).build()

        val result = generateMazeQuizRequest.doWork()

        // Check if the worker finished successfully.
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(ListenableWorker.Result.success())

        // Check if the items were inserted correctly.
        val mazeQuizItems = mazeQuizDao.getAllMazeItems()

        val allCategoryCount = multiChoiceCategories.count() + wordleQuizTypes.count()
        val questionSizePerMode = questionSize / allCategoryCount
        // Get the real generated question size.
        // The real generated question size may be different from the input question size
        // Because the size is equally divided by the number of categories and is rounded down to the nearest integer.
        val realGeneratedQuestionSize = questionSizePerMode * allCategoryCount

        assertThat(mazeQuizItems).hasSize(realGeneratedQuestionSize)

        // Generate the items a second time to check if the items are the same, using the same seed.
        mazeQuizDao.deleteAll()

        val result2 = generateMazeQuizRequest.doWork()

        // Check if the worker finished successfully.
        assertThat(result2).isNotNull()
        assertThat(result2).isEqualTo(ListenableWorker.Result.success())

        val mazeQuizItems2 = mazeQuizDao.getAllMazeItems()

        // Check if the items are the same, ids are different.
        mazeQuizItems.forEachIndexed { index, mazeQuizItem ->
            val mazeQuizItem2 = mazeQuizItems2[index]

            assertThat(mazeQuizItem.difficulty).isEqualTo(mazeQuizItem2.difficulty)
            assertThat(mazeQuizItem.played).isEqualTo(mazeQuizItem2.played)
            assertThat(mazeQuizItem.type).isEqualTo(mazeQuizItem2.type)
            assertThat(mazeQuizItem.mazeSeed).isEqualTo(mazeQuizItem2.mazeSeed)

            assertThat(mazeQuizItem.multiChoiceQuestion?.description).isEqualTo(mazeQuizItem2.multiChoiceQuestion?.description)
            assertThat(mazeQuizItem.multiChoiceQuestion?.imageUrl).isEqualTo(mazeQuizItem2.multiChoiceQuestion?.imageUrl)

            if (mazeQuizItem.multiChoiceQuestion != null) {
                assertThat(mazeQuizItem.multiChoiceQuestion!!.answers).containsAnyIn(mazeQuizItem2.multiChoiceQuestion?.answers.orEmpty())
            }

            assertThat(mazeQuizItem.wordleItem).isEqualTo(mazeQuizItem2.wordleItem)
        }
    }
}
