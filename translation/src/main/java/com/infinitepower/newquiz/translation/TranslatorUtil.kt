package com.infinitepower.newquiz.translation

interface TranslatorUtil {
    suspend fun isTranslatorAvailable(): Boolean

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
    )

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

    suspend fun isModelDownloaded(): Boolean
}