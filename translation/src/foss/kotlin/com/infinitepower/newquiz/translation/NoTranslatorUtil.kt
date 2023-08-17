package com.infinitepower.newquiz.translation

import javax.inject.Inject
import javax.inject.Singleton

class NoTranslatorAvailableException : RuntimeException("No translator available")

@Singleton
class NoTranslatorUtil @Inject constructor() : TranslatorUtil {
    override suspend fun isTranslatorAvailable(): Boolean = false

    override val availableTargetLanguageCodes: List<String> = emptyList()

    override val availableTargetLanguages: TranslatorTargetLanguages = emptyMap()

    override suspend fun getTargetLanguageCode(): String {
        throw NoTranslatorAvailableException()
    }

    override suspend fun downloadModel(
        targetLanguage: String,
        requireWifi: Boolean,
        requireCharging: Boolean
    ) {
        throw NoTranslatorAvailableException()
    }

    override suspend fun deleteModel() {
        throw NoTranslatorAvailableException()
    }

    override suspend fun translate(text: String): String {
        throw NoTranslatorAvailableException()
    }

    override suspend fun translate(items: List<String>): List<String> {
        throw NoTranslatorAvailableException()
    }

    override suspend fun isModelDownloaded(): Boolean = false
}