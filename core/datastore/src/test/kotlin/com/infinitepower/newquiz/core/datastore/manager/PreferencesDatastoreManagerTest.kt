package com.infinitepower.newquiz.core.datastore.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.datastore.PreferenceRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PreferencesDatastoreManagerTest {
    private lateinit var dataStoreManager: DataStoreManager

    private val dataStore = mockk<DataStore<Preferences>>(relaxed = true)

    @BeforeEach
    fun setup() {
        dataStoreManager = PreferencesDatastoreManager(dataStore)
    }

    @Test
    fun `getPreference returns default value when key is not found`() = runTest {
        val key = stringPreferencesKey("key")
        val defaultValue = "default"
        every { dataStore.data } returns flowOf(emptyPreferences())

        val request = PreferenceRequest(key, defaultValue)
        val result = dataStoreManager.getPreference(request)

        verify { dataStore.data }

        assertThat(result).isEqualTo(defaultValue)
    }
}