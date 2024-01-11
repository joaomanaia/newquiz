package com.infinitepower.newquiz.core.datastore.common

import android.content.Context
import androidx.annotation.Keep
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.infinitepower.newquiz.core.datastore.PreferenceRequest
import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo
import com.infinitepower.newquiz.core.R as CoreR
import java.util.Locale

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object SettingsCommon {
    object MultiChoiceQuizQuestionsSize : PreferenceRequest<Int>(
        key = intPreferencesKey("quickQuizQuestionsSize"),
        defaultValue = 5
    )

    object HideOnlineCategories : PreferenceRequest<Boolean>(
        key = booleanPreferencesKey("hideOnlineCategories"),
        defaultValue = false
    )

    @Keep
    data class CategoryConnectionInfoBadge(
        val default: ShowCategoryConnectionInfo = ShowCategoryConnectionInfo.NONE
    ) : PreferenceRequest<String>(
        key = stringPreferencesKey("categoryConnectionInfoBadge"),
        defaultValue = default.name
    )

    object InfiniteWordleQuizLanguage : PreferenceRequest<String>(
        key = stringPreferencesKey("infiniteWordleQuizLanguage"),
        defaultValue = getInfiniteWordleDefaultLang()
    )

    object WordleInfiniteRowsLimited : PreferenceRequest<Boolean>(
        key = booleanPreferencesKey("wordleInfiniteRowsLimited"),
        defaultValue = false
    )

    object WordleInfiniteRowsLimit : PreferenceRequest<Int>(
        key = intPreferencesKey("wordleInfiniteRowsLimit"),
        defaultValue = 6
    )

    object WordleHardMode : PreferenceRequest<Boolean>(
        key = booleanPreferencesKey("wordleHardMode"),
        defaultValue = false
    )

    object WordleColorBlindMode : PreferenceRequest<Boolean>(
        key = booleanPreferencesKey("wordleColorBlindMode"),
        defaultValue = false
    )

    object WordleLetterHints : PreferenceRequest<Boolean>(
        key = booleanPreferencesKey("wordleLetterHints"),
        defaultValue = false
    )

    object GlobalAnimationsEnabled : PreferenceRequest<Boolean>(
        key = booleanPreferencesKey("animations_enabled"),
        defaultValue = true
    )

    object WordleAnimationsEnabled : PreferenceRequest<Boolean>(
        key = booleanPreferencesKey("wordle_animations_enabled"),
        defaultValue = true
    )

    object MultiChoiceAnimationsEnabled : PreferenceRequest<Boolean>(
        key = booleanPreferencesKey("multi_choice_animations_enabled"),
        defaultValue = true
    )

    object MazeAutoScrollToCurrentItem : PreferenceRequest<Boolean>(
        key = booleanPreferencesKey("mazeAutoScrollToCurrentItem"),
        defaultValue = true
    )

    object TemperatureUnit : PreferenceRequest<String>(
        key = stringPreferencesKey("temperatureUnit"),
        defaultValue = ""
    )

    object DistanceUnitType : PreferenceRequest<String>(
        key = stringPreferencesKey("distanceUnitType"),
        defaultValue = ""
    )
}

@Keep
data class SettingsWordleLang(
    val key: String,
    @StringRes val languageId: Int,
    @RawRes val rawListId: Int
)

val textWordleSupportedLang = listOf(
    SettingsWordleLang(
        key = "en",
        languageId = CoreR.string.english,
        rawListId = CoreR.raw.wordle_list
    ),
    SettingsWordleLang(
        key = "pt",
        languageId = CoreR.string.portuguese,
        rawListId = CoreR.raw.wordle_list_pt
    ),
    SettingsWordleLang(
        key = "es",
        languageId = CoreR.string.spanish,
        rawListId = CoreR.raw.wordle_list_es
    ),
    SettingsWordleLang(
        key = "fr",
        languageId = CoreR.string.french,
        rawListId = CoreR.raw.wordle_list_fr
    )
)

private fun getInfiniteWordleDefaultLang(): String {
    val localeLanguage = Locale.getDefault().language
    val langKeys = textWordleSupportedLang.map { it.key }
    return if (localeLanguage in langKeys) localeLanguage else "en"
}
