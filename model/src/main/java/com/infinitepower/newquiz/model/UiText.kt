package com.infinitepower.newquiz.model

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    class DynamicString(
        val value: String,
        vararg val args: Any
    ) : UiText {
        override fun toString(): String = value.format(*args)
    }

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText {
        override fun toString(): String {
            return "String resource: $resId with args: ${args.joinToString()}"
        }
    }

    class PluralStringResource(
        @PluralsRes val resId: Int,
        val quantity: Int,
        vararg val args: Any
    ) : UiText {
        override fun toString(): String {
            return "Plural string resource: $resId with quantity: $quantity and args: ${args.joinToString()}"
        }
    }

    @Composable
    @ReadOnlyComposable
    fun asString(): String = when (this) {
        is DynamicString -> value.format(*args)
        is StringResource -> stringResource(resId, *args)
        is PluralStringResource -> pluralStringResource(id = resId, count = quantity, *args)
    }

    fun asString(context: Context): String = when (this) {
        is DynamicString -> value.format(*args)
        is StringResource -> context.getString(resId, *args)
        is PluralStringResource -> context.resources.getQuantityString(resId, quantity, *args)
    }
}

fun String.toUiText(
    vararg args: Any
): UiText = UiText.DynamicString(value = this, args = args)
