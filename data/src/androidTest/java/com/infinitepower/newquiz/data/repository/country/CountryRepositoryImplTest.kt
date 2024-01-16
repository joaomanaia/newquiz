package com.infinitepower.newquiz.data.repository.country

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import kotlin.test.Test

@SmallTest
@RunWith(AndroidJUnit4::class)
internal class CountryRepositoryImplTest {
    @Test
    fun testGetCountries() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val remoteConfig = mockk<RemoteConfig>()

        every { remoteConfig.get<String>(RemoteConfigValue.FLAG_BASE_URL) } returns "local"

        val repository = CountryRepositoryImpl(
            context = context,
            remoteConfig = remoteConfig
        )

        val countries = repository.getAllCountries().onEach {
            println(it)
        }

        assertThat(countries).isNotEmpty()
    }
}
