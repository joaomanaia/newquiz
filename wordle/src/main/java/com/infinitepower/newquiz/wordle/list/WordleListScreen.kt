package com.infinitepower.newquiz.wordle.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.home_card.components.HomeCardItemContent
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.wordle.list.data.getWordListCardItemData
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun WordleListScreen(
    navigator: DestinationsNavigator
) {
    val cardItemData = remember { getWordListCardItemData(navigator) }

    WordleListScreenImpl(
        cardItemData = cardItemData
    )

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("WordleListScreen")
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun WordleListScreenImpl(
    cardItemData: List<HomeCardItem>
) {
    val spaceMedium = MaterialTheme.spacing.medium

    LazyColumn(
        contentPadding = PaddingValues(spaceMedium),
        verticalArrangement = Arrangement.spacedBy(spaceMedium)
    ) {
        items(
            items = cardItemData,
            key = { it.getId() },
        ) { item ->
            HomeCardItemContent(
                modifier = Modifier.fillParentMaxWidth(),
                item = item
            )
        }
    }
}