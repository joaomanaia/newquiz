package com.infinitepower.newquiz.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Compare
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.ListAlt
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.infinitepower.newquiz.comparison_quiz.destinations.ComparisonQuizListScreenDestination
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.navigation.NavigationItem
import com.infinitepower.newquiz.core.navigation.NavDrawerItemGroup
import com.infinitepower.newquiz.core.navigation.ScreenType
import com.infinitepower.newquiz.home_presentation.destinations.HomeScreenDestination
import com.infinitepower.newquiz.math_quiz.list.destinations.MathQuizListScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizListScreenDestination
import com.infinitepower.newquiz.online_services.ui.profile.destinations.ProfileScreenDestination
import com.infinitepower.newquiz.settings_presentation.destinations.SettingsScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleListScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState

internal val navigationItems = listOf(
    NavigationItem.Item(
        text = R.string.home,
        icon = Icons.Rounded.Home,
        direction = HomeScreenDestination,
        primary = true
    ),
    NavigationItem.Item(
        text = R.string.multi_choice_quiz,
        icon = Icons.Rounded.ListAlt,
        direction = MultiChoiceQuizListScreenDestination,
        primary = true
    ),
    NavigationItem.Item(
        text = R.string.wordle,
        icon = Icons.Rounded.Password,
        direction = WordleListScreenDestination,
        primary = true
    ),
    NavigationItem.Item(
        text = R.string.comparison_quiz,
        icon = Icons.Rounded.Compare,
        direction = ComparisonQuizListScreenDestination,
        primary = true
    ),
    NavigationItem.Item(
        text = R.string.math_quiz,
        icon = Icons.Rounded.Numbers,
        direction = MathQuizListScreenDestination,
        primary = true
    ),
    NavigationItem.Label(
        text = R.string.online,
        group = NavDrawerItemGroup("online")
    ),
    NavigationItem.Item(
        text = R.string.profile,
        icon = Icons.Rounded.Person,
        direction = ProfileScreenDestination,
        group = NavDrawerItemGroup("online")
    ),
    NavigationItem.Label(text = R.string.other),
    NavigationItem.Item(
        text = R.string.settings,
        icon = Icons.Rounded.Settings,
        direction = SettingsScreenDestination(),
        screenType = ScreenType.GAME
    )
)

private fun getNavigationItemBy(route: DestinationSpec<*>?) = navigationItems
    .filterIsInstance<NavigationItem.Item>()
    .find { item -> item.direction == route }

@Composable
@ExperimentalMaterial3Api
internal fun NavigationContainer(
    navController: NavController,
    windowWidthSize: WindowWidthSizeClass,
    isSignedIn: Boolean,
    content: @Composable (PaddingValues) -> Unit
) {
    val destination by navController.currentDestinationAsState()

    val selectedItem = remember(destination) { getNavigationItemBy(destination) }

    val items = remember(isSignedIn) {
        if (isSignedIn) {
            navigationItems
        } else {
            navigationItems.filterNot { it.group == NavDrawerItemGroup("online") }
        }
    }

    val navigationVisible = selectedItem != null && selectedItem.screenType == ScreenType.NORMAL

    if (navigationVisible) {
        when (windowWidthSize) {
            WindowWidthSizeClass.Compact -> CompactContainer(
                navController = navController,
                navigationItems = items,
                selectedItem = selectedItem,
                content = content
            )
            WindowWidthSizeClass.Medium -> MediumContainer(
                navController = navController,
                navigationItems = items,
                selectedItem = selectedItem,
                content = content
            )
            WindowWidthSizeClass.Expanded -> ExpandedContainer(
                navController = navController,
                navigationItems = items,
                selectedItem = selectedItem,
                content = content
            )
        }
    } else {
        Scaffold(
            content = content
        )
    }
}