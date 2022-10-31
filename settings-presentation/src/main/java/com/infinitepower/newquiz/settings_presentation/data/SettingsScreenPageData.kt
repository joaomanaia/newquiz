package com.infinitepower.newquiz.settings_presentation.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleRepository
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.settings_presentation.model.Preference
import com.infinitepower.newquiz.settings_presentation.model.ScreenKey
import com.infinitepower.newquiz.translation_dynamic_feature.TranslatorUtil.TranslatorModelState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class SettingsScreenPageData(val key: ScreenKey) {
    abstract val stringRes: Int

    companion object {
        fun getPage(key: ScreenKey) = when (key) {
            MainPage.key -> MainPage
            General.key -> General
            MultiChoiceQuiz.key -> MultiChoiceQuiz
            Wordle.key -> Wordle
            else -> MainPage
        }
    }

    object MainPage : SettingsScreenPageData(key = ScreenKey("main")) {
        override val stringRes: Int
            get() = CoreR.string.settings
    }

    object General : SettingsScreenPageData(key = ScreenKey("general")) {
        override val stringRes: Int
            get() = CoreR.string.general

        @Composable
        fun items(
            scope: CoroutineScope,
            dataStoreManager: DataStoreManager
        ) = listOf(
            Preference.PreferenceItem.SwitchPreference(
                request = SettingsCommon.ShowLoginCard,
                title = stringResource(id = CoreR.string.show_login_card),
                summary = stringResource(id = CoreR.string.show_login_card_at_home_screen),
                singleLineTitle = true,
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Visibility,
                        contentDescription = stringResource(id = CoreR.string.show_login_card)
                    )
                }
            ),
            Preference.PreferenceItem.TextPreference(
                title = stringResource(id = CoreR.string.clear_settings),
                summary = stringResource(id = CoreR.string.remove_all_saved_settings),
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = stringResource(id = CoreR.string.clear_settings)
                    )
                },
                enabled = true,
                onClick = {
                    scope.launch(Dispatchers.IO) { dataStoreManager.clearPreferences() }
                }
            )
        )
    }

    object MultiChoiceQuiz : SettingsScreenPageData(key = ScreenKey("multi_choice_quiz")) {
        override val stringRes: Int
            get() = CoreR.string.multi_choice_quiz

        @Composable
        fun items(
            translationModelState: TranslatorModelState,
            downloadTranslationModel: () -> Unit,
            deleteTranslationModel: () -> Unit
        ) = listOf(
            Preference.PreferenceItem.SeekBarPreference(
                request = SettingsCommon.MultiChoiceQuizQuestionsSize,
                title = stringResource(id = CoreR.string.quiz_question_size),
                singleLineTitle = true,
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.QuestionMark,
                        contentDescription = stringResource(id = CoreR.string.quiz_question_size),
                    )
                },
                valueRange = (5..20)
            ),
            Preference.PreferenceGroup(
                title = stringResource(id = CoreR.string.translation),
                preferenceItems = listOf(
                    Preference.PreferenceItem.SwitchPreference(
                        request = SettingsCommon.MultiChoiceQuiz.TranslationEnabled,
                        title = stringResource(id = CoreR.string.translation_enabled)
                    ),
                    Preference.PreferenceItem.TextPreference(
                        title = stringResource(id = CoreR.string.download_translation_model),
                        summary = stringResource(id = CoreR.string.download_translation_model_to_device_language),
                        dependency = listOf(SettingsCommon.MultiChoiceQuiz.TranslationEnabled),
                        enabled = translationModelState == TranslatorModelState.None,
                        onClick = downloadTranslationModel
                    ),
                    Preference.PreferenceItem.TextPreference(
                        title = stringResource(id = CoreR.string.delete_translation_model),
                        dependency = listOf(SettingsCommon.MultiChoiceQuiz.TranslationEnabled),
                        enabled = translationModelState == TranslatorModelState.Downloaded,
                        onClick = deleteTranslationModel
                    ),
                )
            )
        )
    }

    object Wordle : SettingsScreenPageData(key = ScreenKey("wordle")) {
        override val stringRes: Int
            get() = CoreR.string.wordle

        @Composable
        fun items(
            scope: CoroutineScope,
            dataStoreManager: DataStoreManager,
            dailyWordleRepository: DailyWordleRepository
        ) = listOf(
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
                summary = stringResource(id = CoreR.string.hint_above_the_letter_that_it_appears_twice_or_more_in_the_hidden_word)
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
                            "pt" to stringResource(id = CoreR.string.portuguese)
                        )
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
                        dependency = listOf(SettingsCommon.WordleInfiniteRowsLimited)
                    )
                )
            ),
            Preference.PreferenceGroup(
                title = stringResource(id = CoreR.string.wordle_daily),
                preferenceItems = listOf(
                    Preference.PreferenceItem.TextPreference(
                        title = stringResource(id = CoreR.string.clean_calendar_data),
                        summary = stringResource(id = CoreR.string.clean_saved_calendar_wins_losses),
                        onClick = {
                            scope.launch(Dispatchers.IO) {
                                dailyWordleRepository.clearAllCalendarItems()
                            }
                        }
                    )
                )
            )
        )
    }
}