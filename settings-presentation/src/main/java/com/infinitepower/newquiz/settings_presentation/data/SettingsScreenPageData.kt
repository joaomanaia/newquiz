package com.infinitepower.newquiz.settings_presentation.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.ListAlt
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
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
            User.key -> User
            else -> MainPage
        }
    }

    object MainPage : SettingsScreenPageData(key = ScreenKey("main")) {
        override val stringRes: Int
            get() = CoreR.string.settings

        @Composable
        @ReadOnlyComposable
        fun items(
            navigateToScreen: (screenKey: ScreenKey) -> Unit,
            screenExpanded: Boolean,
            inMainPage: Boolean,
            currentScreenKey: ScreenKey
        ) = listOf(
            Preference.PreferenceItem.NavigationButton(
                title = stringResource(id = General.stringRes),
                iconImageVector = Icons.Rounded.Settings,
                screenKey = General.key,
                onClick = { navigateToScreen(General.key) },
                itemSelected = currentScreenKey == General.key,
                screenExpanded = screenExpanded,
                inMainPage = inMainPage
            ),
            Preference.PreferenceItem.NavigationButton(
                title = stringResource(id = MultiChoiceQuiz.stringRes),
                iconImageVector = Icons.Rounded.ListAlt,
                screenKey = MultiChoiceQuiz.key,
                onClick = { navigateToScreen(MultiChoiceQuiz.key) },
                itemSelected = currentScreenKey == MultiChoiceQuiz.key,
                screenExpanded = screenExpanded,
                inMainPage = inMainPage
            ),
            Preference.PreferenceItem.NavigationButton(
                title = stringResource(id = Wordle.stringRes),
                iconImageVector = Icons.Rounded.Password,
                screenKey = Wordle.key,
                onClick = { navigateToScreen(Wordle.key) },
                itemSelected = currentScreenKey == Wordle.key,
                screenExpanded = screenExpanded,
                inMainPage = inMainPage
            ),
            Preference.PreferenceItem.NavigationButton(
                title = stringResource(id = User.stringRes),
                iconImageVector = Icons.Rounded.Person,
                screenKey = User.key,
                onClick = { navigateToScreen(User.key) },
                itemSelected = currentScreenKey == User.key,
                screenExpanded = screenExpanded,
                inMainPage = inMainPage
            ),
        )
    }

    object General : SettingsScreenPageData(key = ScreenKey("general")) {
        override val stringRes: Int
            get() = CoreR.string.general

        @Composable
        @ReadOnlyComposable
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
            ),
            Preference.PreferenceGroup(
                title = "Analytics",
                preferenceItems = listOf(
                    Preference.PreferenceItem.SwitchPreference(
                        request = SettingsCommon.AnalyticsCollection,
                        title = "Analytics collection enabled",
                        onCheckChange = { collectionEnabled ->
                            val firebaseAnalytics = Firebase.analytics
                            firebaseAnalytics.setAnalyticsCollectionEnabled(collectionEnabled)
                        }
                    )
                )
            )
        )
    }

    object MultiChoiceQuiz : SettingsScreenPageData(key = ScreenKey("multi_choice_quiz")) {
        override val stringRes: Int
            get() = CoreR.string.multi_choice_quiz

        @Composable
        @ReadOnlyComposable
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
        @ReadOnlyComposable
        fun items(
            clearWordleCalendarItems: () -> Unit,
            onChangeWordleLang: (newLang: String) -> Unit
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
                            "pt" to stringResource(id = CoreR.string.portuguese),
                            "es" to stringResource(id = CoreR.string.spanish),
                            "fr" to stringResource(id = CoreR.string.french)
                        ),
                        onItemClick = onChangeWordleLang
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
                        onClick = clearWordleCalendarItems
                    )
                )
            )
        )
    }

    object User : SettingsScreenPageData(key = ScreenKey("user")) {
        override val stringRes: Int
            get() = CoreR.string.user

        @Composable
        @ReadOnlyComposable
        fun items(
            userIsSignedIn: Boolean,
            signOut: () -> Unit
        ) = listOf(
            Preference.PreferenceItem.TextPreference(
                title = stringResource(id = CoreR.string.sign_out),
                onClick = signOut,
                enabled = userIsSignedIn,
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.ExitToApp,
                        contentDescription = stringResource(id = CoreR.string.sign_out),
                    )
                }
            )
        )
    }
}