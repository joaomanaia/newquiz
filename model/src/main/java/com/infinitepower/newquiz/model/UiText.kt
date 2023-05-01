package com.infinitepower.newquiz.model

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    val args: Array<out Any>

    override fun toString(): String

    class DynamicString(
        val value: String,
        override vararg val args: Any
    ) : UiText {
        override fun toString(): String = value.format(*args)
    }

    class StringResource(
        @StringRes val resId: Int,
        override vararg val args: Any
    ) : UiText {
        override fun toString(): String {
            return "String resource: $resId with args: ${args.joinToString()}"
        }
    }

    class PluralStringResource(
        @PluralsRes val resId: Int,
        val quantity: Int,
        override vararg val args: Any
    ) : UiText {
        override fun toString(): String {
            return "Plural string resource: $resId with quantity: $quantity and args: ${args.joinToString()}"
        }
    }

    @Composable
    @ReadOnlyComposable
    fun asString(): String {
        val argsFormatted = formatArgs(*args)

        return when (this) {
            is DynamicString -> value.format(*argsFormatted)
            is StringResource -> stringResource(resId, *argsFormatted)
            is PluralStringResource -> pluralStringResource(id = resId, count = quantity, *argsFormatted)
        }
    }

    fun asString(context: Context): String {
        val argsFormatted = context.formatArgs(*args)

        return when (this) {
            is DynamicString -> value.format(*argsFormatted)
            is StringResource -> context.getString(resId, *argsFormatted)
            is PluralStringResource -> context.resources.getQuantityString(resId, quantity, *argsFormatted)
        }
    }

    @Composable
    @ReadOnlyComposable
    private fun formatArgs(
        vararg args: Any
    ): Array<out Any> = args.map { arg ->
        when (arg) {
            is UiText -> arg.asString()
            else -> arg
        }
    }.toTypedArray()

    private fun Context.formatArgs(
        vararg args: Any
    ): Array<out Any> = args.map { arg ->
        when (arg) {
            is UiText -> arg.asString(this)
            else -> arg
        }
    }.toTypedArray()
}

fun String.toUiText(
    vararg args: Any
): UiText = UiText.DynamicString(value = this, args = args)
