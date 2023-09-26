package com.infinitepower.newquiz.maze_quiz.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertValueEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.util.asString
import com.infinitepower.newquiz.core_test.utils.setTestContent
import com.infinitepower.newquiz.data.worker.maze.GenerateMazeQuizWorker
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.wordle.WordleCategory
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalMaterial3Api::class)
internal class GenerateCustomMazeTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    data class ComponentClickData(
        val seed: Int?,
        val selectedMultiChoiceCategories: List<MultiChoiceCategory>,
        val selectedWordleCategories: List<WordleCategory>
    )

    @Test
    fun test_generateWithNoChanges() {
        var onClicked = false
        var componentClickData: ComponentClickData? = null

        composeRule.setTestContent {
            GenerateCustomMaze(
                onClick = { seed, selectedMultiChoiceCategories, selectedWordleCategories ->
                    onClicked = true
                    componentClickData = ComponentClickData(
                        seed,
                        selectedMultiChoiceCategories,
                        selectedWordleCategories
                    )
                },
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .testTag("GenerateCustomMaze")
            )
        }

        composeRule
            .onNodeWithText("Generate")
            .assertDoesNotExist()

        // Expand the GenerateCustomMaze component
        composeRule
            .onNodeWithContentDescription("Expand custom options")
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
            .assertIsEnabled()
            .performClick()

        // Scroll to the bottom of the component
        composeRule
            .onNodeWithTag("GenerateCustomMaze")
            .performScrollToNode(hasText("Generate"))

        composeRule
            .onNodeWithText("Generate")
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
            .assertIsEnabled()
            .performClick()

        assertThat(onClicked).isTrue()

        assertThat(componentClickData).isNotNull()
        assertThat(componentClickData?.seed).isNotNull()

        assertThat(componentClickData?.selectedMultiChoiceCategories).isNotNull()
        assertThat(componentClickData?.selectedMultiChoiceCategories).hasSize(5)

        assertThat(componentClickData?.selectedWordleCategories).isNotNull()
        assertThat(componentClickData?.selectedWordleCategories).hasSize(3)
    }

    @Test
    fun test_noCategoriesSelected_generateButtonShouldBeDisabled() {
        var onClicked = false
        var componentClickData: ComponentClickData? = null

        composeRule.setTestContent {
            GenerateCustomMaze(
                onClick = { seed, selectedMultiChoiceCategories, selectedWordleCategories ->
                    onClicked = true
                    componentClickData = ComponentClickData(
                        seed,
                        selectedMultiChoiceCategories,
                        selectedWordleCategories
                    )
                },
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .testTag("GenerateCustomMaze")
            )
        }

        composeRule
            .onNodeWithText("Generate")
            .assertDoesNotExist()

        // Expand the GenerateCustomMaze component
        composeRule
            .onNodeWithContentDescription("Expand custom options")
            .assertIsDisplayed()
            .performClick()

        val context = composeRule.activity.applicationContext

        // Disable all multi-choice categories
        val multiChoiceGameMode = GenerateMazeQuizWorker.GameModes.MultiChoice
        val multiChoiceGameModeName = multiChoiceGameMode.name.asString(context)

        composeRule
            .onNodeWithText(multiChoiceGameModeName)
            .assertIsDisplayed()
            .performClick()

        composeRule
            .onNodeWithContentDescription("$multiChoiceGameModeName game mode checkbox")
            .assertValueEquals("No categories selected")

        // Disable all wordle categories
        val wordleGameMode = GenerateMazeQuizWorker.GameModes.Wordle
        val wordleGameModeName = wordleGameMode.name.asString(context)

        composeRule
            .onNodeWithText(wordleGameModeName)
            .assertIsDisplayed()
            .performClick()

        composeRule
            .onNodeWithContentDescription("$wordleGameModeName game mode checkbox")
            .assertValueEquals("No categories selected")

        // Scroll to the bottom of the component
        composeRule
            .onNodeWithTag("GenerateCustomMaze")
            .performScrollToNode(hasText("Generate"))

        composeRule
            .onNodeWithText("Generate")
            .assertIsDisplayed()
            .assertIsNotEnabled()
            .performClick()

        assertThat(onClicked).isFalse()
        assertThat(componentClickData).isNull()

        // Enable a random category to see if the generate button is enabled
        val randomCategory = (multiChoiceGameMode.categories + wordleGameMode.categories).random()
        val randomCategoryName = randomCategory.name.asString(context)

        // Scroll to the category
        composeRule
            .onNodeWithTag("GenerateCustomMaze")
            .performScrollToNode(hasText(randomCategoryName))

        composeRule
            .onNodeWithText(randomCategoryName)
            .assertIsDisplayed()
            .performClick()

        // Scroll to the bottom of the component
        composeRule
            .onNodeWithTag("GenerateCustomMaze")
            .performScrollToNode(hasText("Generate"))

        composeRule
            .onNodeWithText("Generate")
            .assertIsDisplayed()
            .assertIsEnabled()
            .performClick()
    }
}
