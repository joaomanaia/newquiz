package com.infinitepower.newquiz.translation

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import com.google.mlkit.common.sdkinternal.MlKitContext
import com.google.mlkit.nl.translate.TranslateLanguage
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.BeforeTest

/**
 * Instrumentation tests for [GoogleTranslatorUtil].
 */
@RunWith(AndroidJunit4::class)
internal class GoogleTranslatorUtilTest {
    private val settingsDataStoreManager = mockk<DataStoreManager>()

    private lateinit var translatorUtil: GoogleTranslatorUtil

    @BeforeTest
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        MlKitContext.initializeIfNeeded(context)

        translatorUtil = GoogleTranslatorUtil(
            settingsDataStoreManager = settingsDataStoreManager
        )
    }

    @Test
    fun getTranslator_shouldReturnTranslator() = runTest {
        coEvery {
            settingsDataStoreManager.getPreference(SettingsCommon.Translation.TargetLanguage)
        } returns TranslateLanguage.FRENCH

        Truth.assertThat(translatorUtil.getTranslator()).isNotNull()
    }
}