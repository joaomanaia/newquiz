package com.infinitepower.newquiz.wordle.list

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class WordleListScreenTest {
    @Test
    fun `test getRandomWordleCategory when not internet connection`() {
        val allCategories = getWordleCategories()
        val isInternetAvailable = false

        val categoriesWithoutInternet = allCategories.filter { !it.requireInternetConnection }

        val randomCategory = getRandomWordleCategory(allCategories, isInternetAvailable)

        assertThat(randomCategory).isIn(categoriesWithoutInternet)
        assertThat(randomCategory.requireInternetConnection).isEqualTo(isInternetAvailable)
    }

    @Test
    fun `test getRandomWordleCategory when internet connection`() {
        val allCategories = getWordleCategories()
        val isInternetAvailable = true

        val randomCategory = getRandomWordleCategory(allCategories, isInternetAvailable)

        assertThat(randomCategory).isIn(allCategories)

        // If internet is available, it can return any category
        assertThat(randomCategory.requireInternetConnection).isAnyOf(true, false)
    }
}
