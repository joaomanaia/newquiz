package com.infinitepower.newquiz.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.infinitepower.newquiz.comparison_quiz.destinations.ComparisonQuizListScreenDestination
import com.infinitepower.newquiz.comparison_quiz.destinations.ComparisonQuizScreenDestination
import com.infinitepower.newquiz.home_presentation.destinations.HomeScreenDestination
import com.infinitepower.newquiz.home_presentation.destinations.LoginScreenDestination
import com.infinitepower.newquiz.home_presentation.destinations.LoginWithEmailScreenDestination
import com.infinitepower.newquiz.math_quiz.list.destinations.MathQuizListScreenDestination
import com.infinitepower.newquiz.maze_quiz.destinations.MazeScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.*
import com.infinitepower.newquiz.online_services.ui.profile.destinations.ProfileScreenDestination
import com.infinitepower.newquiz.settings_presentation.destinations.SettingsScreenDestination
import com.infinitepower.newquiz.ui.navigation.NavigationContainer
import com.infinitepower.newquiz.wordle.destinations.DailyWordleCalendarScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleListScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.DestinationScopeWithNoDependencies
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
            DailyWordleCalendarScreenDestination,
            LoginScreenDestination,
            LoginWithEmailScreenDestination,
            MultiChoiceQuizResultsScreenDestination,
            MultiChoiceQuizListScreenDestination,
            MultiChoiceCategoriesScreenDestination,
            ProfileScreenDestination,
            MathQuizListScreenDestination,
            MazeScreenDestination,
            ComparisonQuizScreenDestination,
            ComparisonQuizListScreenDestination
        )
    )
}

internal fun DestinationScopeWithNoDependencies<*>.currentNavigator(): CommonNavGraphNavigator {
    return CommonNavGraphNavigator(navController)
}

@Composable
@ExperimentalMaterial3Api
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    signedIn: Boolean
) {
    NavigationContainer(
        navController = navController,
        windowWidthSize = windowSizeClass.widthSizeClass,
        isSignedIn = signedIn
    ) { innerPadding ->
        DestinationsNavHost(
            navGraph = AppNavGraphs.mainNavGraph,
            navController = navController,
            modifier = modifier.padding(innerPadding),
            dependenciesContainerBuilder = {
                dependency(currentNavigator())
                dependency(windowSizeClass)
            }
        )
    }
}