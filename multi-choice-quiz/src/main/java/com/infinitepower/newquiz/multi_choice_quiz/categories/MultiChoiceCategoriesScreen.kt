package com.infinitepower.newquiz.multi_choice_quiz.categories

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.analytics.logging.multi_choice_quiz.rememberMultiChoiceLoggingAnalytics
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionCategory
import com.infinitepower.newquiz.multi_choice_quiz.categories.components.CategoryComponent
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun MultiChoiceCategoriesScreen(
    navigator: DestinationsNavigator,
    viewModel: MultiChoiceCategoriesScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val multiChoiceLoggingAnalytics = rememberMultiChoiceLoggingAnalytics()

    MultiChoiceCategoriesScreenImpl(
        uiState = uiState,
        onBackClick = navigator::popBackStack,
        navigateToQuizScreen = { categoryId ->
            multiChoiceLoggingAnalytics.logCategoryClicked(categoryId)
            navigator.navigate(MultiChoiceQuizScreenDestination(category = categoryId))
        }
    )

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("CategoriesScreen")
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MultiChoiceCategoriesScreenImpl(
    uiState: MultiChoiceCategoriesUiState,
    onBackClick: () -> Unit,
    navigateToQuizScreen: (category: Int) -> Unit
) {
    SearchBarContainer(
        onBackClick = onBackClick,
        categories = uiState.categories
    ) { category ->
        CategoryComponent(
            category = category,
            onClick = { navigateToQuizScreen(category.id) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun SearchBarContainer(
    onBackClick: () -> Unit,
    categories: List<MultiChoiceQuestionCategory>,
    categoryItem: @Composable LazyGridItemScope.(category: MultiChoiceQuestionCategory) -> Unit
) {
    val context = LocalContext.current

    var searchText by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    val categoriesNames = remember(categories) {
        categories.map { it.name.asString(context) }
    }

    val filteredCategoriesNames by remember(categoriesNames, searchText) {
        derivedStateOf {
            if (searchText.isBlank()) return@derivedStateOf emptyList()

            categoriesNames.filter { categoryName ->
                categoryName.lowercase().contains(searchText.lowercase())
            }
        }
    }

    val filteredCategories = remember(filteredCategoriesNames) {
        if (filteredCategoriesNames.isEmpty()) return@remember categories

        filteredCategoriesNames.map { categoryName ->
            val categoryIndex = categoriesNames.indexOf(categoryName)
            categories[categoryIndex]
        }
    }

    val focusManager = LocalFocusManager.current

    fun closeSearchBar() {
        focusManager.clearFocus()
        active = false
    }

    val spaceMedium = MaterialTheme.spacing.medium

    Box(Modifier.fillMaxSize()) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            query = searchText,
            onQueryChange = { searchText = it },
            onSearch = { closeSearchBar() },
            active = active,
            onActiveChange = {
                active = it
                if (!active) focusManager.clearFocus()
            },
            placeholder = { Text("Search categories") },
            leadingIcon = {
                BackIconButton(onClick = onBackClick)
            }
        ) {
            LazyColumn {
                items(
                    items = filteredCategoriesNames
                ) { categoryName ->
                    Surface(
                        onClick = {
                            searchText = categoryName
                            closeSearchBar()
                        },
                        modifier = Modifier.fillParentMaxWidth()
                    ) {
                        Text(
                            text = categoryName,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(spaceMedium)
                        )
                    }
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            horizontalArrangement = Arrangement.spacedBy(spaceMedium),
            verticalArrangement = Arrangement.spacedBy(spaceMedium),
            contentPadding = PaddingValues(
                start = spaceMedium,
                top = 72.dp,
                end = spaceMedium,
                bottom = spaceMedium
            ),
        ) {
            items(
                items = filteredCategories,
                key = { it.id },
                itemContent = categoryItem
            )
        }
    }
}

@Composable
@AllPreviewsNightLight
fun MultiChoiceCategoriesPreview() {
    val categories = List(10) {
        MultiChoiceQuestionCategory(it, UiText.DynamicString("Category $it"))
    }

    NewQuizTheme {
        Surface {
            MultiChoiceCategoriesScreenImpl(
                uiState = MultiChoiceCategoriesUiState(
                    categories = categories
                ),
                onBackClick = {},
                navigateToQuizScreen = {}
            )
        }
    }
}