package com.infinitepower.newquiz.multi_choice_quiz.categories

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.multi_choice_quiz.categories.components.CategoryComponent
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun MultiChoiceCategoriesScreen(
    navigator: DestinationsNavigator
) {
    MultiChoiceCategoriesScreenImpl(
        onBackClick = navigator::popBackStack,
        navigateToQuizScreen = { categoryId ->
            navigator.navigate(MultiChoiceQuizScreenDestination(category = categoryId))
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MultiChoiceCategoriesScreenImpl(
    onBackClick: () -> Unit,
    navigateToQuizScreen: (category: Int) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val spaceMedium = MaterialTheme.spacing.medium

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.categories))
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = { BackIconButton(onClick = onBackClick) }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier.padding(innerPadding),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(spaceMedium),
            verticalArrangement = Arrangement.spacedBy(spaceMedium),
            contentPadding = PaddingValues(horizontal = spaceMedium)
        ) {
            items(
                items = multiChoiceQuestionCategories,
                key = { it.id }
            ) { category ->
                CategoryComponent(
                    category = category,
                    onClick = { navigateToQuizScreen(category.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}