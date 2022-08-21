package com.infinitepower.newquiz.core.ui.home_card.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.infinitepower.newquiz.core.theme.spacing

@Composable
internal fun <T> HomeHorizontalItems(
    items: List<T>,
    itemContent: @Composable (item: T) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        items(items = items) { item ->
            itemContent(item)
        }
    }
}