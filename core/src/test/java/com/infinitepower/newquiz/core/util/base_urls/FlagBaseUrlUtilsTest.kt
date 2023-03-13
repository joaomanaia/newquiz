package com.infinitepower.newquiz.core.util.base_urls

import com.google.common.truth.Truth.assertThat
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class FlagBaseUrlUtilsTest {
    private val remoteConfig = mockk<FirebaseRemoteConfig>()

    @Test
    fun `getFlagBaseUrl should replace code with countryCode`() {
        // Mock the remote config response
        val remoteConfigBaseUrl = "https://www.example.com/flags/%code%"
        every { remoteConfig.getString("flag_base_url") } returns remoteConfigBaseUrl

        // Call the function with a specific country code
        val countryCode = "us"
        val expectedUrl = "https://www.example.com/flags/us"
        val actualUrl = remoteConfig.getFlagBaseUrl(countryCode)

        // Verify that the function returns the expected URL
        assertThat(actualUrl).isEqualTo(expectedUrl)
    }

    @Test
    fun `getFlagBaseUrl should return the original string if code is not present`() {
        // Mock the remote config response
        val remoteConfigBaseUrl = "https://www.example.com/flags"
        every { remoteConfig.getString("flag_base_url") } returns remoteConfigBaseUrl

        // Call the function with a specific country code
        val countryCode = "us"
        val expectedUrl = "https://www.example.com/flags"
        val actualUrl = remoteConfig.getFlagBaseUrl(countryCode)

        // Verify that the function returns the original string
        assertThat(actualUrl).isEqualTo(expectedUrl)
    }

    @Test
    fun `getFlagBaseUrl should replace code lowercase with countryCode`() {
        // Mock the remote config response
        val remoteConfigBaseUrl = "https://www.example.com/flags/%code%"
        every { remoteConfig.getString("flag_base_url") } returns remoteConfigBaseUrl

        // Call the function with a specific country code
        val countryCode = "US"
        val expectedUrl = "https://www.example.com/flags/us"
        val actualUrl = remoteConfig.getFlagBaseUrl(countryCode)

        // Verify that the function returns the expected URL
        assertThat(actualUrl).isEqualTo(expectedUrl)
    }
}