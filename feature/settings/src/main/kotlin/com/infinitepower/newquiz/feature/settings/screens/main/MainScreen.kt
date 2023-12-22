package com.infinitepower.newquiz.feature.settings.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.ListAlt
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.feature.settings.common.BuildVariant
import com.infinitepower.newquiz.feature.settings.model.Preference
import com.infinitepower.newquiz.feature.settings.model.ScreenKey
import com.infinitepower.newquiz.feature.settings.screens.PreferenceScreen
import com.infinitepower.newquiz.feature.settings.util.datastore.rememberSettingsDataStoreManager

@Composable
@ExperimentalMaterial3Api
internal fun MainScreen(
    modifier: Modifier = Modifier,
    currentScreen: ScreenKey?,
    onScreenSelected: (key: ScreenKey) -> Unit,
    onBackClick: () -> Unit
) {
    val dataStoreManager = rememberSettingsDataStoreManager()

    val items = listOf(
        // General
        Preference.PreferenceItem.NavigationButton(
            title = stringResource(id = CoreR.string.general),
            summary = stringResource(id = CoreR.string.general_settings_summary),
            singleLineSummary = true,
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = null
                )
            },
            screenKey = ScreenKey.GENERAL,
            itemSelected = currentScreen == ScreenKey.GENERAL,
            onClick = { onScreenSelected(ScreenKey.GENERAL) },
        ),
        // Multi Choice Quiz
        Preference.PreferenceItem.NavigationButton(
            title = stringResource(id = CoreR.string.multi_choice_quiz),
            summary = stringResource(id = CoreR.string.multi_choice_settings_summary),
            singleLineSummary = true,
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ListAlt,
                    contentDescription = null
                )
            },
            screenKey = ScreenKey.MULTI_CHOICE_QUIZ,
            itemSelected = currentScreen == ScreenKey.MULTI_CHOICE_QUIZ,
            onClick = { onScreenSelected(ScreenKey.MULTI_CHOICE_QUIZ) },
        ),
        // Wordle
        Preference.PreferenceItem.NavigationButton(
            title = stringResource(id = CoreR.string.wordle),
            summary = stringResource(id = CoreR.string.wordle_settings_summary),
            singleLineSummary = true,
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Password,
                    contentDescription = null
                )
            },
            screenKey = ScreenKey.WORDLE,
            itemSelected = currentScreen == ScreenKey.WORDLE,
            onClick = { onScreenSelected(ScreenKey.WORDLE) },
        ),
        // Translation
        Preference.PreferenceItem.NavigationButton(
            title = stringResource(id = CoreR.string.translation),
            summary = stringResource(id = CoreR.string.translation_settings_summary),
            singleLineSummary = true,
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Translate,
                    contentDescription = null
                )
            },
            screenKey = ScreenKey.TRANSLATION,
            itemSelected = currentScreen == ScreenKey.TRANSLATION,
            onClick = { onScreenSelected(ScreenKey.TRANSLATION) },
            visible = BuildVariant.DISTRIBUTION_FLAVOR == "normal"
        ),
        // About & Help
        Preference.PreferenceItem.NavigationButton(
            title = stringResource(id = CoreR.string.about_and_help),
            summary = stringResource(id = CoreR.string.about_and_help_settings_summary),
            singleLineSummary = true,
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Help,
                    contentDescription = null
                )
            },
            screenKey = ScreenKey.ABOUT_AND_HELP,
            itemSelected = currentScreen == ScreenKey.ABOUT_AND_HELP,
            onClick = { onScreenSelected(ScreenKey.ABOUT_AND_HELP) },
        ),
    )

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = CoreR.string.settings)) },
                scrollBehavior = scrollBehavior,
                navigationIcon = { BackIconButton(onClick = onBackClick) }
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        PreferenceScreen(
            modifier = Modifier.padding(innerPadding),
            items = items,
            dataStoreManager = dataStoreManager
        )
    }
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun MainScreenPreview() {
    NewQuizTheme {
        Surface {
            MainScreen(
                modifier = Modifier.padding(16.dp),
                currentScreen = ScreenKey.GENERAL,
                onScreenSelected = {},
                onBackClick = {}
            )
        }
    }
}
