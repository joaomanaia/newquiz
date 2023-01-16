package com.infinitepower.newquiz.settings_presentation

import androidx.annotation.Keep
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.settings_presentation.components.PreferencesScreen
import com.infinitepower.newquiz.settings_presentation.data.SettingsScreenPageData
import com.infinitepower.newquiz.settings_presentation.destinations.SettingsScreenDestination
import com.infinitepower.newquiz.settings_presentation.model.ScreenKey
import com.infinitepower.newquiz.translation_dynamic_feature.TranslatorUtil
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction

@Keep
data class SettingsScreenNavArgs(
    val screenKey: String = SettingsScreenPageData.MainPage.key.value
)

@Composable
@Destination(navArgsDelegate = SettingsScreenNavArgs::class)
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(
    navigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreenImpl(
        uiState = uiState,
        windowSizeClass = windowSizeClass,
        onBackClick = navigator::popBackStack,
        onNavigateClick = navigator::navigate,
        onEvent = settingsViewModel::onEvent
    )

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("SettingsScreen")
    }

    if (uiState.translationModelState == TranslatorUtil.TranslatorModelState.Downloading) {
        DownloadingTranslatorDialog()
    }
}

@Composable
@ExperimentalMaterial3Api
private fun SettingsScreenImpl(
    uiState: SettingsUiState,
    windowSizeClass: WindowSizeClass,
    onBackClick: () -> Unit,
    onNavigateClick: (direction: Direction) -> Unit,
    onEvent: (event: SettingsScreenUiEvent) -> Unit
) {
    SettingsContainer(
        screenKey = uiState.screenKey,
        windowWidthSizeClass = windowSizeClass.widthSizeClass,
        mainContent = {
            PreferencesScreen(
                page = SettingsScreenPageData.MainPage,
                uiState = uiState,
                onEvent = onEvent,
                navigateToScreen = { screenKey ->
                    onNavigateClick(SettingsScreenDestination(screenKey.value))
                },
                screenExpanded = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact
            )
        },
        settingsContent = {
            PreferencesScreen(
                page = SettingsScreenPageData.getPage(uiState.screenKey),
                uiState = uiState,
                onEvent = onEvent,
                navigateToScreen = { screenKey ->
                    onNavigateClick(SettingsScreenDestination(screenKey.value))
                },
                screenExpanded = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact
            )
        },
        onBackClick = onBackClick
    )
}

@Composable
@ExperimentalMaterial3Api
private fun SettingsContainer(
    modifier: Modifier = Modifier,
    screenKey: ScreenKey,
    windowWidthSizeClass: WindowWidthSizeClass,
    onBackClick: () -> Unit,
    mainContent: @Composable BoxScope.() -> Unit,
    settingsContent: @Composable BoxScope.() -> Unit
) {
    val isMainPage = screenKey == SettingsScreenPageData.MainPage.key

    val currentPage = remember(screenKey) {
        SettingsScreenPageData.getPage(screenKey)
    }

    val topBarTextStringRes = if (isMainPage) {
        SettingsScreenPageData.MainPage.stringRes
    } else {
        currentPage.stringRes
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val isExpandedWidthAndNotMainPage = windowWidthSizeClass > WindowWidthSizeClass.Compact && !isMainPage

    val mainContentColor by animateColorAsState(
        targetValue = if (isExpandedWidthAndNotMainPage) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.surface
        }
    )

    Row(modifier = modifier.fillMaxSize()) {
        if (isExpandedWidthAndNotMainPage) {
            Surface(
                modifier = Modifier.weight(1f),
                color = mainContentColor
            ) {
                Box(
                    content = mainContent,
                    modifier = Modifier.padding(MaterialTheme.spacing.medium)
                )
            }
        }

        Scaffold(
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(text = stringResource(id = topBarTextStringRes))
                    },
                    scrollBehavior = scrollBehavior,
                    navigationIcon = { BackIconButton(onClick = onBackClick) }
                )
            },
            modifier = Modifier
                .weight(2f)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding),
                content = settingsContent
            )
        }
    }
}

@Composable
private fun DownloadingTranslatorDialog() {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = "Downloading translation model")
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Please wait until translation model is downloaded!")
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                CircularProgressIndicator()
            }
        },
        confirmButton = {
            TextButton(
                onClick = {},
                enabled = false
            ) {
                Text(text = "Close")
            }
        }
    )
}

@Composable
@AllPreviewsNightLight
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
private fun SettingsScreenPreview() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(screenWidth, screenHeight))

    NewQuizTheme {
        Surface {
            SettingsScreenImpl(
                uiState = SettingsUiState(
                    screenKey = SettingsScreenPageData.General.key
                ),
                windowSizeClass = windowSizeClass,
                onBackClick = {},
                onNavigateClick = {},
                onEvent = {}
            )
        }
    }
}