package com.infinitepower.newquiz.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.feature.settings.destinations.SettingsScreenDestination
import com.infinitepower.newquiz.feature.settings.model.ScreenKey
import com.infinitepower.newquiz.feature.settings.screens.PreferencesScreen
import com.infinitepower.newquiz.feature.settings.screens.main.MainScreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(
    screenKey: ScreenKey? = null,
    windowSizeClass: WindowSizeClass,
    navigator: DestinationsNavigator,
    // viewModel: MainSettingsViewModel = hiltViewModel()
) {
    val isScreenExpanded = remember(windowSizeClass.widthSizeClass) {
        windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    }

    SettingsContainer(
        currentScreen = screenKey,
        windowWidthSizeClass = windowSizeClass.widthSizeClass,
        mainContent = {
            MainScreen(
                currentScreen = screenKey,
                onScreenSelect = { key ->
                    navigator.navigate(SettingsScreenDestination(key))
                },
                onBackClick = navigator::popBackStack,
            )
        },
        preferencesContent = {
            PreferencesScreen(
                currentScreen = screenKey,
                isScreenExpanded = isScreenExpanded,
                onBackClick = navigator::popBackStack,
                navigateToScreen = { key ->
                    navigator.navigate(SettingsScreenDestination(key))
                }
            )
        }
    )
}

@Composable
private fun SettingsContainer(
    modifier: Modifier = Modifier,
    currentScreen: ScreenKey?,
    windowWidthSizeClass: WindowWidthSizeClass,
    mainContent: @Composable BoxScope.() -> Unit,
    preferencesContent: @Composable BoxScope.() -> Unit
) {
    val isInMainScreen = remember(currentScreen) { currentScreen == null }
    val isExpanded = remember(windowWidthSizeClass) {
        windowWidthSizeClass == WindowWidthSizeClass.Expanded
    }

    // If the screen is not in the main screen and the window is expanded, we want to show the
    // main content in the left side of the screen and the preferences content in the right side.
    // Otherwise, we only show the current screen content.
    val inPreferencesAndExpanded = remember(isInMainScreen, isExpanded) {
        !isInMainScreen && isExpanded
    }

    val leftContent = if (isInMainScreen || isExpanded) mainContent else preferencesContent

    Row(modifier = modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .weight(weight = 1f)
                .padding(if (inPreferencesAndExpanded) MaterialTheme.spacing.medium else 0.dp),
            tonalElevation = if (inPreferencesAndExpanded) 4.dp else 0.dp,
            shape = if (inPreferencesAndExpanded) MaterialTheme.shapes.extraLarge else RectangleShape,
        ) {
            Box(
                modifier = Modifier.padding(
                    if (inPreferencesAndExpanded) MaterialTheme.spacing.medium else 0.dp
                ),
                content = leftContent
            )
        }

        if (inPreferencesAndExpanded) {
            Box(
                modifier = Modifier.weight(2f),
                content = preferencesContent
            )
        }
    }
}

@Composable
@PreviewScreenSizes
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private fun SettingsScreenPreview() {
    BoxWithConstraints {
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(maxWidth, maxHeight))

        NewQuizTheme {
            Surface {
                SettingsScreen(
                    screenKey = ScreenKey.WORDLE,
                    windowSizeClass = windowSizeClass,
                    navigator = EmptyDestinationsNavigator
                )
            }
        }
    }
}
