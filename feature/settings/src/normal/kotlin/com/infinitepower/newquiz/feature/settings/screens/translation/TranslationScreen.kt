package com.infinitepower.newquiz.feature.settings.screens.translation

import android.os.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.core.translation.TranslatorModelState
import com.infinitepower.newquiz.feature.settings.model.Preference
import com.infinitepower.newquiz.feature.settings.screens.PreferenceScreen
import com.infinitepower.newquiz.feature.settings.util.datastore.rememberSettingsDataStoreManager
import kotlinx.collections.immutable.persistentListOf

@Composable
@ExperimentalMaterial3Api
internal fun TranslationScreen(
    modifier: Modifier = Modifier,
    isScreenExpanded: Boolean,
    onBackClick: () -> Unit,
    viewModel: TranslationScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TranslationScreen(
        modifier = modifier,
        uiState = uiState,
        isScreenExpanded = isScreenExpanded,
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent
    )
}

@Composable
@ExperimentalMaterial3Api
internal fun TranslationScreen(
    modifier: Modifier = Modifier,
    uiState: TranslationScreenUiState,
    isScreenExpanded: Boolean,
    onBackClick: () -> Unit,
    onEvent: (event: TranslationScreenUiEvent) -> Unit
) {
    val dataStoreManager = rememberSettingsDataStoreManager()

    val items = persistentListOf(
        Preference.PreferenceItem.SwitchPreference(
            request = SettingsCommon.Translation.Enabled,
            title = stringResource(id = CoreR.string.translation_enabled),
            primarySwitch = true
        ),
        Preference.PreferenceItem.ListPreference(
            request = SettingsCommon.Translation.TargetLanguage,
            title = stringResource(id = CoreR.string.target_language),
            summary = stringResource(id = CoreR.string.target_language_description),
            entries = uiState.translatorTargetLanguages,
            enabled = uiState.translationModelState == TranslatorModelState.None,
            dependency = persistentListOf(SettingsCommon.Translation.Enabled)
        ),
        Preference.PreferenceItem.TextPreference(
            title = stringResource(id = CoreR.string.download_translation_model),
            summary = stringResource(id = CoreR.string.download_translation_model_description),
            dependency = persistentListOf(SettingsCommon.Translation.Enabled),
            visible = uiState.translationModelState == TranslatorModelState.None,
            enabled = uiState.translatorTargetLanguage.isNotBlank(),
            onClick = { onEvent(TranslationScreenUiEvent.DownloadTranslationModel) }
        ),
        Preference.PreferenceItem.TextPreference(
            title = stringResource(id = CoreR.string.delete_translation_model),
            dependency = persistentListOf(SettingsCommon.Translation.Enabled),
            visible = uiState.translationModelState == TranslatorModelState.Downloaded,
            onClick = { onEvent(TranslationScreenUiEvent.DeleteTranslationModel) }
        ),
        Preference.PreferenceGroup(
            title = stringResource(id = CoreR.string.download_settings),
            preferenceItems = listOf(
                Preference.PreferenceItem.SwitchPreference(
                    request = SettingsCommon.Translation.RequireWifi,
                    title = stringResource(id = CoreR.string.require_wifi),
                    summary = stringResource(id = CoreR.string.translation_require_wifi_description),
                    dependency = persistentListOf(SettingsCommon.Translation.Enabled),
                ),
                Preference.PreferenceItem.SwitchPreference(
                    request = SettingsCommon.Translation.RequireCharging,
                    title = stringResource(id = CoreR.string.require_charging),
                    summary = stringResource(id = CoreR.string.translation_require_charging_description),
                    dependency = persistentListOf(SettingsCommon.Translation.Enabled),
                    visible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                ),
            )
        )
    )

    PreferenceScreen(
        modifier = modifier,
        title = stringResource(id = CoreR.string.translation),
        items = items,
        dataStoreManager = dataStoreManager,
        isScreenExpanded = isScreenExpanded,
        onBackClick = onBackClick
    )
}
