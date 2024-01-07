package com.infinitepower.newquiz.feature.settings.screens.wordle

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.analytics.LocalAnalyticsHelper
import com.infinitepower.newquiz.core.analytics.UserProperty
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.feature.settings.model.Preference
import com.infinitepower.newquiz.feature.settings.screens.PreferenceScreen
import com.infinitepower.newquiz.feature.settings.util.datastore.rememberSettingsDataStoreManager
import kotlinx.collections.immutable.persistentListOf

@Composable
@ExperimentalMaterial3Api
internal fun WordleScreen(
    modifier: Modifier = Modifier,
    isScreenExpanded: Boolean,
    onBackClick: () -> Unit,
) {
    val dataStoreManager = rememberSettingsDataStoreManager()

    val analyticsHelper = LocalAnalyticsHelper.current

    val items = persistentListOf(
        Preference.PreferenceItem.SwitchPreference(
            request = SettingsCommon.WordleHardMode,
            title = stringResource(id = CoreR.string.hard_mode),
            summary = stringResource(id = CoreR.string.any_revealed_hints_must_be_used_in_subsequest_guesses)
        ),
        Preference.PreferenceItem.SwitchPreference(
            request = SettingsCommon.WordleColorBlindMode,
            title = stringResource(id = CoreR.string.color_blind_mode),
            summary = stringResource(id = CoreR.string.high_contrast_colors)
        ),
        Preference.PreferenceItem.SwitchPreference(
            request = SettingsCommon.WordleLetterHints,
            title = stringResource(id = CoreR.string.letter_hints),
            summary = stringResource(
                id = CoreR.string.hint_above_the_letter_that_it_appears_twice_or_more_in_the_hidden_word
            )
        ),
        Preference.PreferenceGroup(
            title = stringResource(id = CoreR.string.wordle_infinite),
            preferenceItems = listOf(
                Preference.PreferenceItem.ListPreference(
                    request = SettingsCommon.InfiniteWordleQuizLanguage,
                    title = stringResource(id = CoreR.string.quiz_language),
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Language,
                            contentDescription = stringResource(id = CoreR.string.quiz_language),
                        )
                    },
                    entries = mapOf(
                        "en" to stringResource(id = CoreR.string.english),
                        "pt" to stringResource(id = CoreR.string.portuguese),
                        "es" to stringResource(id = CoreR.string.spanish),
                        "fr" to stringResource(id = CoreR.string.french)
                    ),
                    onItemClick = { lang ->
                        analyticsHelper.setUserProperty(UserProperty.WordleLanguage(lang))
                    }
                ),
                Preference.PreferenceItem.SwitchPreference(
                    request = SettingsCommon.WordleInfiniteRowsLimited,
                    title = stringResource(id = CoreR.string.rows_limited),
                    summary = stringResource(id = CoreR.string.wordle_infinite_row_limited)
                ),
                Preference.PreferenceItem.SeekBarPreference(
                    request = SettingsCommon.WordleInfiniteRowsLimit,
                    title = stringResource(id = CoreR.string.rows_limited),
                    summary = stringResource(id = CoreR.string.wordle_infinite_row_limit_value),
                    valueRange = 2..30,
                    dependency = persistentListOf(SettingsCommon.WordleInfiniteRowsLimited)
                )
            )
        )
    )

    PreferenceScreen(
        modifier = modifier,
        title = stringResource(id = CoreR.string.wordle),
        items = items,
        dataStoreManager = dataStoreManager,
        isScreenExpanded = isScreenExpanded,
        onBackClick = onBackClick
    )
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun WordleScreenPreview() {
    NewQuizTheme {
        Surface {
            WordleScreen(
                modifier = Modifier.padding(16.dp),
                isScreenExpanded = true,
                onBackClick = {}
            )
        }
    }
}
