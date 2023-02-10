package com.infinitepower.newquiz.core.ui.home_card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.home_card.components.HomeCardItemContent
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem

@Composable
@ExperimentalMaterial3Api
fun HomeListContent(
    items: List<HomeCardItem>
) {
    val spaceMedium = MaterialTheme.spacing.medium

    LazyColumn(
        contentPadding = PaddingValues(spaceMedium),
        verticalArrangement = Arrangement.spacedBy(spaceMedium)
    ) {
        items(
            items = items,
            key = { it.getId() },
        ) { item ->
            HomeCardItemContent(
                modifier = Modifier.fillParentMaxWidth(),
                item = item
            )
        }
    }
}