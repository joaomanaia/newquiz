package com.infinitepower.newquiz.wordle.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.category.CategoryComponent
import com.infinitepower.newquiz.core.ui.components.rememberIsInternetAvailable
import com.infinitepower.newquiz.core.ui.home_card.components.HomeGroupTitle
import com.infinitepower.newquiz.core.ui.home_card.components.HomeLargeCard
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.data.local.wordle.WordleCategories
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun WordleListScreen(
    navigator: DestinationsNavigator
) {
    WordleListScreenImpl(
        navigateToWordleQuiz = { wordleQuizType ->
            navigator.navigate(WordleScreenDestination(quizType = wordleQuizType))
        }
    )
}

@Composable
@ExperimentalMaterial3Api
private fun WordleListScreenImpl(
    navigateToWordleQuiz: (wordleQuizType: WordleQuizType) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val isInternetAvailable = rememberIsInternetAvailable()

    val categories = remember { WordleCategories.allCategories }

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
                        val randomCategory = WordleCategories.random(categories, isInternetAvailable)
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
    }
}

@Composable
@AllPreviewsNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun WordleListScreenPreview() {
    NewQuizTheme {
        Surface {
            WordleListScreenImpl(
                navigateToWordleQuiz = {}
            )
        }
    }
}