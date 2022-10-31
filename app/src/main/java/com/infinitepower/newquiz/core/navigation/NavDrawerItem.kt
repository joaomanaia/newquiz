package com.infinitepower.newquiz.core.navigation

import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.Direction

sealed class NavDrawerItem {
    @get:StringRes
    abstract val text: Int

    abstract val group: NavDrawerItemGroup?

    data class Label(
        @StringRes override val text: Int,
        override val group: NavDrawerItemGroup? = null
    ) : NavDrawerItem()

    data class Item(
        @StringRes override val text: Int,
        override val group: NavDrawerItemGroup? = null,
        val icon: ImageVector,
        val badge: NavDrawerBadgeItem? = null,
        val direction: Direction
    ) : NavDrawerItem()
}

@JvmInline
value class NavDrawerItemGroup(val key: String)

@Keep
data class NavDrawerBadgeItem(
    val value: Int,
    val description: String
)