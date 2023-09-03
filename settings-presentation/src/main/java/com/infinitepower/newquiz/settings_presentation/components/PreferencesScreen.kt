package com.infinitepower.newquiz.settings_presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.infinitepower.newquiz.core.analytics.LocalAnalyticsHelper
import com.infinitepower.newquiz.core.analytics.UserProperty
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
        onEvent = onEvent,
        navigateToScreen = navigateToScreen
    )
}

@Composable
internal fun PreferencesScreen(
    page: SettingsScreenPageData,
    uiState: SettingsUiState,
    screenExpanded: Boolean,
    dataStoreManager: DataStoreManager,
    onEvent: (event: SettingsScreenUiEvent) -> Unit,
    navigateToScreen: (screenKey: ScreenKey) -> Unit
) {
    val scope = rememberCoroutineScope()

    val analyticsHelper = LocalAnalyticsHelper.current

    PreferenceScreen(
        items = when(page) {
            is SettingsScreenPageData.MainPage -> page.items(
                navigateToScreen = navigateToScreen,
                screenExpanded = screenExpanded,
                inMainPage = uiState.screenKey == SettingsScreenPageData.MainPage.key,
                currentScreenKey = uiState.screenKey,
                translatorAvailable = uiState.translatorAvailable,
                userIsSignedIn = uiState.userIsSignedIn
            )
            is SettingsScreenPageData.General -> page.items(
                scope = scope,
                dataStoreManager = dataStoreManager,
                navigateToScreen = navigateToScreen,
                cleanRecentCategories = { onEvent(SettingsScreenUiEvent.ClearHomeRecentCategories) }
            )
            is SettingsScreenPageData.MultiChoiceQuiz -> page.items()
            is SettingsScreenPageData.Wordle -> page.items(
                onChangeWordleLang = { lang ->
                    analyticsHelper.setUserProperty(UserProperty.WordleLanguage(lang))
                }
            )
            is SettingsScreenPageData.Translation -> page.items(
                currentTargetLanguage = uiState.translatorTargetLanguage,
                targetLanguages = uiState.translatorTargetLanguages,
                translationModelState = uiState.translationModelState,
                downloadTranslationModel = { onEvent(SettingsScreenUiEvent.DownloadTranslationModel) },
                deleteTranslationModel = { onEvent(SettingsScreenUiEvent.DeleteTranslationModel) }
            )
            is SettingsScreenPageData.User -> page.items(
                userIsSignedIn = uiState.userIsSignedIn,
                signOut = { onEvent(SettingsScreenUiEvent.SignOut) }
            )
            is SettingsScreenPageData.AboutAndHelp -> page.items()
            is SettingsScreenPageData.Analytics -> page.items(
                analyticsHelper = analyticsHelper,
                enableLoggingAnalytics = { onEvent(SettingsScreenUiEvent.EnableLoggingAnalytics(it)) },
            )
            is SettingsScreenPageData.Animations -> page.items()
        },
        dataStoreManager = dataStoreManager,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
@AllPreviewsNightLight
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
                onEvent = {},
                navigateToScreen = {},
                screenExpanded = false
            )
        }
    }
}