package com.infinitepower.newquiz.core.ui.home_card.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem

@Composable
@ExperimentalMaterial3Api
fun HomeCardItemContent(
    modifier: Modifier = Modifier,
    item: HomeCardItem,
) {
    when (item) {
        is HomeCardItem.GroupTitle -> {
            HomeGroupTitle(
                modifier = modifier,
                data = item
            )
        }
        is HomeCardItem.LargeCard -> {
            HomeLargeCard(
                modifier = modifier,
                data = item
            )
        }
        is HomeCardItem.MediumCard -> {
            HomeMediumCard(
                modifier = modifier,
                data = item
            )
        }
        is HomeCardItem.HorizontalItems<*> -> {
            HomeHorizontalItems(
                item = item,
                itemContent = item.itemContent
            )
        }
        is HomeCardItem.CustomItem -> item.content()
    }
}