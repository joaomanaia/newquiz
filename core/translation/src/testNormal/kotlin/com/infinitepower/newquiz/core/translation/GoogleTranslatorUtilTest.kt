package com.infinitepower.newquiz.core.translation

import com.google.common.truth.Truth.assertThat
import com.google.mlkit.nl.translate.TranslateLanguage
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.Locale
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for [GoogleTranslatorUtil].
 */
internal class GoogleTranslatorUtilTest {
    private val settingsDataStoreManager = mockk<DataStoreManager>()

    private lateinit var translatorUtil: GoogleTranslatorUtil

    @BeforeTest
    fun setUp() {
        translatorUtil = GoogleTranslatorUtil(
            settingsDataStoreManager = settingsDataStoreManager
        )
    }

    @Test
    fun `should return true for isTranslatorAvailable`() {
        assertThat(translatorUtil.isTranslatorAvailable).isTrue()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "en", "fr", "de"])
    fun `getTargetLanguageCode should return the target language`(
        targetLanguage: String
    ) = runTest {
        coEvery {
            settingsDataStoreManager.getPreference(SettingsCommon.Translation.TargetLanguage)
        } returns targetLanguage

        assertThat(translatorUtil.getTargetLanguageCode()).isEqualTo(targetLanguage)
    }

    @Test
    fun `availableTargetLanguageCodes should return all available languages`() {
        val expectLanguages = TranslateLanguage.getAllLanguages().filter { languageCode ->
            // Remove English from the list of available languages
            // because we don't want to translate to English
            languageCode != TranslateLanguage.ENGLISH
        }

        assertThat(translatorUtil.availableTargetLanguageCodes).containsExactlyElementsIn(
            expectLanguages
        )
    }

    @Test
    fun `availableTargetLanguages should return all available languages`() {
        val expectLanguages = TranslateLanguage.getAllLanguages().filter { languageCode ->
            // Remove English from the list of available languages
            // because we don't want to translate to English
            languageCode != TranslateLanguage.ENGLISH
        }.associateWith { languageCode ->
            // Get the locale for the given language code
            val locale = Locale(languageCode)

            locale.getDisplayName(locale)
        }

        assertThat(translatorUtil.availableTargetLanguages).containsExactlyEntriesIn(expectLanguages)
    }

    @Test
    fun `getTranslator should throw IllegalStateException when target language is empty`() =
        runTest {
            coEvery {
                settingsDataStoreManager.getPreference(SettingsCommon.Translation.TargetLanguage)
            } returns ""

            assertThrows<IllegalStateException> {
                translatorUtil.getTranslator()
            }
        }

    @Test
    fun `getTranslator should throw IllegalStateException when target language is English`() =
        runTest {
            coEvery {
                settingsDataStoreManager.getPreference(SettingsCommon.Translation.TargetLanguage)
            } returns TranslateLanguage.ENGLISH

            assertThrows<IllegalStateException> {
                translatorUtil.getTranslator()
            }
        }
}