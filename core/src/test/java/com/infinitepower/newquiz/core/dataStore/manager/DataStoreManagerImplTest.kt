package com.infinitepower.newquiz.core.dataStore.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class DataStoreManagerImplTest {
    private lateinit var dataStoreManager: DataStoreManager

    private val dataStore = mockk<DataStore<Preferences>>(relaxed = true)

    @BeforeEach
    fun setup() {
        dataStoreManager = DataStoreManagerImpl(dataStore)
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