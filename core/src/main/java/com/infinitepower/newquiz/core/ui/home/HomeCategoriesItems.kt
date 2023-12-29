package com.infinitepower.newquiz.core.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.ui.components.category.CategoryComponent
import com.infinitepower.newquiz.core.util.asString
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo

fun <T : BaseCategory> LazyListScope.homeCategoriesItems(
    contentPadding: PaddingValues = PaddingValues(),
    seeAllCategories: Boolean,
    recentCategories: List<T>,
    otherCategories: List<T>,
    isInternetAvailable: Boolean,
    showConnectionInfo: ShowCategoryConnectionInfo,
    onCategoryClick: (T) -> Unit,
    onSeeAllCategoriesClick: () -> Unit,
) {
    if (recentCategories.isEmpty() && otherCategories.isEmpty()) {
        item {
            Box(
                modifier = Modifier.fillParentMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.no_categories_available),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    items(
        items = recentCategories,
        key = { category -> "recent_category_${category.id}" }
    ) { category ->
        CategoryComponent(
            modifier = Modifier
                .fillParentMaxWidth()
                .height(120.dp)
                .padding(contentPadding),
            title = category.name.asString(),
            imageUrl = category.image,
            onClick = { onCategoryClick(category) },
            enabled = isInternetAvailable || !category.requireInternetConnection,
            requireInternetConnection = category.requireInternetConnection,
            showConnectionInfo = showConnectionInfo
        )
    }

    if (recentCategories.isNotEmpty() && otherCategories.isNotEmpty()) {
        item(
            key = "see_all_categories_button",
        ) {
            ExpandCategoriesButton(
                modifier = Modifier.fillParentMaxWidth(),
                seeAllCategories = seeAllCategories,
                onSeeAllCategoriesClick = onSeeAllCategoriesClick
            )
        }
    }

    if (seeAllCategories) {
        items(
            items = otherCategories,
            key = { category -> category.id }
        ) { category ->
            CategoryComponent(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(120.dp)
                    .padding(contentPadding),
                title = category.name.asString(),
                imageUrl = category.image,
                onClick = { onCategoryClick(category) },
                enabled = isInternetAvailable || !category.requireInternetConnection,
                requireInternetConnection = category.requireInternetConnection,
                showConnectionInfo = showConnectionInfo
            )
        }
    }
}
