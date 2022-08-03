package com.infinitepower.newquiz.core.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.Direction

@JvmInline
value class NavigationDrawerItemGroup(
    val id: String
) {
    init {
        require(id.isNotBlank()) { "id must not be blank" }
    }
}

sealed class NavigationDrawerItem {
    abstract val text: String
    abstract val group: NavigationDrawerItemGroup?

    data class Label(
        override val text: String,
        override val group: NavigationDrawerItemGroup? = null
    ) : NavigationDrawerItem()

    data class Item(
        override val text: String,
        override val group: NavigationDrawerItemGroup? = null,
        val icon: ImageVector,
        val direction: Direction
    ) : NavigationDrawerItem()
}