package com.infinitepower.newquiz.multi_choice_quiz.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.home_card.components.HomeCardItemContent
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.multi_choice_quiz.list.data.MultiChoiceQuizListCardItemData
import com.infinitepower.newquiz.core.R as CoreR
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun MultiChoiceQuizListScreen(
    navigator: DestinationsNavigator,
    viewModel: MultiChoiceQuizListScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val cardItemData = remember(uiState.savedQuestionsSize) {
        MultiChoiceQuizListCardItemData(navigator, uiState.savedQuestionsSize)
    }

    MultiChoiceQuizListScreenImpl(cardItemData = cardItemData.items)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MultiChoiceQuizListScreenImpl(
    cardItemData: List<HomeCardItem>
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
                    Text(text = stringResource(id = CoreR.string.multi_choice_quiz))
                },
                scrollBehavior = scrollBehavior
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