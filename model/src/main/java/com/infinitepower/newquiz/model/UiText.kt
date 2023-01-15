package com.infinitepower.newquiz.model

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class DynamicString(val value: String) : UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    @Composable
    @ReadOnlyComposable
    fun asString(): String = when (this) {
        is DynamicString -> value
        is StringResource -> stringResource(resId, *args)
    }

    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(resId, *args)
    }

    override fun toString(): String = when (this) {
        is DynamicString -> value
        is StringResource -> "String id: $resId, with args: $args"
    }
}