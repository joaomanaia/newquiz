package com.infinitepower.newquiz.feature.settings.screens.about_and_help

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.AppNameWithLogo
import com.infinitepower.newquiz.feature.settings.components.AboutAndHelpButtons
import com.infinitepower.newquiz.feature.settings.model.Preference
import com.infinitepower.newquiz.feature.settings.screens.PreferenceScreen
import com.infinitepower.newquiz.feature.settings.util.datastore.rememberSettingsDataStoreManager
import kotlinx.collections.immutable.persistentListOf

@Composable
@ExperimentalMaterial3Api
internal fun AboutAndHelpScreen(
    modifier: Modifier = Modifier,
    isScreenExpanded: Boolean,
    onBackClick: () -> Unit,
) {
    val dataStoreManager = rememberSettingsDataStoreManager()

    val items = remember {
        persistentListOf(
            Preference.CustomPreference(
                title = "NewQuiz Logo",
                content = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(MaterialTheme.spacing.medium)
                    ) {
                        AppNameWithLogo()
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
                        AboutAndHelpButtons()
                    }
                }
            )
        )
    }

    PreferenceScreen(
        modifier = modifier,
        title = stringResource(id = R.string.about_and_help),
        items = items,
        dataStoreManager = dataStoreManager,
        isScreenExpanded = isScreenExpanded,
        onBackClick = onBackClick
    )
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun AboutAndHelpScreenPreview() {
    NewQuizTheme {
        Surface {
            AboutAndHelpScreen(
                modifier = Modifier.padding(16.dp),
                isScreenExpanded = true,
                onBackClick = {}
            )
        }
    }
}
