package com.infinitepower.newquiz.core.navigation

import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.Direction

sealed class NavigationItem {
    @get:StringRes
    abstract val text: Int

    abstract val group: NavDrawerItemGroup?

    data class Label(
        @StringRes override val text: Int,
        override val group: NavDrawerItemGroup? = null
    ) : NavigationItem()

    /**
     * @param primary items to navigation bar
     */
    data class Item(
        @StringRes override val text: Int,
        override val group: NavDrawerItemGroup? = null,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector? = null,
        val badge: NavDrawerBadgeItem? = null,
        val direction: Direction,
        val primary: Boolean = false,
        val screenType: ScreenType = ScreenType.NORMAL
    ) : NavigationItem() {
        fun getIcon(selected: Boolean): ImageVector {
            return if (selected || unselectedIcon == null) selectedIcon else unselectedIcon
        }
    }
}

enum class ScreenType {
    /** When using this type navigation items will be visible and have a top bar */
    NORMAL,

    /** When using this type all navigation items will be invisible and have no top bar */
    NAVIGATION_HIDDEN
}

@JvmInline
value class NavDrawerItemGroup(val key: String)

@Keep
data class NavDrawerBadgeItem(
    val value: Int,
    val description: String
)