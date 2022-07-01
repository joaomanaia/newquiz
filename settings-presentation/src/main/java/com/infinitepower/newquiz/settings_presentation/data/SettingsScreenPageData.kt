package com.infinitepower.newquiz.settings_presentation.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManagerImpl
import com.infinitepower.newquiz.settings_presentation.R
import com.infinitepower.newquiz.settings_presentation.model.Preference
import com.infinitepower.newquiz.settings_presentation.model.ScreenKey
import kotlinx.coroutines.CoroutineScope

sealed class SettingsScreenPageData(val key: ScreenKey) {
    abstract val stringRes: Int

    companion object {
        fun getPage(key: ScreenKey) = when(key) {
            MainPage.key -> MainPage
            General.key -> General
            Quiz.key -> Quiz
            else -> MainPage
        }
    }

    object MainPage : SettingsScreenPageData(key = ScreenKey("main")) {
        override val stringRes: Int
            get() = R.string.settings
    }

    object General : SettingsScreenPageData(key = ScreenKey("general")) {
        override val stringRes: Int
            get() = R.string.general

        @Composable
        fun items(
            scope: CoroutineScope,
            dataStoreManager: DataStoreManager
        ) = listOf(
            Preference.PreferenceItem.SwitchPreference(
                request = SettingsCommon.ShowLoginCard,
                title = "Show login card",
                summary = "Show login card at home screen",
                singleLineTitle = true,
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Visibility,
                        contentDescription = "Show login card",
                    )
                }
            )
        )
    }

    object Quiz : SettingsScreenPageData(key = ScreenKey("quiz")) {
        override val stringRes: Int
            get() = R.string.quiz

        @Composable
        fun items() = listOf(
            Preference.PreferenceItem.SeekBarPreference(
                request = SettingsCommon.QuickQuizQuestionsSize,
                title = "Quick quiz question size",
                singleLineTitle = true,
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.QuestionMark,
                        contentDescription = "Quick quiz question size",
                    )
                },
                valueRepresentation = { "$it" },
                valueRange = (5..20),
            )
        )
    }
}