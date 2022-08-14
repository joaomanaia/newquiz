package com.infinitepower.newquiz.core.navigation

import androidx.annotation.Keep
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.Direction

sealed class NavDrawerItem {
    abstract val text: String

    data class Label(
        override val text: String,
    ) : NavDrawerItem()

    data class Item(
        override val text: String,
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