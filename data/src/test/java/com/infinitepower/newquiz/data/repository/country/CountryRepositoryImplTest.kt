package com.infinitepower.newquiz.data.repository.country

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test

/**
 * Tests for [CountryRepositoryImpl]
 */
internal class CountryRepositoryImplTest {
    @Test
    fun `test get countries`() = runTest {
        val context = mockk<Context>()

        val countriesEntities = List(10) {
            CountryEntity(
                countryCode = "TEST_$it",
                countryName = "Test Country $it",
                capital = "Test Capital $it",
                continent = "Europe",
                difficulty = "easy"
            )
        }

        val inputStream = Json
            .encodeToString(countriesEntities)
            .byteInputStream()

        coEvery {
            context.resources.openRawResource(any())
        } returns inputStream

        val remoteConfig = mockk<RemoteConfig>()
        every { remoteConfig.get(RemoteConfigValue.FLAG_BASE_URL) } returns "https://example.com/%code%.png"

        val countryRepository = CountryRepositoryImpl(
            context = context,
            remoteConfig = remoteConfig
        )

        val allCountries = countryRepository.getAllCountries()

        assertThat(allCountries).hasSize(countriesEntities.size)

        coVerify(exactly = 1) { context.resources.openRawResource(any()) }
        coVerify(exactly = 1) { remoteConfig.get(RemoteConfigValue.FLAG_BASE_URL) }

        confirmVerified(context, remoteConfig)
    }
}
