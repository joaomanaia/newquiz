package com.infinitepower.newquiz.core.navigation

import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.infinitepower.newquiz.quiz_presentation.destinations.QuizScreenDestination
import com.infinitepower.newquiz.home_presentation.destinations.HomeScreenDestination
import com.infinitepower.newquiz.quiz_presentation.destinations.SavedQuestionsScreenDestination
import com.infinitepower.newquiz.settings_presentation.destinations.SettingsScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.scope.DestinationScope
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.destination
import com.ramcosta.composedestinations.utils.startDestination
import kotlinx.coroutines.launch

internal data class NavGraph(
    override val route: String,
    override val startRoute: Route,
    val destinations: List<DestinationSpec<out Any>>,
    override val nestedNavGraphs: List<NavGraph> = emptyList()
) : NavGraphSpec {
    override val destinationsByRoute: Map<String, DestinationSpec<out Any>> =
        destinations.associateBy { it.route }
}

internal object AppNavGraphs {
    val mainNavGraph = NavGraph(
        route = "main_nav_graph",
        startRoute = HomeScreenDestination,
        destinations = listOf(
            HomeScreenDestination,
            QuizScreenDestination,
            SettingsScreenDestination,
            SavedQuestionsScreenDestination,
            WordleScreenDestination
        )
    )
}

internal fun DestinationScope<*>.currentNavigator(): CommonNavGraphNavigator {
    return CommonNavGraphNavigator(navController)
}

private val navDrawerVisibleDestinations = listOf(
    HomeScreenDestination,
    SettingsScreenDestination
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    windowWidthSizeClass: WindowWidthSizeClass,
    windowHeightSizeClass: WindowHeightSizeClass
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                navController = navController,
                drawerState = drawerState,
                onItemClick = { item ->
                    scope.launch { drawerState.close() }
                    navController.navigate(item.direction)
                }
            )
        }
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
}