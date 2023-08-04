package com.infinitepower.newquiz.core.common.dataStore

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
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.dataStore.manager.PreferenceRequest
import com.infinitepower.newquiz.model.DataAnalyticsConsentState
import java.util.Locale

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object SettingsCommon {
    object ShowLoginCard : PreferenceRequest<Boolean>(booleanPreferencesKey("showLoginCard"), true)

    // Data Analytics
    object DataAnalyticsCollectionEnabled : PreferenceRequest<Boolean>(booleanPreferencesKey("dataAnalyticsEnabled"), false)

    object DataAnalyticsConsent : PreferenceRequest<String>(stringPreferencesKey("dataAnalyticsConsent"), DataAnalyticsConsentState.NONE.name)

    object GeneralAnalyticsEnabled : PreferenceRequest<Boolean>(booleanPreferencesKey("generalAnalyticsEnabled"), false)

    object CrashlyticsEnabled : PreferenceRequest<Boolean>(booleanPreferencesKey("crashlyticsEnabled"), false)

    object PerformanceMonitoringEnabled : PreferenceRequest<Boolean>(booleanPreferencesKey("performanceMonitoringEnabled"), false)

    object MultiChoiceQuizQuestionsSize :
        PreferenceRequest<Int>(intPreferencesKey("quickQuizQuestionsSize"), 5)

    object HideOnlineCategories : PreferenceRequest<Boolean>(booleanPreferencesKey("hideOnlineCategories"), false)

    object MultiChoiceQuiz {
        object TranslationEnabled : PreferenceRequest<Boolean>(
            booleanPreferencesKey("multiChoiceQuizTranslationEnabled"),
            false
        )
    }

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
        languageId = R.string.english,
        rawListId = R.raw.wordle_list
    ),
    SettingsWordleLang(
        key = "pt",
        languageId = R.string.portuguese,
        rawListId = R.raw.wordle_list_pt
    ),
    SettingsWordleLang(
        key = "es",
        languageId = R.string.spanish,
        rawListId = R.raw.wordle_list_es
    ),
    SettingsWordleLang(
        key = "fr",
        languageId = R.string.french,
        rawListId = R.raw.wordle_list_fr
    )
)

private fun getInfiniteWordleDefaultLang(): String {
    val localeLanguage = Locale.getDefault().language
    val langKeys = textWordleSupportedLang.map { it.key }
    return if (localeLanguage in langKeys) localeLanguage else "en"
}