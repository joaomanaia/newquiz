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
    object MultiChoiceQuizQuestionsSize :
        PreferenceRequest<Int>(intPreferencesKey("quickQuizQuestionsSize"), 5)

    object HideOnlineCategories : PreferenceRequest<Boolean>(booleanPreferencesKey("hideOnlineCategories"), false)

    @Keep
    data class CategoryConnectionInfoBadge(
        val default: ShowCategoryConnectionInfo = ShowCategoryConnectionInfo.NONE
    ) : PreferenceRequest<String>(
        stringPreferencesKey("categoryConnectionInfoBadge"),
        default.name
    )

    object InfiniteWordleQuizLanguage : PreferenceRequest<String>(
        stringPreferencesKey("infiniteWordleQuizLanguage"),
        getInfiniteWordleDefaultLang()
    )

    object WordleInfiniteRowsLimited :
        PreferenceRequest<Boolean>(booleanPreferencesKey("wordleInfiniteRowsLimited"), false)

    object WordleInfiniteRowsLimit :
        PreferenceRequest<Int>(intPreferencesKey("wordleInfiniteRowsLimit"), 6)

    object WordleHardMode :
        PreferenceRequest<Boolean>(booleanPreferencesKey("wordleHardMode"), false)

    object WordleColorBlindMode :
        PreferenceRequest<Boolean>(booleanPreferencesKey("wordleColorBlindMode"), false)

    object WordleLetterHints :
        PreferenceRequest<Boolean>(booleanPreferencesKey("wordleLetterHints"), false)

    object GlobalAnimationsEnabled : PreferenceRequest<Boolean>(booleanPreferencesKey("animations_enabled"), true)
    object WordleAnimationsEnabled : PreferenceRequest<Boolean>(booleanPreferencesKey("wordle_animations_enabled"), true)
    object MultiChoiceAnimationsEnabled : PreferenceRequest<Boolean>(booleanPreferencesKey("multi_choice_animations_enabled"), true)

    /**
     * The translation settings.
     */
    object Translation {
        /**
         * Returns whether the translation is enabled.
         */
        object Enabled : PreferenceRequest<Boolean>(
            booleanPreferencesKey("translationEnabled"),
            false
        )

        /**
         * Returns the target language for the app translation.
         * When empty, there is no translation target language.
         */
        object TargetLanguage : PreferenceRequest<String>(
            stringPreferencesKey("translationTargetLanguage"),
            ""
        )

        /**
         * Preference to store whether the user wants to download the translation model over wifi only.
         */
        object RequireWifi : PreferenceRequest<Boolean>(
            booleanPreferencesKey("translationRequireWifi"),
            true
        )

        /**
         * Preference to store whether the user wants to download the translation model only when charging.
         */
        object RequireCharging : PreferenceRequest<Boolean>(
            booleanPreferencesKey("translationRequireCharging"),
            false
        )
    }
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
