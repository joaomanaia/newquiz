package com.infinitepower.newquiz.model

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

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
}

fun String.toUiText(
    vararg args: Any
): UiText = UiText.DynamicString(value = this, args = args)
