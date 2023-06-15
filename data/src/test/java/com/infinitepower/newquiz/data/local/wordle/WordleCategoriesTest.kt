package com.infinitepower.newquiz.data.local.wordle

import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

internal class WordleCategoriesTest {
    @Test
    fun `test getRandomWordleCategory when not internet connection`() {
        val allCategories = WordleCategories.allCategories
        val isInternetAvailable = false

        val categoriesWithoutInternet = allCategories.filter { !it.requireInternetConnection }

        val randomCategory = WordleCategories.random(isInternetAvailable)

        Truth.assertThat(randomCategory).isIn(categoriesWithoutInternet)
        Truth.assertThat(randomCategory.requireInternetConnection).isEqualTo(isInternetAvailable)
    }

    @Test
    fun `test getRandomWordleCategory when internet connection`() {
        val allCategories = WordleCategories.allCategories
        val isInternetAvailable = true

        val randomCategory = WordleCategories.random(isInternetAvailable)

        Truth.assertThat(randomCategory).isIn(allCategories)

        // If internet is available, it can return any category
        Truth.assertThat(randomCategory.requireInternetConnection).isAnyOf(true, false)
    }
}