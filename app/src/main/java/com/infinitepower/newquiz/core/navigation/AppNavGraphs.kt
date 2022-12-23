package com.infinitepower.newquiz.core.navigation

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.android.ump.ConsentInformation
import com.infinitepower.newquiz.core.util.ui.nav_drawer.NavDrawerUtil
import com.infinitepower.newquiz.core.util.ui.nav_drawer.NavDrawerUtilImpl
import com.infinitepower.newquiz.home_presentation.destinations.HomeScreenDestination
import com.infinitepower.newquiz.home_presentation.destinations.LoginScreenDestination
import com.infinitepower.newquiz.math_quiz.list.destinations.MathQuizListScreenDestination
import com.infinitepower.newquiz.maze_quiz.destinations.MazeScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.*
import com.infinitepower.newquiz.online_services.ui.profile.destinations.ProfileScreenDestination
import com.infinitepower.newquiz.settings_presentation.destinations.SettingsScreenDestination
import com.infinitepower.newquiz.wordle.destinations.DailyWordSelectorScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleListScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.scope.DestinationScope
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

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
            MultiChoiceQuizScreenDestination,
            SettingsScreenDestination,
            SavedMultiChoiceQuestionsScreenDestination,
            WordleScreenDestination,
            WordleListScreenDestination,
            DailyWordSelectorScreenDestination,
            LoginScreenDestination,
            MultiChoiceQuizResultsScreenDestination,
            MultiChoiceQuizListScreenDestination,
            MultiChoiceCategoriesScreenDestination,
            ProfileScreenDestination,
            MathQuizListScreenDestination,
            MazeScreenDestination
        )
    )
}

internal fun DestinationScope<*>.currentNavigator(): CommonNavGraphNavigator {
    return CommonNavGraphNavigator(navController)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    windowWidthSizeClass: WindowWidthSizeClass,
    windowHeightSizeClass: WindowHeightSizeClass,
    consentInformation: ConsentInformation,
    signedIn: Boolean
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navDrawerUtil: NavDrawerUtil = NavDrawerUtilImpl(drawerState, scope)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                modifier = Modifier.fillMaxHeight(),
                navController = navController,
                drawerState = drawerState,
                signedIn = signedIn,
                onItemClick = { item ->
                    navDrawerUtil.close()
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
                dependency(consentInformation)
                dependency(navDrawerUtil)
            }
        )
    }
}