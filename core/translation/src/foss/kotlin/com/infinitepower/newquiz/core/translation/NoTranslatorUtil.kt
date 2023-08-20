package com.infinitepower.newquiz.core.translation

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

class NoTranslatorAvailableException : RuntimeException("No translator available")

/**
 * A [TranslatorUtil] implementation that does nothing.
 * This is used when the translator is not available.
 * The functions will throw [NoTranslatorAvailableException] when called.
 */
@Singleton
class NoTranslatorUtil @Inject constructor() : TranslatorUtil {
    override val isTranslatorAvailable: Boolean = false

    override suspend fun isModelDownloaded(): Boolean = false

    override val availableTargetLanguageCodes: List<String> = emptyList()

    override val availableTargetLanguages: TranslatorTargetLanguages = emptyMap()

    override suspend fun getTargetLanguageCode(): String {
        throw NoTranslatorAvailableException()
    }

    override suspend fun downloadModel(
        targetLanguage: String,
        requireWifi: Boolean,
        requireCharging: Boolean
    ): Flow<TranslatorModelState> {
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
}