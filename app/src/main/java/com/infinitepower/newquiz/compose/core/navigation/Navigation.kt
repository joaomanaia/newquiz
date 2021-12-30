package com.infinitepower.newquiz.compose.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import com.infinitepower.newquiz.compose.ui.login.LoginScreen
import com.infinitepower.newquiz.compose.ui.main.MainScreen
import com.infinitepower.newquiz.compose.ui.quiz.QuizOption
import com.infinitepower.newquiz.compose.ui.quiz.QuizScreen

@Composable
fun Navigation(
    authUserApi: AuthUserApi
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController, authUserApi)
        }

        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }

        composable(
            route = "${Screen.QuizScreen.route}/{quizOptions}",
            arguments = listOf(
                navArgument("quizOptions") {
                    type = NavType.IntType
                    nullable = false
                    defaultValue = QuizOption.QuickQuiz.id
                }
            )
        ) {
            QuizScreen(navController)
        }
    }
}