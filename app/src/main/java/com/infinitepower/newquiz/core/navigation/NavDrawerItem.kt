package com.infinitepower.newquiz.core.navigation

import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.Direction

sealed class NavDrawerItem {
    @get:StringRes
    abstract val text: Int

    data class Label(
        @StringRes override val text: Int
    ) : NavDrawerItem()

    data class Item(
        @StringRes override val text: Int,
        val icon: ImageVector,
        val badge: NavDrawerBadgeItem? = null,
        val direction: Direction
    ) : NavDrawerItem()
}

@Keep
data class NavDrawerBadgeItem(
    val value: Int,
    val description: String
)