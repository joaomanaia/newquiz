package com.infinitepower.newquiz.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.automirrored.rounded.ListAlt
import androidx.compose.material.icons.outlined.Compare
import androidx.compose.material.icons.outlined.ViewModule
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material.icons.rounded.ViewModule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.infinitepower.newquiz.comparison_quiz.destinations.ComparisonQuizListScreenDestination
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.navigation.NavDrawerBadgeItem
import com.infinitepower.newquiz.core.navigation.NavigationItem
import com.infinitepower.newquiz.core.navigation.ScreenType
import com.infinitepower.newquiz.core.ui.ObserveAsEvents
import com.infinitepower.newquiz.core.ui.SnackbarController
import com.infinitepower.newquiz.core.util.asString
import com.infinitepower.newquiz.feature.daily_challenge.destinations.DailyChallengeScreenDestination
import com.infinitepower.newquiz.feature.maze.destinations.MazeScreenDestination
import com.infinitepower.newquiz.feature.profile.destinations.ProfileScreenDestination
import com.infinitepower.newquiz.feature.settings.destinations.SettingsScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizListScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleListScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

internal fun getPrimaryItems(): ImmutableList<NavigationItem.Item> = persistentListOf(
    NavigationItem.Item(
        text = R.string.multi_choice_quiz,
        selectedIcon = Icons.AutoMirrored.Rounded.ListAlt,
        unselectedIcon = Icons.AutoMirrored.Rounded.List,
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
)

internal fun getOtherItems(
    dailyChallengeClaimCount: Int = 0
): ImmutableList<NavigationItem> = persistentListOf(
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
    NavigationItem.Label(text = R.string.user),
    NavigationItem.Item(
        text = R.string.profile,
        selectedIcon = Icons.Rounded.Person,
        direction = ProfileScreenDestination,
        screenType = ScreenType.NAVIGATION_HIDDEN
    ),
    NavigationItem.Label(text = R.string.other),
    NavigationItem.Item(
        text = R.string.settings,
        selectedIcon = Icons.Rounded.Settings,
        direction = SettingsScreenDestination(),
        screenType = ScreenType.NAVIGATION_HIDDEN
    )
)

private fun List<NavigationItem>.getNavigationItemBy(
    route: DestinationSpec<*>?
): NavigationItem.Item? = filterIsInstance<NavigationItem.Item>()
    .find { item -> item.direction == route }

@Composable
@ExperimentalMaterial3Api
internal fun NavigationContainer(
    navController: NavController,
    windowWidthSize: WindowWidthSizeClass,
    dailyChallengeClaimCount: Int,
    userDiamonds: UInt,
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()

    val navigator = navController.rememberDestinationsNavigator()
    val destination by navController.currentDestinationAsState()

    val primaryItems = remember { getPrimaryItems() }

    val otherItems = remember(dailyChallengeClaimCount) {
        getOtherItems(dailyChallengeClaimCount)
    }

    val selectedItem = remember(primaryItems, otherItems, destination) {
        primaryItems.getNavigationItemBy(destination)
            ?: otherItems.getNavigationItemBy(destination)
    }

    val navigationVisible = remember(selectedItem) {
        selectedItem != null && selectedItem.screenType == ScreenType.NORMAL
    }

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    ObserveAsEvents(flow = SnackbarController.events, snackbarHostState) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = event.message.asString(context),
                actionLabel = event.action?.name?.asString(context),
                withDismissAction = event.withDismissAction,
                duration = event.duration
            )

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }

    if (navigationVisible) {
        when (windowWidthSize) {
            WindowWidthSizeClass.Compact -> CompactContainer(
                navigator = navigator,
                navController = navController,
                primaryItems = primaryItems,
                otherItems = otherItems,
                selectedItem = selectedItem,
                userDiamonds = userDiamonds,
                snackbarHostState = snackbarHostState,
                content = content
            )

            WindowWidthSizeClass.Medium -> MediumContainer(
                navigator = navigator,
                primaryItems = primaryItems,
                otherItems = otherItems,
                selectedItem = selectedItem,
                userDiamonds = userDiamonds,
                snackbarHostState = snackbarHostState,
                content = content
            )

            WindowWidthSizeClass.Expanded -> ExpandedContainer(
                navigator = navigator,
                primaryItems = primaryItems,
                otherItems = otherItems,
                selectedItem = selectedItem,
                userDiamonds = userDiamonds,
                snackbarHostState = snackbarHostState,
                content = content
            )
        }
    } else {
        Scaffold(
            content = content,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            // This Scaffold only manages Snackbar display and shouldn't handle window insets.
            // Insets are delegated to parent layouts or other Scaffolds in the composable hierarchy.
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        )
    }
}
