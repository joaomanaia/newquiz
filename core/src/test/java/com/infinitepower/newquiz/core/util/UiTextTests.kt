package com.infinitepower.newquiz.core.util

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.toUiText
import io.mockk.mockk
import org.junit.jupiter.api.Test

/**
 * Tests for [UiText].
 */
internal class UiTextTests {
    @Test
    fun `String#toUiText() should return a UiText`() {
        val uiText = "Play %d Questions".toUiText(5)

        val context = mockk<Context>()

        assertThat(uiText).isInstanceOf(UiText.DynamicString::class.java)
        assertThat(uiText.asString(context)).isEqualTo("Play 5 Questions")
    }

    @Test
    fun `String#toUiText() should return a UiText with multiple arguments`() {
        val uiText = "Play %d Questions in %s".toUiText(5, "English")

        val context = mockk<Context>()

        assertThat(uiText).isInstanceOf(UiText.DynamicString::class.java)
        assertThat(uiText.asString(context)).isEqualTo("Play 5 Questions in English")
    }

    @Test
    fun `String#toUiText() should return a UiText with no arguments`() {
        val uiText = "Play Questions".toUiText()

        val context = mockk<Context>()

        assertThat(uiText).isInstanceOf(UiText.DynamicString::class.java)
        assertThat(uiText.asString(context)).isEqualTo("Play Questions")
    }
}