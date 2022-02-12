package com.infinitepower.footballagent.core.util.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Suppress("INLINE_CLASS_DEPRECATED")
@Immutable
internal inline class Strings private constructor(@Suppress("unused") private val value: Int) {
    companion object {
        val NavigationMenu = Strings(0)
        val CloseDrawer = Strings(1)
        val CloseSheet = Strings(2)
        val DefaultErrorMessage = Strings(3)
        val ExposedDropdownMenu = Strings(4)
    }
}