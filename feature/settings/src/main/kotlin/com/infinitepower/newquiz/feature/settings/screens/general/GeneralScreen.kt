package com.infinitepower.newquiz.feature.settings.screens.general

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Animation
import androidx.compose.material.icons.rounded.ClearAll
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.feature.settings.common.BuildVariant
import com.infinitepower.newquiz.feature.settings.model.Preference
import com.infinitepower.newquiz.feature.settings.model.ScreenKey
import com.infinitepower.newquiz.feature.settings.screens.PreferenceScreen
import com.infinitepower.newquiz.feature.settings.util.datastore.rememberSettingsDataStoreManager
import com.infinitepower.newquiz.feature.settings.util.getShowCategoryConnectionInfoEntryMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterial3Api
internal fun GeneralScreen(
    modifier: Modifier = Modifier,
    isScreenExpanded: Boolean,
    onBackClick: () -> Unit,
    navigateToScreen: (key: ScreenKey) -> Unit,
    viewModel: GeneralScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GeneralScreen(
        modifier = modifier,
        uiState = uiState,
        isScreenExpanded = isScreenExpanded,
        onBackClick = onBackClick,
        navigateToScreen = navigateToScreen,
        onEvent = viewModel::onEvent
    )
}

@Composable
@ExperimentalMaterial3Api
internal fun GeneralScreen(
    modifier: Modifier = Modifier,
    uiState: GeneralScreenUiState = GeneralScreenUiState(),
    isScreenExpanded: Boolean,
    onBackClick: () -> Unit,
    navigateToScreen: (key: ScreenKey) -> Unit,
    onEvent: (GeneralScreenUiEvent) -> Unit
) {
    val dataStoreManager = rememberSettingsDataStoreManager()
    val scope = rememberCoroutineScope()

    val items = persistentListOf(
        Preference.PreferenceItem.TextPreference(
            title = stringResource(id = R.string.clear_settings),
            summary = stringResource(id = R.string.remove_all_saved_settings),
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = stringResource(id = R.string.clear_settings)
                )
            },
            enabled = true,
            onClick = {
                scope.launch(Dispatchers.IO) { dataStoreManager.clearPreferences() }
            }
        ),
        Preference.PreferenceGroup(
            title = stringResource(id = R.string.categories),
            preferenceItems = listOf(
                Preference.PreferenceItem.ListPreference(
                    title = stringResource(id = R.string.show_category_connection_info),
                    request = SettingsCommon.CategoryConnectionInfoBadge(
                        default = uiState.defaultShowCategoryConnectionInfo
                    ),
                    entries = getShowCategoryConnectionInfoEntryMap(),
                ),
                Preference.PreferenceItem.SwitchPreference(
                    title = stringResource(id = R.string.hide_online_categories),
                    summary = stringResource(id = R.string.hide_online_categories_description),
                    request = SettingsCommon.HideOnlineCategories,
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Visibility,
                            contentDescription = stringResource(id = R.string.hide_online_categories)
                        )
                    }
                ),
                Preference.PreferenceItem.TextPreference(
                    title = stringResource(id = R.string.clear_recent_categories),
                    summary = stringResource(id = R.string.clear_recent_categories_description),
                    onClick = { onEvent(GeneralScreenUiEvent.ClearHomeRecentCategories) },
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.ClearAll,
                            contentDescription = stringResource(id = R.string.clear_recent_categories),
                        )
                    }
                )
            )
        ),
        Preference.PreferenceGroup(
            title = stringResource(id = R.string.maze),
            preferenceItems = listOf(
                Preference.PreferenceItem.SwitchPreference(
                    title = stringResource(id = R.string.maze_settings_auto_scroll_title),
                    summary = stringResource(id = R.string.maze_settings_auto_scroll_summary),
                    request = SettingsCommon.MazeAutoScrollToCurrentItem,
                )
            )
        ),
        Preference.PreferenceItem.TextPreference(
            title = stringResource(id = R.string.animations),
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Animation,
                    contentDescription = stringResource(id = R.string.animations)
                )
            },
            onClick = { navigateToScreen(ScreenKey.ANIMATIONS) }
        ),
        Preference.PreferenceItem.TextPreference(
            title = stringResource(id = R.string.analytics),
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Analytics,
                    contentDescription = stringResource(id = R.string.analytics)
                )
            },
            onClick = { navigateToScreen(ScreenKey.ANALYTICS) },
            visible = BuildVariant.DISTRIBUTION_FLAVOR == "normal"
        )
    )

    PreferenceScreen(
        modifier = modifier,
        title = "General",
        items = items,
        dataStoreManager = dataStoreManager,
        isScreenExpanded = isScreenExpanded,
        onBackClick = onBackClick
    )
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun GeneralScreenPreview() {
    NewQuizTheme {
        Surface {
            GeneralScreen(
                uiState = GeneralScreenUiState(),
                isScreenExpanded = false,
                onBackClick = {},
                navigateToScreen = {},
                onEvent = {}
            )
        }
    }
}
