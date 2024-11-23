package com.infinitepower.newquiz.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.infinitepower.newquiz.comparison_quiz.destinations.ComparisonQuizListScreenDestination
import com.infinitepower.newquiz.comparison_quiz.destinations.ComparisonQuizScreenDestination
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.feature.daily_challenge.destinations.DailyChallengeScreenDestination
import com.infinitepower.newquiz.feature.maze.destinations.LevelResultsScreenDestination
import com.infinitepower.newquiz.feature.maze.destinations.MazeCategoriesInfoScreenDestination
import com.infinitepower.newquiz.feature.maze.destinations.MazeScreenDestination
import com.infinitepower.newquiz.feature.profile.destinations.ProfileScreenDestination
import com.infinitepower.newquiz.feature.settings.destinations.SettingsScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizListScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizResultsScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.SavedMultiChoiceQuestionsScreenDestination
import com.infinitepower.newquiz.ui.navigation.NavigationContainer
import com.infinitepower.newquiz.wordle.destinations.WordleListScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.DestinationScopeWithNoDependencies
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

@Immutable
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
        startRoute = MultiChoiceQuizListScreenDestination,
        destinations = listOf(
            MultiChoiceQuizScreenDestination,
            MultiChoiceQuizListScreenDestination,
            MultiChoiceQuizResultsScreenDestination,
            SettingsScreenDestination,
            SavedMultiChoiceQuestionsScreenDestination,
            WordleScreenDestination,
            WordleListScreenDestination,
            MazeScreenDestination,
            MazeCategoriesInfoScreenDestination,
            LevelResultsScreenDestination,
            ComparisonQuizScreenDestination,
            ComparisonQuizListScreenDestination,
            DailyChallengeScreenDestination,
            ProfileScreenDestination
        )
    )
}

internal fun DestinationScopeWithNoDependencies<*>.currentNavigator(
    remoteConfig: RemoteConfig
): CommonNavGraphNavigator {
    return CommonNavGraphNavigator(navController, remoteConfig)
}

@Composable
@ExperimentalMaterial3Api
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    remoteConfig: RemoteConfig,
    dailyChallengeClaimCount: Int,
    userDiamonds: UInt
) {
    NavigationContainer(
        navController = navController,
        windowWidthSize = windowSizeClass.widthSizeClass,
        dailyChallengeClaimCount = dailyChallengeClaimCount,
        userDiamonds = userDiamonds
    ) { innerPadding ->
        DestinationsNavHost(
            navGraph = AppNavGraphs.mainNavGraph,
            navController = navController,
            modifier = modifier.padding(innerPadding),
            dependenciesContainerBuilder = {
                dependency(currentNavigator(remoteConfig))
                dependency(windowSizeClass)
            }
        )
    }
}
