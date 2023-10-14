package com.infinitepower.newquiz.maze_quiz.components

import androidx.activity.ComponentActivity
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.assertValueEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.util.asString
import com.infinitepower.newquiz.core.testing.utils.setTestContent
import com.infinitepower.newquiz.data.worker.maze.GenerateMazeQuizWorker
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@SmallTest
@RunWith(AndroidJUnit4::class)
internal class GameModeComponentTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun test_itemsVisibility() {
        val gameMode = GenerateMazeQuizWorker.GameModes.MultiChoice

        composeRule.setTestContent {
            GameModeComponent(
                gameMode = gameMode,
                selectedCategories = gameMode.categories, // All categories are selected by default
                onSelectChange = {},
                onParentSelectChange = {},
            )
        }

        val context = composeRule.activity.applicationContext
        val gameModeName = gameMode.name.asString(context)

        // Check if the game mode checkbox parent is displayed
        composeRule
            .onNodeWithContentDescription("$gameModeName game mode")
            .assertExists()
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertIsToggleable()

        composeRule
            .onNodeWithText(gameModeName)
            .assertExists()
            .assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription("$gameModeName game mode checkbox")
            .assertExists()
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertIsToggleable()
            .assertValueEquals("All categories selected")

        // Test child categories
        gameMode.categories.forEach { category ->
            val categoryName = category.name.asString(context)

            composeRule
                .onNodeWithText(categoryName)
                .assertExists()
                .assertIsDisplayed()

            composeRule
                .onNodeWithContentDescription("$categoryName category checkbox")
                .assertExists()
                .assertIsDisplayed()
                .assertIsEnabled()
                .assertIsToggleable()
                .assertValueEquals("Selected")
        }
    }

    @Test
    fun test_changeParentCheck() {
        val gameMode = GenerateMazeQuizWorker.GameModes.MultiChoice

        val selectedMultiChoiceCategories = gameMode.categories.toMutableStateList()

        composeRule.setTestContent {
            GameModeComponent(
                gameMode = gameMode,
                selectedCategories = selectedMultiChoiceCategories, // All categories are selected by default
                onSelectChange = { category ->
                    if (category in selectedMultiChoiceCategories) {
                        selectedMultiChoiceCategories.remove(category)
                    } else {
                        selectedMultiChoiceCategories.add(category)
                    }
                },
                onParentSelectChange = { enableAll ->
                    selectedMultiChoiceCategories.clear()

                    if (enableAll) {
                        selectedMultiChoiceCategories.addAll(gameMode.categories)
                    }
                },
            )
        }

        val context = composeRule.activity.applicationContext
        val gameModeName = gameMode.name.asString(context)

        assertThat(selectedMultiChoiceCategories).containsExactlyElementsIn(gameMode.categories)

        // Test parent click to disable all categories
        composeRule
            .onNodeWithContentDescription("$gameModeName game mode checkbox")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertValueEquals("All categories selected")
            .performClick()

        assertThat(selectedMultiChoiceCategories).isEmpty()

        // Check if all child checkboxes are unchecked
        gameMode.categories.forEach { category ->
            val categoryName = category.name.asString(context)

            composeRule
                .onNodeWithContentDescription("$categoryName category checkbox")
                .assertValueEquals("Not selected")
        }

        // Test parent click to enable all categories
        composeRule
            .onNodeWithContentDescription("$gameModeName game mode checkbox")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertValueEquals("No categories selected")
            .performClick()

        assertThat(selectedMultiChoiceCategories).containsExactlyElementsIn(gameMode.categories)

        // Check if all child checkboxes are checked
        gameMode.categories.forEach { category ->
            val categoryName = category.name.asString(context)

            composeRule
                .onNodeWithContentDescription("$categoryName category checkbox")
                .assertValueEquals("Selected")
        }
    }

    @Test
    fun test_changeChildCheck() {
        val gameMode = GenerateMazeQuizWorker.GameModes.MultiChoice

        val selectedMultiChoiceCategories = gameMode.categories.toMutableStateList()

        composeRule.setTestContent {
            GameModeComponent(
                gameMode = gameMode,
                selectedCategories = selectedMultiChoiceCategories, // All categories are selected by default
                onSelectChange = { category ->
                    if (category in selectedMultiChoiceCategories) {
                        selectedMultiChoiceCategories.remove(category)
                    } else {
                        selectedMultiChoiceCategories.add(category)
                    }
                },
                onParentSelectChange = {},
            )
        }

        val context = composeRule.activity.applicationContext
        val gameModeName = gameMode.name.asString(context)

        // Check for parent checkbox
        composeRule
            .onNodeWithContentDescription("$gameModeName game mode checkbox")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertValueEquals("All categories selected")

        // Click on a child checkbox
        val randomCategory = gameMode.categories.random()

        composeRule
            .onNodeWithText(randomCategory.name.asString(context))
            .assertIsDisplayed()
            .assertIsEnabled()
            .performClick()

        assertThat(selectedMultiChoiceCategories).doesNotContain(randomCategory)

        composeRule
            .onNodeWithContentDescription("$gameModeName game mode checkbox")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertValueEquals("Some categories selected")

        // Click on the same child checkbox
        composeRule
            .onNodeWithText(randomCategory.name.asString(context))
            .assertIsDisplayed()
            .assertIsEnabled()
            .performClick()

        assertThat(selectedMultiChoiceCategories).contains(randomCategory)

        composeRule
            .onNodeWithContentDescription("$gameModeName game mode checkbox")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertValueEquals("All categories selected")
    }
}
