package com.infinitepower.newquiz.core.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.ui.components.category.CategoryComponent
import com.infinitepower.newquiz.core.util.asString
import com.infinitepower.newquiz.model.BaseCategory

fun <T : BaseCategory> LazyListScope.homeCategoriesItems(
    contentPadding: PaddingValues = PaddingValues(),
    seeAllCategories: Boolean,
    recentCategories: List<T>,
    otherCategories: List<T>,
    isInternetAvailable: Boolean,
    onCategoryClick: (T) -> Unit,
    onSeeAllCategoriesClick: () -> Unit,
) {
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
            enabled = isInternetAvailable || !category.requireInternetConnection
        )
    }

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
    } else if (recentCategories.isNotEmpty() && otherCategories.isNotEmpty()) {
        item {
            val seeAllText = if (seeAllCategories) {
                stringResource(id = R.string.see_less_categories)
            } else {
                stringResource(id = R.string.see_all_categories)
            }

            val seeAllIcon = if (seeAllCategories) {
                Icons.Rounded.ExpandLess
            } else {
                Icons.Rounded.ExpandMore
            }

            Box(
                modifier = Modifier.fillParentMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(onClick = onSeeAllCategoriesClick) {
                    Icon(
                        imageVector = seeAllIcon,
                        contentDescription = seeAllText,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text(
                        text = seeAllText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
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
                enabled = isInternetAvailable || !category.requireInternetConnection
            )
        }
    }
}
