package com.infinitepower.newquiz.feature.settings.screens.animations

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.feature.settings.model.Preference
import com.infinitepower.newquiz.feature.settings.screens.PreferenceScreen
import com.infinitepower.newquiz.feature.settings.util.datastore.rememberSettingsDataStoreManager
import kotlinx.collections.immutable.persistentListOf
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@ExperimentalMaterial3Api
internal fun AnimationsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    isScreenExpanded: Boolean,
) {
    val dataStoreManager = rememberSettingsDataStoreManager()

    val items = persistentListOf(
        Preference.PreferenceItem.SwitchPreference(
            request = SettingsCommon.GlobalAnimationsEnabled,
            title = stringResource(id = CoreR.string.animations_enabled),
            primarySwitch = true
        ),
        Preference.PreferenceItem.SwitchPreference(
            request = SettingsCommon.WordleAnimationsEnabled,
            title = stringResource(id = CoreR.string.wordle_animations_enabled),
            summary = stringResource(id = CoreR.string.wordle_animations_enabled_description),
            dependency = persistentListOf(SettingsCommon.GlobalAnimationsEnabled)
        ),
        Preference.PreferenceItem.SwitchPreference(
            request = SettingsCommon.MultiChoiceAnimationsEnabled,
            title = stringResource(id = CoreR.string.multi_choice_animations_enabled),
            summary = stringResource(id = CoreR.string.multi_choice_animations_enabled_description),
            dependency = persistentListOf(SettingsCommon.GlobalAnimationsEnabled)
        )
    )

    PreferenceScreen(
        modifier = modifier,
        title = stringResource(id = CoreR.string.animations),
        items = items,
        dataStoreManager = dataStoreManager,
        isScreenExpanded = isScreenExpanded,
        onBackClick = onBackClick
    )
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun AnimationsScreenPreview() {
    NewQuizTheme {
        Surface {
            AnimationsScreen(
                modifier = Modifier.padding(16.dp),
                isScreenExpanded = true,
                onBackClick = {}
            )
        }
    }
}
