package com.infinitepower.newquiz.feature.settings.screens.multi_choice_quiz

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.feature.settings.model.Preference
import com.infinitepower.newquiz.feature.settings.screens.PreferenceScreen
import com.infinitepower.newquiz.feature.settings.util.datastore.rememberSettingsDataStoreManager
import kotlinx.collections.immutable.persistentListOf
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@ExperimentalMaterial3Api
internal fun MultiChoiceQuizScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    isScreenExpanded: Boolean,
) {
    val dataStoreManager = rememberSettingsDataStoreManager()

    val items = persistentListOf(
        Preference.PreferenceItem.SeekBarPreference(
            request = SettingsCommon.MultiChoiceQuizQuestionsSize,
            title = stringResource(id = CoreR.string.quiz_question_size),
            singleLineTitle = true,
            valueRange = (5..20)
        )
    )

    PreferenceScreen(
        modifier = modifier,
        title = stringResource(id = CoreR.string.multi_choice_quiz),
        items = items,
        dataStoreManager = dataStoreManager,
        isScreenExpanded = isScreenExpanded,
        onBackClick = onBackClick
    )
}
