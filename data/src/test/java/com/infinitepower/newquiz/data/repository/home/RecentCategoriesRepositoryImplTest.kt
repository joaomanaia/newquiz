package com.infinitepower.newquiz.data.repository.home

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.common.dataStore.RecentCategoryDataStoreCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.dataStore.manager.PreferenceRequest
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.data.local.wordle.WordleCategories
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizFormatType
import com.infinitepower.newquiz.model.toUiText
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

/**
 * Tests for [RecentCategoriesRepositoryImpl]
 */
internal class RecentCategoriesRepositoryImplTest {
    private lateinit var recentCategoriesRepository: RecentCategoriesRepositoryImpl

    private val recentCategoriesDataStoreManager: DataStoreManager = mockk(relaxed = true)
    private val comparisonQuizRepository: ComparisonQuizRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        // Create an instance of RecentCategoriesRepositoryImpl with mocked dependencies
        recentCategoriesRepository = RecentCategoriesRepositoryImpl(
            recentCategoriesDataStoreManager,
            comparisonQuizRepository
        )
    }

    @AfterEach
    fun tearDown() {
        // Clear any recorded calls and reset mocks after each test
        clearAllMocks()
    }

    private data class TestCategory(
        override val id: String,
        override val name: UiText,
        override val image: String,
        override val requireInternetConnection: Boolean
    ) : BaseCategory

    private val testCategories = List(10) {
        TestCategory(
            id = "id$it",
            name = "name$it".toUiText(),
            image = "image$it",
            requireInternetConnection = it % 2 == 0
        )
    }

    @Test
    fun `getCategories should return expected result, when connection available and have recent categories`() = runTest {
        // Mock the necessary dependencies and data
        val recentCategories = listOf(
            testCategories[0],
            testCategories[1],
            testCategories[2]
        )

        val recentCategoriesIds = recentCategories
            .map { it.id }
            .toSet()

        // Call the method under test
        val result = recentCategoriesRepository.getHomeCategories(
            allCategories = testCategories,
            recentCategoriesFlow = flowOf(recentCategoriesIds),
            isInternetAvailable = true
        ).first()

        val otherCategories = testCategories.filterNot { it in recentCategories }

        // Verify the result
        assertThat(result.recentCategories.size).isAtMost(3)
        assertThat(result.recentCategories).containsExactlyElementsIn(recentCategories)

        assertThat(result.otherCategories).containsExactlyElementsIn(otherCategories)
    }

    @Test
    fun `getCategories should return expected result, when connection available and have no recent categories`() = runTest {
        // Mock the necessary dependencies and data
        val recentCategoriesIds = emptySet<String>()

        // Call the method under test
        val result = recentCategoriesRepository.getHomeCategories(
            allCategories = testCategories,
            recentCategoriesFlow = flowOf(recentCategoriesIds),
            isInternetAvailable = true
        ).first()

        // Verify the result
        assertThat(result.recentCategories).isNotEmpty()
        assertThat(result.recentCategories).hasSize(3)

        assertThat(result.otherCategories).hasSize(testCategories.size - 3)
        assertThat(result.otherCategories).containsExactlyElementsIn(testCategories - result.recentCategories.toSet())
    }

    @Test
    fun `getCategories should return expected result, when connection not available and have recent categories`() = runTest {
        // Mock the necessary dependencies and data
        val recentCategories = listOf(
            testCategories[0],
            testCategories[1],
            testCategories[2]
        )

        val recentCategoriesIds = recentCategories
            .map { it.id }
            .toSet()

        // Call the method under test
        val result = recentCategoriesRepository.getHomeCategories(
            allCategories = testCategories,
            recentCategoriesFlow = flowOf(recentCategoriesIds),
            isInternetAvailable = false
        ).first()

        val otherCategories = testCategories.filterNot { it in recentCategories }

        // Verify the result
        assertThat(result.recentCategories.size).isAtMost(3)
        assertThat(result.recentCategories).containsExactlyElementsIn(recentCategories)

        assertThat(result.otherCategories).containsExactlyElementsIn(otherCategories)

        // Check that the other categories with no internet connection are on the top of the list
        val otherCategoriesSorted = otherCategories.sortedBy { it.requireInternetConnection }
        assertThat(result.otherCategories).isEqualTo(otherCategoriesSorted)
    }

    @Test
    fun `getCategories should return expected result, when connection not available and have no recent categories`() = runTest {
        // Mock the necessary dependencies and data
        val recentCategoriesIds = emptySet<String>()

        // Call the method under test
        val result = recentCategoriesRepository.getHomeCategories(
            allCategories = testCategories,
            recentCategoriesFlow = flowOf(recentCategoriesIds),
            isInternetAvailable = false
        ).first()

        // Verify the result
        assertThat(result.recentCategories).isNotEmpty()

        assertThat(result.recentCategories.size).isAtMost(3)

        assertThat(result.otherCategories).hasSize(testCategories.size - result.recentCategories.size)
        assertThat(result.otherCategories).containsExactlyElementsIn(testCategories - result.recentCategories.toSet())
    }

    @Test
    fun `getCategories should return expected result, when connection not available and have no recent categories and all categories require internet`() = runTest {
        val testCategories = List(10) {
            TestCategory(
                id = "id$it",
                name = "name$it".toUiText(),
                image = "image$it",
                requireInternetConnection = true
            )
        }

        // Mock the necessary dependencies and data
        val recentCategoriesIds = emptySet<String>()

        // Call the method under test
        val result = recentCategoriesRepository.getHomeCategories(
            allCategories = testCategories,
            recentCategoriesFlow = flowOf(recentCategoriesIds),
            isInternetAvailable = false
        ).first()

        // Verify the result
        assertThat(result.recentCategories).isNotEmpty()
        assertThat(result.recentCategories).hasSize(3)

        assertThat(result.otherCategories).hasSize(testCategories.size - result.recentCategories.size)
    }

    @ParameterizedTest
    @MethodSource("getMultiChoiceCategoriesParams")
    fun `getMultiChoiceCategories should return expected result with parameters`(
        isInternetAvailable: Boolean,
        recentCategoriesIds: Set<String>
    ) = runTest {
        every { recentCategoriesDataStoreManager.getPreferenceFlow<Set<String>>(any()) } returns flowOf(recentCategoriesIds)

        // Call the method under test
        val result = recentCategoriesRepository.getMultiChoiceCategories(
            isInternetAvailable = isInternetAvailable
        ).first()

        // Verify the result
        assertThat(result).isNotNull()

        assertThat(result.recentCategories).isNotEmpty()
        assertThat(result.recentCategories.size).isAtMost(3)

        if (recentCategoriesIds.isNotEmpty()) {
            val recentCategories = multiChoiceQuestionCategories.filter { it.id in recentCategoriesIds }
            assertThat(result.recentCategories).containsExactlyElementsIn(recentCategories)
        }

        assertThat(result.otherCategories).hasSize(multiChoiceQuestionCategories.size - result.recentCategories.size)
        assertThat(result.otherCategories).containsExactlyElementsIn(multiChoiceQuestionCategories - result.recentCategories.toSet())
    }

    @ParameterizedTest
    @MethodSource("getWordleCategoriesParams")
    fun `getWordCategories should return expected result with parameters`(
        isInternetAvailable: Boolean,
        recentCategoriesIds: Set<String>
    ) = runTest {
        every { recentCategoriesDataStoreManager.getPreferenceFlow<Set<String>>(any()) } returns flowOf(recentCategoriesIds)

        // Call the method under test
        val result = recentCategoriesRepository.getWordleCategories(
            isInternetAvailable = isInternetAvailable
        ).first()

        // Verify the result
        assertThat(result).isNotNull()

        assertThat(result.recentCategories).isNotEmpty()
        assertThat(result.recentCategories.size).isAtMost(3)

        val allWordleCategories = WordleCategories.allCategories

        if (recentCategoriesIds.isNotEmpty()) {
            val recentCategories = allWordleCategories.filter { it.id in recentCategoriesIds }
            assertThat(result.recentCategories).containsExactlyElementsIn(recentCategories)
        }

        assertThat(result.otherCategories).hasSize(allWordleCategories.size - result.recentCategories.size)
        assertThat(result.otherCategories).containsExactlyElementsIn(allWordleCategories - result.recentCategories.toSet())
    }

    @ParameterizedTest
    @MethodSource("getComparisonCategoriesParams")
    fun `getComparisonCategories should return expected result with parameters`(
        isInternetAvailable: Boolean,
        recentCategoriesIds: Set<String>
    ) = runTest {
        every { comparisonQuizRepository.getCategories() } returns allComparisonQuizCategories
        every { recentCategoriesDataStoreManager.getPreferenceFlow<Set<String>>(any()) } returns flowOf(recentCategoriesIds)

        // Call the method under test
        val result = recentCategoriesRepository.getComparisonCategories(
            isInternetAvailable = isInternetAvailable
        ).first()

        // Verify the result
        assertThat(result).isNotNull()

        assertThat(result.recentCategories).isNotEmpty()
        assertThat(result.recentCategories.size).isAtMost(3)

        if (recentCategoriesIds.isNotEmpty()) {
            val recentCategories = allComparisonQuizCategories.filter { it.id in recentCategoriesIds }
            assertThat(result.recentCategories).containsExactlyElementsIn(recentCategories)
        }

        assertThat(result.otherCategories).hasSize(allComparisonQuizCategories.size - result.recentCategories.size)
        assertThat(result.otherCategories).containsExactlyElementsIn(allComparisonQuizCategories - result.recentCategories.toSet())
    }

    // Tests for addMultiChoiceCategory
    @ParameterizedTest
    @MethodSource("addMultiChoiceCategoryParams")
    fun `addMultiChoiceCategory should add category to recent categories`(
        categoryIdToAdd: String,
        initialCategories: Set<String>
    ) = runTest {
        // Mock the necessary dependencies and data
        coEvery { recentCategoriesDataStoreManager.getPreference<Set<String>>(any()) } returns initialCategories
        coJustRun { recentCategoriesDataStoreManager.editPreference<Set<String>>(any(), any()) }

        // Act
        recentCategoriesRepository.addMultiChoiceCategory(categoryIdToAdd)

        // Assert
        val slot = slot<PreferenceRequest<Set<String>>>()
        coVerify { recentCategoriesDataStoreManager.getPreference(capture(slot)) }
        assertThat(slot.captured.key).isEqualTo(RecentCategoryDataStoreCommon.MultiChoice.key)

        val addFunctionShouldBeCalled = categoryIdToAdd !in initialCategories

        val newValueSlot = slot<Set<String>>()
        coVerify(
            exactly = if (addFunctionShouldBeCalled) 1 else 0
        ) { recentCategoriesDataStoreManager.editPreference(any(), capture(newValueSlot)) }

        if (addFunctionShouldBeCalled) {
            assertThat(newValueSlot.captured.size).isAtMost(3)
            assertThat(newValueSlot.captured).contains(categoryIdToAdd)
        } else {
            assertThat(newValueSlot.isCaptured).isFalse()
        }
    }

    // Tests for addWordleCategory
    @ParameterizedTest
    @MethodSource("addWordleCategoryParams")
    fun `addWordleCategory should add category to recent categories`(
        categoryIdToAdd: String,
        initialCategories: Set<String>
    ) = runTest {
        // Mock the necessary dependencies and data
        coEvery { recentCategoriesDataStoreManager.getPreference<Set<String>>(any()) } returns initialCategories
        coJustRun { recentCategoriesDataStoreManager.editPreference<Set<String>>(any(), any()) }

        // Act
        recentCategoriesRepository.addWordleCategory(categoryIdToAdd)

        // Assert
        val slot = slot<PreferenceRequest<Set<String>>>()
        coVerify { recentCategoriesDataStoreManager.getPreference(capture(slot)) }
        assertThat(slot.captured.key).isEqualTo(RecentCategoryDataStoreCommon.Wordle.key)

        val addFunctionShouldBeCalled = categoryIdToAdd !in initialCategories

        val newValueSlot = slot<Set<String>>()
        coVerify(
            exactly = if (addFunctionShouldBeCalled) 1 else 0
        ) { recentCategoriesDataStoreManager.editPreference(any(), capture(newValueSlot)) }

        if (addFunctionShouldBeCalled) {
            assertThat(newValueSlot.captured.size).isAtMost(3)
            assertThat(newValueSlot.captured).contains(categoryIdToAdd)
        } else {
            assertThat(newValueSlot.isCaptured).isFalse()
        }
    }

    // Tests for addComparisonCategory
    @ParameterizedTest
    @MethodSource("addComparisonCategoryParams")
    fun `addComparisonCategory should add category to recent categories`(
        categoryIdToAdd: String,
        initialCategories: Set<String>
    ) = runTest {
        // Mock the necessary dependencies and data
        coEvery { recentCategoriesDataStoreManager.getPreference<Set<String>>(any()) } returns initialCategories
        coJustRun { recentCategoriesDataStoreManager.editPreference<Set<String>>(any(), any()) }

        // Act
        recentCategoriesRepository.addComparisonCategory(categoryIdToAdd)

        // Assert
        val slot = slot<PreferenceRequest<Set<String>>>()
        coVerify { recentCategoriesDataStoreManager.getPreference(capture(slot)) }
        assertThat(slot.captured.key).isEqualTo(RecentCategoryDataStoreCommon.ComparisonQuiz.key)

        val addFunctionShouldBeCalled = categoryIdToAdd !in initialCategories

        val newValueSlot = slot<Set<String>>()
        coVerify(
            exactly = if (addFunctionShouldBeCalled) 1 else 0
        ) { recentCategoriesDataStoreManager.editPreference(any(), capture(newValueSlot)) }

        if (addFunctionShouldBeCalled) {
            assertThat(newValueSlot.captured.size).isAtMost(3)
            assertThat(newValueSlot.captured).contains(categoryIdToAdd)
        } else {
            assertThat(newValueSlot.isCaptured).isFalse()
        }
    }

    // Tests for cleanAllSavedCategories
    @Test
    fun `cleanAllSavedCategories should clean all saved categories`() = runTest {
        // Mock the necessary dependencies and data
        coJustRun { recentCategoriesDataStoreManager.editPreference<Set<String>>(any(), any()) }

        // Act
        recentCategoriesRepository.cleanAllSavedCategories()

        coVerify(exactly = 1) {
            recentCategoriesDataStoreManager.editPreference(
                key = RecentCategoryDataStoreCommon.MultiChoice.key,
                newValue = emptySet()
            )
            recentCategoriesDataStoreManager.editPreference(
                key = RecentCategoryDataStoreCommon.Wordle.key,
                newValue = emptySet()
            )
            recentCategoriesDataStoreManager.editPreference(
                key = RecentCategoryDataStoreCommon.ComparisonQuiz.key,
                newValue = emptySet()
            )
        }
    }

    companion object {
        // Params for getCategories tests
        @JvmStatic
        private fun getCategoriesParams(
            allCategories: List<BaseCategory>
        ) = listOf(
            Arguments.of(
                true,
                setOf(
                    allCategories[0].id,
                    allCategories[1].id,
                    allCategories[2].id
                )
            ),
            Arguments.of(
                false,
                setOf(
                    allCategories[0].id,
                    allCategories[1].id,
                    allCategories[2].id
                )
            ),
            Arguments.of(
                true,
                emptySet<String>()
            ),
            Arguments.of(
                false,
                emptySet<String>()
            ),
            Arguments.of(
                true,
                setOf(
                    allCategories.first().id
                )
            )
        )

        @JvmStatic
        fun getMultiChoiceCategoriesParams() = getCategoriesParams(multiChoiceQuestionCategories)

        @JvmStatic
        fun getWordleCategoriesParams() = getCategoriesParams(WordleCategories.allCategories)

        @JvmStatic
        fun getComparisonCategoriesParams() = getCategoriesParams(allComparisonQuizCategories)

        // Params for addCategory tests
        @JvmStatic
        private fun addCategoryParams(
            allCategories: List<BaseCategory>
        ) = listOf(
            Arguments.of(
                allCategories[0].id,
                emptySet<BaseCategory>()
            ),
            Arguments.of(
                allCategories[0].id,
                setOf(
                    allCategories[1].id
                )
            ),
            Arguments.of(
                allCategories[0].id,
                setOf(
                    allCategories[1].id,
                    allCategories[2].id
                )
            ),
            Arguments.of(
                allCategories[0].id,
                setOf(
                    allCategories[1].id,
                    allCategories[2].id,
                    allCategories[3].id
                )
            ),
            // Test that adding a category that already exists in the set will not change the set
            Arguments.of(
                allCategories[0].id,
                setOf(
                    allCategories[0].id,
                )
            ),
            // Test that adding a category that already exists in the set will not change the set
            Arguments.of(
                allCategories[1].id,
                setOf(
                    allCategories[0].id,
                    allCategories[1].id
                )
            ),
            // Test that adding a category that already exists in the set will not change the set
            Arguments.of(
                allCategories[1].id,
                setOf(
                    allCategories[0].id,
                    allCategories[1].id,
                    allCategories[2].id
                )
            ),
        )

        @JvmStatic
        fun addMultiChoiceCategoryParams() = addCategoryParams(multiChoiceQuestionCategories)

        @JvmStatic
        fun addWordleCategoryParams() = addCategoryParams(WordleCategories.allCategories)

        @JvmStatic
        fun addComparisonCategoryParams() = addCategoryParams(allComparisonQuizCategories)

        private val allComparisonQuizCategories = listOf(
            ComparisonQuizCategory(
                id = "numbers",
                name = "Numbers".toUiText(),
                description = "Numbers description",
                image = "",
                questionDescription = ComparisonQuizCategory.QuestionDescription(
                    greater = "Which number is greater?",
                    less = "Which number is lesser?",
                ),
                formatType = ComparisonQuizFormatType.Number,
                dataSourceAttribution = ComparisonQuizCategory.DataSourceAttribution(
                    text = "NewQuiz API",
                    logo = ""
                ),
                requireInternetConnection = false
            ),
            ComparisonQuizCategory(
                id = "countries",
                name = "Countries".toUiText(),
                description = "Countries description",
                image = "",
                questionDescription = ComparisonQuizCategory.QuestionDescription(
                    greater = "Which country is bigger?",
                    less = "Which country is smaller?",
                ),
                formatType = ComparisonQuizFormatType.Number,
                dataSourceAttribution = ComparisonQuizCategory.DataSourceAttribution(
                    text = "NewQuiz API",
                    logo = ""
                ),
                requireInternetConnection = false
            ),
            ComparisonQuizCategory(
                id = "cities",
                name = "Cities".toUiText(),
                description = "Cities description",
                image = "",
                questionDescription = ComparisonQuizCategory.QuestionDescription(
                    greater = "Which city is bigger?",
                    less = "Which city is smaller?",
                ),
                formatType = ComparisonQuizFormatType.Number,
                dataSourceAttribution = ComparisonQuizCategory.DataSourceAttribution(
                    text = "NewQuiz API",
                    logo = ""
                ),
                requireInternetConnection = true
            ),
            ComparisonQuizCategory(
                id = "planets",
                name = "Planets".toUiText(),
                description = "Planets description",
                image = "",
                questionDescription = ComparisonQuizCategory.QuestionDescription(
                    greater = "Which planet is bigger?",
                    less = "Which planet is smaller?",
                ),
                formatType = ComparisonQuizFormatType.Number,
                dataSourceAttribution = ComparisonQuizCategory.DataSourceAttribution(
                    text = "NewQuiz API",
                    logo = ""
                ),
                requireInternetConnection = true
            ),
        )
    }
}
