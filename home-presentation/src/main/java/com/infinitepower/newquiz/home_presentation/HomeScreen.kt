package com.infinitepower.newquiz.home_presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.home_card.components.HomeCardItemContent
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.core.util.ui.nav_drawer.NavDrawerUtil
import com.infinitepower.newquiz.home_presentation.data.getHomeCardItemData
import com.infinitepower.newquiz.home_presentation.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun HomeScreen(
    navigator: DestinationsNavigator,
    homeScreenNavigator: HomeScreenNavigator,
    navDrawerUtil: NavDrawerUtil,
    homeViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()

    val homeCardItemData = remember {
        getHomeCardItemData(homeNavigator = homeScreenNavigator)
    }

    HomeScreenImpl(
        uiState = uiState,
        homeCardItemData = homeCardItemData,
        onEvent = homeViewModel::onEvent,
        navigateToLoginScreen = { navigator.navigate(LoginScreenDestination) },
        openDrawer = navDrawerUtil::open
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeScreenImpl(
    uiState: HomeScreenUiState,
    homeCardItemData: List<HomeCardItem>,
    onEvent: (event: HomeScreenUiEvent) -> Unit,
    navigateToLoginScreen: () -> Unit,
    openDrawer: () -> Unit,
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
                    Text(text = "NewQuiz")
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
            // TODO: Add login
            /*
            if (!uiState.isLoggedIn && uiState.showLoginCard) {
                item {
                    SignInCard(
                        modifier = Modifier.fillParentMaxWidth(),
                        onSignInClick = navigateToLoginScreen,
                        onDismissClick = {
                            onEvent(HomeScreenUiEvent.DismissLoginCard)
                        }
                    )
                }
            }

             */

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

@Composable
private fun SignInCard(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit,
    onDismissClick: () -> Unit
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
        getHomeCardItemData(homeNavigator = HomeNavigatorPreviewImpl())
    }

    NewQuizTheme {
        HomeScreenImpl(
            uiState = uiState,
            homeCardItemData = homeCardItemData,
            onEvent = {},
            navigateToLoginScreen = {},
            openDrawer = {}
        )
    }
}