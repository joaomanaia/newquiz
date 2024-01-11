package com.infinitepower.newquiz.core.datastore.common

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.infinitepower.newquiz.core.datastore.PreferenceRequest

object TranslationCommon {
    /**
     * Returns whether the translation is enabled.
     */
    object Enabled : PreferenceRequest<Boolean>(
        key = booleanPreferencesKey("translationEnabled"),
        defaultValue = false
    )

    /**
     * Returns the target language for the app translation.
     * When empty, there is no translation target language.
     */
    object TargetLanguage : PreferenceRequest<String>(
        key = stringPreferencesKey("translationTargetLanguage"),
        defaultValue = ""
    )

    /**
     * Preference to store whether the user wants to download the translation model over wifi only.
     */
    object RequireWifi : PreferenceRequest<Boolean>(
        key = booleanPreferencesKey("translationRequireWifi"),
        defaultValue = true
    )

    /**
     * Preference to store whether the user wants to download the translation model only when charging.
     */
    object RequireCharging : PreferenceRequest<Boolean>(
        key = booleanPreferencesKey("translationRequireCharging"),
        defaultValue = false
    )
}