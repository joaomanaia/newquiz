package com.infinitepower.newquiz.core.ui.home_card.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem

@Composable
@ExperimentalMaterial3Api
internal fun <T : Any> HomeHorizontalItems(
    modifier: Modifier = Modifier,
    item: HomeCardItem.HorizontalItems<T>,
    itemContent: @Composable (item: T) -> Unit
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        items(items = item.items) { item ->
            itemContent(item)
        }
    }
}