package com.infinitepower.newquiz.multi_choice_quiz.list.util

import com.google.common.collect.Ordering
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import org.junit.jupiter.api.Test

internal class MultiChoiceQuizListUtilsTest {
    @Test
    fun `test getAllCategories when internet is available`() {
        val isInternetAvailable = true
        val categories = MultiChoiceQuizListUtils.getAllCategories(isInternetAvailable)

        assertThat(categories).isNotEmpty()

        // Assert that the categories that require internet connection are also present
        assertThat(categories).containsExactlyElementsIn(multiChoiceQuestionCategories)
    }

    @Test
    fun `test getAllCategories when internet is not available`() {
        val isInternetAvailable = false
        val categories = MultiChoiceQuizListUtils.getAllCategories(isInternetAvailable)

        assertThat(categories).isNotEmpty()

        val categoriesSorted = multiChoiceQuestionCategories.sortedBy { it.requireInternetConnection }

        assertThat(categories).containsExactlyElementsIn(multiChoiceQuestionCategories)
        assertThat(categories).isInOrder(Ordering.explicit(categoriesSorted))
    }

    @Test
    fun `test getRecentCategories when recent categories are not empty`() {
        val recentCategories = multiChoiceQuestionCategories.take(3)
        val allCategories = multiChoiceQuestionCategories
        val isInternetAvailable = true

        val categories = MultiChoiceQuizListUtils.getRecentCategories(
            recentCategories = recentCategories,
            allCategories = allCategories,
            isInternetAvailable = isInternetAvailable
        )

        assertThat(categories).isNotEmpty()

        // When there are recent categories, we return them
        assertThat(categories).containsExactlyElementsIn(recentCategories)
    }

    @Test
    fun `test getRecentCategories when recent categories are empty and internet is available`() {
        val initialRecentCategories = emptyList<MultiChoiceCategory>()
        val allCategories = multiChoiceQuestionCategories
        val isInternetAvailable = true

        val categories = MultiChoiceQuizListUtils.getRecentCategories(
            recentCategories = initialRecentCategories,
            allCategories = allCategories,
            isInternetAvailable = isInternetAvailable
        )

        assertThat(categories).isNotEmpty()
        assertThat(categories).containsAnyIn(allCategories)

        // Assert that the returned list of categories have at most 3 elements
        assertThat(categories.size).isAtMost(3)

        // Assert that the returned list of categories satisfy the filtering condition
        assertThat(categories).containsNoneIn(allCategories.filter { it.requireInternetConnection && !isInternetAvailable })
    }

    @Test
    fun `test getRecentCategories when recent categories are empty and internet is not available`() {
        val initialRecentCategories = emptyList<MultiChoiceCategory>()
        val allCategories = multiChoiceQuestionCategories
        val isInternetAvailable = false

        val categories = MultiChoiceQuizListUtils.getRecentCategories(
            recentCategories = initialRecentCategories,
            allCategories = allCategories,
            isInternetAvailable = isInternetAvailable
        )

        assertThat(categories).isNotEmpty()
        assertThat(categories).isNotEqualTo(allCategories)

        // Assert that the returned list of categories have at most 3 elements
        assertThat(categories.size).isAtMost(3)

        // Assert that the returned list of categories satisfy the filtering condition
        assertThat(categories).containsNoneIn(allCategories.filter { it.requireInternetConnection })
    }
}
