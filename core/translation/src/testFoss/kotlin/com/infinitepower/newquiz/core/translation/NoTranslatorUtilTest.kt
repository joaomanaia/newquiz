package com.infinitepower.newquiz.core.translation

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for [NoTranslatorUtil].
 */
internal class NoTranslatorUtilTest {
    private lateinit var translatorUtil: TranslatorUtil

    @BeforeTest
    fun setUp() {
        translatorUtil = NoTranslatorUtil()
    }

    @Test
    fun `translator should not be available and downloaded`() {
        runTest {
            assertThat(translatorUtil.isModelDownloaded()).isFalse()
            assertThat(translatorUtil.isReadyToTranslate()).isFalse()
        }
    }

    @Test
    fun `should return empty list of available target language codes and languages`() {
        assertThat(translatorUtil.availableTargetLanguageCodes).isEmpty()
        assertThat(translatorUtil.availableTargetLanguages).isEmpty()
    }

    @Test
    fun `should throw exception when getting target language code`() {
        assertThrows<NoTranslatorAvailableException> {
            runTest {
                translatorUtil.getTargetLanguageCode()
            }
        }
    }

    @Test
    fun `should throw exception when downloading model`() {
        assertThrows<NoTranslatorAvailableException> {
            runTest {
                translatorUtil.downloadModel(
                    targetLanguage = "en",
                    requireWifi = false,
                    requireCharging = false
                )
            }
        }
    }

    @Test
    fun `should throw exception when deleting model`() {
        assertThrows<NoTranslatorAvailableException> {
            runTest {
                translatorUtil.deleteModel()
            }
        }
    }

    @Test
    fun `should throw exception when translating text`() {
        assertThrows<NoTranslatorAvailableException> {
            runTest {
                translatorUtil.translate("Hello")
            }
        }
    }

    @Test
    fun `should throw exception when translating list of items`() {
        assertThrows<NoTranslatorAvailableException> {
            runTest {
                translatorUtil.translate(listOf("Hello", "World"))
            }
        }
    }
}
