package com.infinitepower.newquiz.settings_presentation.data

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Animation
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.ClearAll
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.ListAlt
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.AppNameWithLogo
import com.infinitepower.newquiz.core.util.analytics.AnalyticsUtils
import com.infinitepower.newquiz.settings_presentation.components.other.AboutAndHelpButtons
import com.infinitepower.newquiz.settings_presentation.model.Preference
import com.infinitepower.newquiz.settings_presentation.model.ScreenKey
import com.infinitepower.newquiz.translation_dynamic_feature.TranslatorUtil.TranslatorModelState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.infinitepower.newquiz.core.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
sealed class SettingsScreenPageData(val key: ScreenKey) {
    abstract val stringRes: Int

    companion object {
        fun getPage(key: ScreenKey) = when (key) {
            MainPage.key -> MainPage
            General.key -> General
            MultiChoiceQuiz.key -> MultiChoiceQuiz
            Wordle.key -> Wordle
            User.key -> User
            AboutAndHelp.key -> AboutAndHelp
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
            Preference.PreferenceItem.NavigationButton(
                title = stringResource(id = AboutAndHelp.stringRes),
                iconImageVector = Icons.Rounded.Help,
                screenKey = AboutAndHelp.key,
                onClick = { navigateToScreen(AboutAndHelp.key) },
                itemSelected = currentScreenKey == AboutAndHelp.key,
                screenExpanded = screenExpanded,
                inMainPage = inMainPage
            )
        )
    }

    object General : SettingsScreenPageData(key = ScreenKey("general")) {
        override val stringRes: Int
            get() = CoreR.string.general

        @Composable
        @ReadOnlyComposable
        fun items(
            scope: CoroutineScope,
            dataStoreManager: DataStoreManager,
            enableLoggingAnalytics: (enabled: Boolean) -> Unit,
            cleanRecentCategories: () -> Unit
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
            Preference.PreferenceItem.TextPreference(
                title = stringResource(id = CoreR.string.clear_recent_categories),
                summary = stringResource(id = CoreR.string.clear_recent_categories_description),
                onClick = cleanRecentCategories,
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.ClearAll,
                        contentDescription = stringResource(id = CoreR.string.clear_recent_categories),
                    )
                }
            ),
            Preference.PreferenceGroup(
                title = stringResource(id = CoreR.string.animations),
                preferenceItems = listOf(
                    Preference.PreferenceItem.SwitchPreference(
                        request = SettingsCommon.GlobalAnimationsEnabled,
                        title = stringResource(id = CoreR.string.animations_enabled),
                        summary = stringResource(id = CoreR.string.global_animations_enabled),
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Animation,
                                contentDescription = stringResource(id = CoreR.string.animations_enabled)
                            )
                        }
                    ),
                    Preference.PreferenceItem.SwitchPreference(
                        request = SettingsCommon.WordleAnimationsEnabled,
                        title = stringResource(id = CoreR.string.wordle_animations_enabled),
                        summary = stringResource(id = CoreR.string.wordle_animations_enabled_description),
                        dependency = listOf(SettingsCommon.GlobalAnimationsEnabled)
                    ),
                    Preference.PreferenceItem.SwitchPreference(
                        request = SettingsCommon.MultiChoiceAnimationsEnabled,
                        title = stringResource(id = CoreR.string.multi_choice_animations_enabled),
                        summary = stringResource(id = CoreR.string.multi_choice_animations_enabled_description),
                        dependency = listOf(SettingsCommon.GlobalAnimationsEnabled)
                    )
                )
            ),
            Preference.PreferenceGroup(
                title = stringResource(id = CoreR.string.analytics),
                preferenceItems = listOf(
                    // Global analytics dependency
                    Preference.PreferenceItem.SwitchPreference(
                        request = SettingsCommon.DataAnalyticsCollectionEnabled,
                        title = stringResource(id = CoreR.string.analytics_collection_enabled),
                        onCheckChange = enableLoggingAnalytics,
                        primarySwitch = true
                    ),
                    Preference.PreferenceItem.SwitchPreference(
                        request = SettingsCommon.GeneralAnalyticsEnabled,
                        title = stringResource(id = CoreR.string.general_analytics_enabled),
                        summary = stringResource(id = CoreR.string.general_analytics_description_enabled),
                        dependency = listOf(SettingsCommon.DataAnalyticsCollectionEnabled),
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Analytics,
                                contentDescription = stringResource(id = CoreR.string.general_analytics_enabled)
                            )
                        },
                        onCheckChange = { enabled -> AnalyticsUtils.enableGeneralAnalytics(enabled) }
                    ),
                    Preference.PreferenceItem.SwitchPreference(
                        request = SettingsCommon.CrashlyticsEnabled,
                        title = stringResource(id = CoreR.string.crash_analytics_enabled),
                        summary = stringResource(id = CoreR.string.crash_analytics_description_enabled),
                        dependency = listOf(SettingsCommon.DataAnalyticsCollectionEnabled),
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.BugReport,
                                contentDescription = stringResource(id = CoreR.string.crash_analytics_enabled)
                            )
                        },
                        onCheckChange = { enabled -> AnalyticsUtils.enableCrashlytics(enabled) }
                    ),
                    Preference.PreferenceItem.SwitchPreference(
                        request = SettingsCommon.PerformanceMonitoringEnabled,
                        title = stringResource(id = CoreR.string.performance_monitoring_enabled),
                        summary = stringResource(id = CoreR.string.performance_monitoring_description_enabled),
                        dependency = listOf(SettingsCommon.DataAnalyticsCollectionEnabled),
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.MonitorHeart,
                                contentDescription = stringResource(id = CoreR.string.performance_monitoring_enabled)
                            )
                        },
                        onCheckChange = { enabled -> AnalyticsUtils.enablePerformanceMonitoring(enabled) }
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

    object AboutAndHelp : SettingsScreenPageData(key = ScreenKey("about_and_help")) {
        override val stringRes: Int
            get() = CoreR.string.about_and_help

        @Composable
        @ReadOnlyComposable
        fun items() = listOf(
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
}