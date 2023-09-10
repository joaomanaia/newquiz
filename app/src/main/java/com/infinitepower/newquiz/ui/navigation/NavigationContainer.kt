package com.infinitepower.newquiz.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Compare
import androidx.compose.material.icons.outlined.ViewModule
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.ListAlt
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material.icons.rounded.ViewModule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.infinitepower.newquiz.comparison_quiz.destinations.ComparisonQuizListScreenDestination
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.navigation.NavDrawerBadgeItem
import com.infinitepower.newquiz.core.navigation.NavDrawerItemGroup
import com.infinitepower.newquiz.core.navigation.NavigationItem
import com.infinitepower.newquiz.core.navigation.ScreenType
import com.infinitepower.newquiz.daily_challenge.destinations.DailyChallengeScreenDestination
import com.infinitepower.newquiz.maze_quiz.destinations.MazeScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizListScreenDestination
import com.infinitepower.newquiz.online_services.ui.destinations.ProfileScreenDestination
import com.infinitepower.newquiz.settings_presentation.destinations.SettingsScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleListScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState

internal fun getNavigationItems(
    dailyChallengeClaimCount: Int = 0
) = listOf(
    NavigationItem.Item(
        text = R.string.multi_choice_quiz,
        selectedIcon = Icons.Rounded.ListAlt,
        unselectedIcon = Icons.Rounded.List,
        direction = MultiChoiceQuizListScreenDestination,
        primary = true
    ),
    NavigationItem.Item(
        text = R.string.wordle,
        selectedIcon = Icons.Rounded.ViewModule,
        unselectedIcon = Icons.Outlined.ViewModule,
        direction = WordleListScreenDestination,
        primary = true
    ),
    NavigationItem.Item(
        text = R.string.comparison_quiz,
        selectedIcon = Icons.Rounded.Image,
        unselectedIcon = Icons.Outlined.Compare,
        direction = ComparisonQuizListScreenDestination,
        primary = true
    ),
    NavigationItem.Item(
        text = R.string.maze,
        selectedIcon = Icons.Rounded.Route,
        direction = MazeScreenDestination,
        screenType = ScreenType.NAVIGATION_HIDDEN
    ),
    NavigationItem.Item(
        text = R.string.daily_challenge,
        selectedIcon = Icons.Rounded.Today,
        direction = DailyChallengeScreenDestination,
        screenType = ScreenType.NAVIGATION_HIDDEN,
        badge = NavDrawerBadgeItem(
            value = dailyChallengeClaimCount,
            description = "Daily challenge claim count"
        )
    ),
    NavigationItem.Label(
        text = R.string.online,
        group = NavDrawerItemGroup("online")
    ),
    NavigationItem.Item(
        text = R.string.profile,
        selectedIcon = Icons.Rounded.Person,
        direction = ProfileScreenDestination,
        group = NavDrawerItemGroup("online")
    ),
    NavigationItem.Label(text = R.string.other),
    NavigationItem.Item(
        text = R.string.settings,
        selectedIcon = Icons.Rounded.Settings,
        direction = SettingsScreenDestination(),
        screenType = ScreenType.NAVIGATION_HIDDEN
    )
)

private fun getNavigationItemBy(
    route: DestinationSpec<*>?,
    dailyChallengeClaimCount: Int = 0
) = getNavigationItems(dailyChallengeClaimCount)
    .filterIsInstance<NavigationItem.Item>()
    .find { item -> item.direction == route }

@Composable
@ExperimentalMaterial3Api
internal fun NavigationContainer(
    navController: NavController,
    windowWidthSize: WindowWidthSizeClass,
    isSignedIn: Boolean,
    showLoginCard: Boolean,
    dailyChallengeClaimCount: Int,
    onSignInClick: () -> Unit,
    onSignDismissClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val destination by navController.currentDestinationAsState()

    val selectedItem = remember(destination) { getNavigationItemBy(destination) }

    val navigationVisible = selectedItem != null && selectedItem.screenType == ScreenType.NORMAL

    val items = remember(isSignedIn, dailyChallengeClaimCount) {
        if (isSignedIn) {
            getNavigationItems(dailyChallengeClaimCount)
        } else {
            getNavigationItems(dailyChallengeClaimCount).filterNot { it.group == NavDrawerItemGroup("online") }
        }
    }

    val primaryItems = remember(items) {
        items
            .filterIsInstance<NavigationItem.Item>()
            .filter { it.primary }
    }

    val otherItems = remember(items, primaryItems) {
        items - primaryItems.toSet()
    }

    if (navigationVisible) {
        when (windowWidthSize) {
            WindowWidthSizeClass.Compact -> CompactContainer(
                navController = navController,
                primaryItems = primaryItems,
                navDrawerItems = otherItems,
                selectedItem = selectedItem,
                onSignInClick = onSignInClick,
                onSignDismissClick = onSignDismissClick,
                showLoginCard = showLoginCard,
                content = content
            )
            WindowWidthSizeClass.Medium -> MediumContainer(
                navController = navController,
                primaryItems = primaryItems,
                navDrawerItems = otherItems,
                selectedItem = selectedItem,
                onSignInClick = onSignInClick,
                onSignDismissClick = onSignDismissClick,
                showLoginCard = showLoginCard,
                content = content
            )
            WindowWidthSizeClass.Expanded -> ExpandedContainer(
                navController = navController,
                navigationItems = items,
                selectedItem = selectedItem,
                onSignInClick = onSignInClick,
                onSignDismissClick = onSignDismissClick,
                showLoginCard = showLoginCard,
                content = content
            )
        }
    } else {
        Scaffold(
            content = content
        )
    }
}