package com.infinitepower.newquiz.translation

import android.os.Build
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.SettingsDataStoreManager
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleTranslatorUtil @Inject constructor(
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager
) : TranslatorUtil {
    override suspend fun isTranslatorAvailable(): Boolean {
        return isModelDownloaded()
    }

    override val availableTargetLanguageCodes: List<String> by lazy {
        TranslateLanguage.getAllLanguages().filter { languageCode ->
            // Remove English from the list of available languages
            // because we don't want to translate to English
            languageCode != TranslateLanguage.ENGLISH
        }
    }

    override val availableTargetLanguages: TranslatorTargetLanguages by lazy {
        // Associate the language code with the language name
        this.availableTargetLanguageCodes.associateWith { languageCode ->
            // Get the locale for the given language code
            val locale = Locale(languageCode)

            locale.getDisplayName(locale)
        }
    }

    override suspend fun getTargetLanguageCode(): String {
        return settingsDataStoreManager.getPreference(SettingsCommon.Translation.TargetLanguage)
    }

    private suspend fun getTranslator(): Translator {
        val targetLanguage = getTargetLanguageCode()

        if (targetLanguage.isEmpty()) {
            throw IllegalStateException("Target language is empty")
        }

        if (targetLanguage == TranslateLanguage.ENGLISH) {
            throw IllegalStateException("Target language cannot be English")
        }

        return getTranslator(targetLanguage)
    }

    private fun getTranslator(targetLanguage: String): Translator {
        val options = TranslatorOptions
            .Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(targetLanguage)
            .build()

        return Translation.getClient(options)
    }

    override suspend fun downloadModel(
        targetLanguage: String,
        requireWifi: Boolean,
        requireCharging: Boolean
    ) {
        // Get the translator
        val translator = getTranslator(targetLanguage)

        val conditions = DownloadConditions
            .Builder()
            .apply {
                if (requireWifi) requireWifi()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && requireCharging){
                    requireCharging()
                }
            }.build()

        // Download the model
        translator
            .downloadModelIfNeeded(conditions)
            .await()
    }

    override suspend fun deleteModel() {
        // Get the current target language
        val localeLanguage = getTargetLanguageCode()

        val localeModel = TranslateRemoteModel
            .Builder(localeLanguage)
            .build()

        // Delete the model
        RemoteModelManager
            .getInstance()
            .deleteDownloadedModel(localeModel)
            .await()
    }

    override suspend fun translate(text: String): String {
        val translator = getTranslator()

        return translate(text, translator)
    }

    override suspend fun translate(items: List<String>): List<String> {
        val translator = getTranslator()

        return items.map { item ->
            translate(item, translator)
        }
    }

    /**
     * Translate the given [text] using the given [translator].
     *
     * @return the translated text
     */
    private suspend fun translate(text: String, translator: Translator): String {
        return translator.translate(text).await()
    }

    override suspend fun isModelDownloaded(): Boolean {
        val localeLanguage = getTargetLanguageCode()

        // If the locale language is empty, then the model is not downloaded
        if (localeLanguage.isEmpty()) {
            return false
        }

        val localeModel = TranslateRemoteModel
            .Builder(localeLanguage)
            .build()

        return RemoteModelManager
            .getInstance()
            .isModelDownloaded(localeModel)
            .await()
    }
}