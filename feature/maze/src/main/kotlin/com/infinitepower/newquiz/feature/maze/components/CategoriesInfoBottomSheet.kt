package com.infinitepower.newquiz.feature.maze.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.category.CategoryComponent
import com.infinitepower.newquiz.core.util.asString
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.data.local.wordle.WordleCategories
import com.infinitepower.newquiz.feature.maze.CategoriesByGameMode
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.GameMode
import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
internal fun CategoriesInfoBottomSheet(
    onDismissRequest: () -> Unit,
    categoriesByGameMode: CategoriesByGameMode
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        CategoriesContent(
            multiChoiceCategories = categoriesByGameMode[GameMode.MULTI_CHOICE].orEmpty().toImmutableList(),
            wordleCategories = categoriesByGameMode[GameMode.WORDLE].orEmpty().toImmutableList(),
            comparisonQuizCategories = categoriesByGameMode[GameMode.COMPARISON_QUIZ].orEmpty().toImmutableList()
        )
    }
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
private fun CategoriesContent(
    modifier: Modifier = Modifier,
    multiChoiceCategories: ImmutableList<BaseCategory>,
    wordleCategories: ImmutableList<BaseCategory>,
    comparisonQuizCategories: ImmutableList<BaseCategory>,
) {
    val multiChoiceHeader = stringResource(id = R.string.multi_choice_quiz)
    val wordleHeader = stringResource(id = R.string.wordle)
    val comparisonQuizHeader = stringResource(id = R.string.comparison_quiz)

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        categoriesItemsWithHeader(
            title = multiChoiceHeader,
            categories = multiChoiceCategories
        )
        categoriesItemsWithHeader(
            title = wordleHeader,
            categories = wordleCategories
        )
        categoriesItemsWithHeader(
            title = comparisonQuizHeader,
            categories = comparisonQuizCategories
        )
    }
}

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
private fun LazyListScope.categoriesItemsWithHeader(
    title: String,
    categories: ImmutableList<BaseCategory>
) {
    if (categories.isNotEmpty()) {
        categoriesStickyHeader(title = title)
        categoriesItems(categories = categories)
    }
}

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
private fun LazyListScope.categoriesStickyHeader(
    title: String,
) {
    stickyHeader {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillParentMaxWidth()
                .background(color = BottomSheetDefaults.ContainerColor)
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.small
                )
        )
    }
}

private fun <T : BaseCategory> LazyListScope.categoriesItems(
    categories: ImmutableList<T>,
) {
    items(items = categories) { category ->
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
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
private fun MazeCategoriesContentPreview() {
    NewQuizTheme {
        Surface {
            CategoriesContent(
                multiChoiceCategories = multiChoiceQuestionCategories.take(2).toImmutableList(),
                wordleCategories = WordleCategories.allCategories.take(1).toImmutableList(),
                comparisonQuizCategories = persistentListOf()
            )
        }
    }
}
