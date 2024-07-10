package com.infinitepower.newquiz.core.datastore.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.datastore.PreferenceRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class PreferencesDatastoreManagerTest {
    private lateinit var dataStoreManager: DataStoreManager

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @field:TempDir
    lateinit var tempFile: File

    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { tempFile.resolve("test.preferences_pb") }
    )

    @BeforeTest
    fun setup() {
        dataStoreManager = PreferencesDatastoreManager(testDataStore)
    }

    @AfterTest
    fun tearDown() {
        tempFile.delete()
    }

    @Test
    fun `getPreference returns default value`() = runTest {
        val key = stringPreferencesKey("key")
        val defaultValue = "default"

        val request = PreferenceRequest(key, defaultValue)
        val result = dataStoreManager.getPreference(request)

        assertThat(result).isEqualTo(defaultValue)
    }

    @Test
    fun `getPreference returns new data when edited`() = runTest {
        val key = stringPreferencesKey("key")
        val defaultValue = "default"

        val request = PreferenceRequest(key, defaultValue)

        val newValue = "test"
        dataStoreManager.editPreference(key, newValue)

        val result = dataStoreManager.getPreference(request)

        assertThat(result).isEqualTo(newValue)
    }

    @Test
    fun `getPreferenceFlow returns correct value`() = runTest {
        val key = stringPreferencesKey("key")
        val defaultValue = "default"

        val request = PreferenceRequest(key, defaultValue)


        dataStoreManager.getPreferenceFlow(request).test {
            assertThat(awaitItem()).isEqualTo(defaultValue)

            dataStoreManager.editPreference(key, "test")
            assertThat(awaitItem()).isEqualTo("test")
        }
    }

    @Test
    fun `test edit and clear multiple preferences`() = runTest {
        val request1 = PreferenceRequest(
            key = stringPreferencesKey("key1"),
            defaultValue = "default1"
        )
        val request2 = PreferenceRequest(
            key = stringPreferencesKey("key2"),
            defaultValue = "default2"
        )

        dataStoreManager.editPreferences(
            request1.key to "a",
            request2.key to "b"
        )

        // Check that the preferences were set
        assertThat(dataStoreManager.getPreference(request1)).isEqualTo("a")
        assertThat(dataStoreManager.getPreference(request2)).isEqualTo("b")

        dataStoreManager.clearPreferences()

        // Check that the preferences were cleared
        assertThat(dataStoreManager.getPreference(request1)).isEqualTo(request1.defaultValue)
        assertThat(dataStoreManager.getPreference(request2)).isEqualTo(request2.defaultValue)
    }
}
