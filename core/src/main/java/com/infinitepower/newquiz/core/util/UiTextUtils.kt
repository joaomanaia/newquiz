package com.infinitepower.newquiz.core.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.infinitepower.newquiz.model.UiText

@Composable
@ReadOnlyComposable
fun UiText.asString(): String {
    val argsFormatted = formatArgs(*args)

    return when (this) {
        is UiText.DynamicString -> value.format(*argsFormatted)
        is UiText.StringResource -> stringResource(resId, *argsFormatted)
        is UiText.PluralStringResource -> pluralStringResource(id = resId, count = quantity, *argsFormatted)
    }
}

fun UiText.asString(context: Context): String {
    val argsFormatted = context.formatArgs(*args)

    return when (this) {
        is UiText.DynamicString -> value.format(*argsFormatted)
        is UiText.StringResource -> context.getString(resId, *argsFormatted)
        is UiText.PluralStringResource -> context.resources.getQuantityString(resId, quantity, *argsFormatted)
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
