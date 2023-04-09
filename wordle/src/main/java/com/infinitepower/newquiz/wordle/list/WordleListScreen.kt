package com.infinitepower.newquiz.wordle.list

import androidx.annotation.Keep
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.category.CategoryComponent
import com.infinitepower.newquiz.core.ui.components.rememberIsInternetAvailable
import com.infinitepower.newquiz.core.ui.home_card.components.HomeGroupTitle
import com.infinitepower.newquiz.core.ui.home_card.components.HomeLargeCard
import com.infinitepower.newquiz.core.ui.home_card.components.HomeMediumCard
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.wordle.destinations.DailyWordleCalendarScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.random.Random
import com.infinitepower.newquiz.core.R as CoreR

@Keep
data class WordleCategory(
    val id: Int,
    val name: UiText,
    val image: Any,
    val wordleQuizType: WordleQuizType,
    val requireInternetConnection: Boolean = false
)

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun WordleListScreen(
    navigator: DestinationsNavigator
) {
    WordleListScreenImpl(
        navigateToWordleQuiz = { wordleQuizType ->
            navigator.navigate(WordleScreenDestination(quizType = wordleQuizType))
        },
        navigateToWordleCalendar = { navigator.navigate(DailyWordleCalendarScreenDestination) }
    )

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("WordleListScreen")
    }
}

@Composable
@ExperimentalMaterial3Api
private fun WordleListScreenImpl(
    navigateToWordleQuiz: (wordleQuizType: WordleQuizType) -> Unit,
    navigateToWordleCalendar: () -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val isInternetAvailable = rememberIsInternetAvailable()

    val categories = remember { getWordleCategories() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(spaceMedium),
        contentPadding = PaddingValues(
            start = spaceMedium,
            end = spaceMedium,
            bottom = MaterialTheme.spacing.large,
        )
    ) {
        item {
            HomeGroupTitle(title = stringResource(id = CoreR.string.wordle_infinite))
        }

        item {
            HomeLargeCard(
                modifier = Modifier.fillParentMaxWidth(),
                data = HomeCardItem.LargeCard(
                    title = CoreR.string.quiz_with_random_categories,
                    icon = CardIcon.Icon(Icons.Rounded.QuestionMark),
                    backgroundPrimary = true,
                    onClick = {
                        val randomCategory = getRandomWordleCategory(categories, isInternetAvailable)
                        navigateToWordleQuiz(randomCategory.wordleQuizType)
                    }
                )
            )
        }

        item {
            Text(
                text = stringResource(id = CoreR.string.categories),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        items(
            items = categories,
            key = { category -> category.id }
        ) { category ->
            CategoryComponent(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(120.dp),
                title = category.name.asString(),
                imageUrl = category.image,
                onClick = { navigateToWordleQuiz(category.wordleQuizType) },
                enabled = isInternetAvailable || !category.requireInternetConnection
            )
        }

        item {
            HomeGroupTitle(title = stringResource(id = CoreR.string.wordle_daily))
        }

        item {
            HomeMediumCard(
                data = HomeCardItem.MediumCard(
                    title = CoreR.string.wordle_daily,
                    description = "See the daily wordle calendar",
                    icon = CardIcon.Icon(Icons.Rounded.Today),
                    onClick = navigateToWordleCalendar
                )
            )
        }
    }
}

/**
 * Returns a random [WordleCategory] from the list of [allCategories].
 * If [isInternetAvailable] is false, it will only return categories that don't require internet connection.
 *
 * @param allCategories The list of all categories to get the random category.
 * @param isInternetAvailable Whether the internet is available or not.
 * @param random The random instance to use.
 * @see [WordleCategory]
 */
fun getRandomWordleCategory(
    allCategories: List<WordleCategory>,
    isInternetAvailable: Boolean,
    random: Random = Random
): WordleCategory = if (isInternetAvailable) {
    allCategories.random(random)
} else {
    allCategories.filter { !it.requireInternetConnection }.random(random)
}

fun getWordleCategories() = listOf(
    WordleCategory(
        id = 1,
        name = UiText.StringResource(CoreR.string.guess_the_word),
        image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fwordle_illustration.jpg?alt=media&token=69019438-4904-4656-8b1c-18678c537d6b",
        wordleQuizType = WordleQuizType.TEXT
    ),
    WordleCategory(
        id = 2,
        name = UiText.StringResource(CoreR.string.guess_the_number),
        image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fnumbers_12345_illustration.jpg?alt=media&token=f170e7ca-02a3-4dae-87f0-63b0f1205bc5",
        wordleQuizType = WordleQuizType.NUMBER
    ),
    WordleCategory(
        id = 3,
        name = UiText.StringResource(CoreR.string.guess_math_formula),
        image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fnumber_illustration.jpg?alt=media&token=68faf243-2b0e-4a13-aa9c-223743e263fd",
        wordleQuizType = WordleQuizType.MATH_FORMULA
    ),
    WordleCategory(
        id = 4,
        name = UiText.StringResource(CoreR.string.number_trivia),
        image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fnumber_12_in_beach.jpg?alt=media&token=9b888c81-c51c-49ac-a376-0b3bde45db36",
        wordleQuizType = WordleQuizType.NUMBER_TRIVIA,
        requireInternetConnection = true
    )
)

@Composable
@AllPreviewsNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun WordleListScreenPreview() {
    NewQuizTheme {
        Surface {
            WordleListScreenImpl(
                navigateToWordleQuiz = {},
                navigateToWordleCalendar = {}
            )
        }
    }
}