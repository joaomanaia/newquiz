package com.infinitepower.newquiz.core.navigation

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.infinitepower.newquiz.quiz_presentation.destinations.QuizScreenDestination
import com.infinitepower.newquiz.home_presentation.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.DestinationScope
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

internal data class NavGraph(
    override val route: String,
    override val startRoute: Route,
    val destinations: List<DestinationSpec<out Any>>,
    override val nestedNavGraphs: List<NavGraph> = emptyList()
): NavGraphSpec {
    override val destinationsByRoute: Map<String, DestinationSpec<out Any>> = destinations.associateBy { it.route }
}

internal object AppNavGraphs {
    val mainNavGraph = NavGraph(
        route = "main_nav_graph",
        startRoute = HomeScreenDestination,
        destinations = listOf(
            HomeScreenDestination,
            QuizScreenDestination
        )
    )
}

internal fun DestinationScope<*>.currentNavigator(): CommonNavGraphNavigator {
    return CommonNavGraphNavigator(navController)
}

@Composable
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    windowWidthSizeClass: WindowWidthSizeClass,
    windowHeightSizeClass: WindowHeightSizeClass
) {
    DestinationsNavHost(
        navGraph = AppNavGraphs.mainNavGraph,
        navController = navController,
        modifier = modifier,
        dependenciesContainerBuilder = {
            dependency(currentNavigator())
            dependency(windowWidthSizeClass)
            dependency(windowHeightSizeClass)
        }
    )
}