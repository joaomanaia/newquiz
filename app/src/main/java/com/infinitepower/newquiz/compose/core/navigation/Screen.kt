package com.infinitepower.newquiz.compose.core.navigation

import androidx.navigation.NavHostController
import com.infinitepower.newquiz.compose.ui.quiz.QuizOption
import io.ktor.util.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")

    object LoginScreen : Screen("login_screen")

    object QuizScreen : Screen("quiz_screen") {
        fun navigate(host: NavHostController, options: QuizOption) {
            host.navigate(QuizScreen.withArgs(options.id))
        }
    }

    fun withArgs(vararg args: Any): String = buildString {
        append(route)
        args.forEach { arg ->
            //append("/${URLEncoder.encode(arg, StandardCharsets.UTF_8.toString())}")
            append("/$arg")
        }
    }
}