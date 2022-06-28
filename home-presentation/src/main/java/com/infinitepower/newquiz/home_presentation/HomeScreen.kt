package com.infinitepower.newquiz.home_presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.home_presentation.components.HomeGroupTitle
import com.infinitepower.newquiz.home_presentation.components.HomeLargeCard
import com.infinitepower.newquiz.home_presentation.data.HomeCardItemData
import com.infinitepower.newquiz.home_presentation.model.HomeCardItem
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun HomeScreen(
    homeNavigator: HomeNavigator
) {
    val uiState = remember {
        HomeScreenUiState()
    }

    val homeCardItemData = remember {
        HomeCardItemData(homeNavigator = homeNavigator)
    }

    HomeScreenImpl(
        uiState = uiState,
        homeCardItemData = homeCardItemData.items
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeScreenImpl(
    uiState: HomeScreenUiState,
    homeCardItemData: List<HomeCardItem>
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarScrollState()
    )

    val spaceMedium = MaterialTheme.spacing.medium

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "NewQuiz")
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
            if (!uiState.isLoggedIn && uiState.showLoginCard) {
                item {
                    SignInCard(
                        modifier = Modifier.fillParentMaxWidth(),
                        onSignInClick = {},
                        onDismissClick = {}
                    )
                }

                items(
                    items = homeCardItemData,
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
}

@Composable
@ExperimentalMaterial3Api
private fun HomeCardItemContent(
    modifier: Modifier = Modifier,
    item: HomeCardItem
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
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SignInCard(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit,
    onDismissClick: () -> Unit,
) {
    val spaceMedium = MaterialTheme.spacing.medium

    OutlinedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(spaceMedium),
        ) {
            Text(
                text = "Save your progress, buy skips and see leaderboard by signing in!",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(spaceMedium))
            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spaceMedium)
            ) {
                TextButton(onClick = onDismissClick) {
                    Text(text = "Dismiss")
                }
                FilledTonalButton(onClick = onSignInClick) {
                    Text(text = "Sign In")
                }
            }
        }
    }
}

@Composable
@PreviewNightLight
private fun HomeScreenPreview() {
    val uiState = remember { HomeScreenUiState() }

    val homeCardItemData = remember {
        HomeCardItemData(homeNavigator = HomeNavigatorPreviewImpl())
    }

    NewQuizTheme {
        HomeScreenImpl(
            uiState = uiState,
            homeCardItemData = homeCardItemData.items
        )
    }
}