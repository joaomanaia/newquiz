package com.infinitepower.newquiz.model.category

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * Tests for [ShowCategoryConnectionInfo]
 */
internal class ShowCategoryConnectionInfoTest {
    @ParameterizedTest
    @CsvSource(
        "NONE, false, false",
        "NONE, true, false",
        "BOTH, false, true",
        "BOTH, true, true",
        "REQUIRE_CONNECTION, false, false",
        "REQUIRE_CONNECTION, true, true",
        "DONT_REQUIRE_CONNECTION, false, true",
        "DONT_REQUIRE_CONNECTION, true, false",
    )
    fun test_shouldShowBadge(
        showCategoryConnectionInfo: ShowCategoryConnectionInfo,
        requireInternetConnection: Boolean,
        expected: Boolean,
    ) {
        assertThat(showCategoryConnectionInfo.shouldShowBadge(requireInternetConnection)).isEqualTo(expected)
    }
}
