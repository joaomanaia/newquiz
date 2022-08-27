package com.infinitepower.newquiz.wordle.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.home_card.components.HomeCardItemContent
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.core.util.ui.nav_drawer.NavDrawerUtil
import com.infinitepower.newquiz.wordle.list.data.getWordListCardItemData
import com.infinitepower.newquiz.core.R as CoreR
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun WordleListScreen(
    navigator: DestinationsNavigator,
    navDrawerUtil: NavDrawerUtil,
) {
    val cardItemData = remember { getWordListCardItemData(navigator) }

    WordleListScreenImpl(
        cardItemData = cardItemData,
        openDrawer = navDrawerUtil::open
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun WordleListScreenImpl(
    cardItemData: List<HomeCardItem>,
    openDrawer: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    val spaceMedium = MaterialTheme.spacing.medium

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = CoreR.string.wordle))
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = "Open drawer"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
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
}