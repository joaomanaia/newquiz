package com.infinitepower.newquiz.settings_presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.infinitepower.newquiz.core.analytics.logging.CoreLoggingAnalytics
import com.infinitepower.newquiz.core.analytics.logging.LocalCoreLoggingAnalytics
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.common.dataStore.settingsDataStore
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManagerImpl
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.settings_presentation.SettingsScreenUiEvent
import com.infinitepower.newquiz.settings_presentation.SettingsUiState
import com.infinitepower.newquiz.settings_presentation.data.SettingsScreenPageData
import com.infinitepower.newquiz.settings_presentation.model.ScreenKey

@Composable
@ExperimentalMaterial3Api
internal fun PreferencesScreen(
    page: SettingsScreenPageData,
    uiState: SettingsUiState,
    screenExpanded: Boolean,
    onEvent: (event: SettingsScreenUiEvent) -> Unit,
    navigateToScreen: (screenKey: ScreenKey) -> Unit
) {
    val context = LocalContext.current
    val dataStore = context.settingsDataStore
    val dataStoreManager = remember {
        DataStoreManagerImpl(dataStore)
    }

    PreferencesScreen(
        page = page,
        uiState = uiState,
        screenExpanded = screenExpanded,
        dataStoreManager = dataStoreManager,
        coreLoggingAnalytics = rememberCoreLoggingAnalytics(),
        onEvent = onEvent,
        navigateToScreen = navigateToScreen
    )
}

@Composable
@ExperimentalMaterial3Api
internal fun PreferencesScreen(
    page: SettingsScreenPageData,
    uiState: SettingsUiState,
    screenExpanded: Boolean,
    dataStoreManager: DataStoreManager,
    coreLoggingAnalytics: CoreLoggingAnalytics,
    onEvent: (event: SettingsScreenUiEvent) -> Unit,
    navigateToScreen: (screenKey: ScreenKey) -> Unit
) {
    val scope = rememberCoroutineScope()

    PreferenceScreen(
        items = when(page) {
            is SettingsScreenPageData.MainPage -> page.items(
                navigateToScreen = navigateToScreen,
                screenExpanded = screenExpanded,
                inMainPage = uiState.screenKey == SettingsScreenPageData.MainPage.key,
                currentScreenKey = uiState.screenKey
            )
            is SettingsScreenPageData.General -> page.items(scope, dataStoreManager)
            is SettingsScreenPageData.MultiChoiceQuiz -> page.items(
                translationModelState = uiState.translationModelState,
                downloadTranslationModel = { onEvent(SettingsScreenUiEvent.DownloadTranslationModel) },
                deleteTranslationModel = { onEvent(SettingsScreenUiEvent.DeleteTranslationModel) }
            )
            is SettingsScreenPageData.Wordle -> page.items(
                clearWordleCalendarItems = { onEvent(SettingsScreenUiEvent.ClearWordleCalendarItems) },
                onChangeWordleLang = coreLoggingAnalytics::setWordleLangUserProperty
            )
            is SettingsScreenPageData.User -> page.items(
                userIsSignedIn = uiState.userIsSignedIn,
                signOut = { onEvent(SettingsScreenUiEvent.SignOut) }
            )
        },
        dataStoreManager = dataStoreManager,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
@AllPreviewsNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun PreferencesScreenPreview() {
    val context = LocalContext.current
    val dataStore = context.settingsDataStore
    val dataStoreManager = remember {
        DataStoreManagerImpl(dataStore)
    }

    NewQuizTheme {
        Surface {
            PreferencesScreen(
                page = SettingsScreenPageData.General,
                uiState = SettingsUiState(),
                dataStoreManager = dataStoreManager,
                coreLoggingAnalytics = LocalCoreLoggingAnalytics,
                onEvent = {},
                navigateToScreen = {},
                screenExpanded = false
            )
        }
    }
}