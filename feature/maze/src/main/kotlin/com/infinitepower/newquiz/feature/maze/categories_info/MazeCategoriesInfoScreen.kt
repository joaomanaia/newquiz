package com.infinitepower.newquiz.feature.maze.categories_info

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.category.CategoryComponent
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.core.util.asString
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.data.local.wordle.WordleCategories
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
@Destination
fun MazeCategoriesInfoScreen(
    navigator: DestinationsNavigator,
    viewModel: MazeCategoriesInfoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MazeCategoriesInfoScreenImpl(
        onBackClick = navigator::popBackStack,
        uiState = uiState
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
private fun MazeCategoriesInfoScreenImpl(
    uiState: MazeCategoriesInfoUiState,
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Categories Info") },
                navigationIcon = { BackIconButton(onClick = onBackClick) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (uiState.loading) {
                CircularProgressIndicator()
            } else {
                CategoriesContent(uiState = uiState)
            }
        }
    }
}

@Composable
@ExperimentalFoundationApi
private fun CategoriesContent(
    uiState: MazeCategoriesInfoUiState,
) {
    val multiChoiceHeader = stringResource(id = R.string.multi_choice_quiz)
    val wordleHeader = stringResource(id = R.string.wordle)
    val comparisonQuizHeader = stringResource(id = R.string.comparison_quiz)

    LazyColumn(
        contentPadding = PaddingValues(bottom = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        categoriesItemsWithHeader(
            title = multiChoiceHeader,
            categories = uiState.multiChoiceCategories
        )
        categoriesItemsWithHeader(
            title = wordleHeader,
            categories = uiState.wordleCategories
        )
        categoriesItemsWithHeader(
            title = comparisonQuizHeader,
            categories = uiState.comparisonQuizCategories
        )
    }
}

@ExperimentalFoundationApi
private fun LazyListScope.categoriesItemsWithHeader(
    title: String,
    categories: ImmutableList<BaseCategory>
) {
    if (categories.isNotEmpty()) {
        categoriesStickyHeader(title = title)
        categoriesItems(categories = categories, baseItemKey = title)
    }
}

@ExperimentalFoundationApi
private fun LazyListScope.categoriesStickyHeader(
    title: String
) {
    stickyHeader {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillParentMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.small
                )
        )
    }
}

private fun <T : BaseCategory> LazyListScope.categoriesItems(
    categories: ImmutableList<T>,
    baseItemKey: String = "",
) {
    items(
        items = categories,
        key = { category -> "$baseItemKey-${category.id}" }
    ) { category ->
        CategoryComponent(
            title = category.name.asString(),
            imageUrl = category.image,
            requireInternetConnection = category.requireInternetConnection,
            showConnectionInfo = ShowCategoryConnectionInfo.BOTH,
            modifier = Modifier
                .fillParentMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium),
            clickEnabled = false
        )
    }
}

@Composable
@PreviewLightDark
private fun MazeCategoriesInfoScreenPreview() {
    NewQuizTheme {
        MazeCategoriesInfoScreenImpl(
            uiState = MazeCategoriesInfoUiState(
                loading = false,
                multiChoiceCategories = multiChoiceQuestionCategories.take(2).toImmutableList(),
                wordleCategories = WordleCategories.allCategories.take(1).toImmutableList(),
            )
        )
    }
}