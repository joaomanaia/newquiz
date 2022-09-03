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
import java.util.Locale

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object SettingsCommon {
    object ShowLoginCard : PreferenceRequest<Boolean>(booleanPreferencesKey("showLoginCard"), true)

    object MultiChoiceQuizQuestionsSize :
        PreferenceRequest<Int>(intPreferencesKey("quickQuizQuestionsSize"), 5)

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
}

@Keep
data class SettingsWordleLang(
    val key: String,
    @StringRes val languageId: Int,
    @RawRes val rawListId: Int
)

val infiniteWordleSupportedLang = listOf(
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
)

private fun getInfiniteWordleDefaultLang(): String {
    val localeLanguage = Locale.getDefault().language
    val langKeys = infiniteWordleSupportedLang.map { it.key }
    return if (localeLanguage in langKeys) localeLanguage else "en"
}