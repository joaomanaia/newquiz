package com.infinitepower.newquiz.core.translation

import kotlinx.coroutines.flow.Flow

interface TranslatorUtil {
    /**
     * @return true if the translator is available, false otherwise
     */
    val isTranslatorAvailable: Boolean

    /**
     * @return true if the model is downloaded, false otherwise
     */
    suspend fun isModelDownloaded(): Boolean

    /**
     * @return the list of available language codes
     */
    val availableTargetLanguageCodes: List<String>

    /**
     * @return the list of available [TranslatorTargetLanguages] for the target languages.
     * The key is the language code, the value is the language name.
     */
    val availableTargetLanguages: TranslatorTargetLanguages

    /**
     * @return the current target language code
     */
    suspend fun getTargetLanguageCode(): String

    /**
     * Downloads the translation model for the current target language.
     */
    suspend fun downloadModel(
        targetLanguage: String,
        requireWifi: Boolean,
        requireCharging: Boolean
    ): Flow<TranslatorModelState>

    /**
     * Deletes the current translation model
     */
    suspend fun deleteModel()

    /**
     * Translates the given [text] to the current target language.
     */
    suspend fun translate(text: String): String

    /**
     * Translates the given [items] to the current target language.
     */
    suspend fun translate(items: List<String>): List<String>
}
