package com.infinitepower.newquiz.compose.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.infinitepower.newquiz.compose.R
import com.infinitepower.newquiz.compose.core.navigation.Screen
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import com.infinitepower.newquiz.compose.ui.quiz.QuizOption

@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
fun MainScreen(
    navController: NavHostController,
    authUserApi: AuthUserApi
) {
    val mAuthUser = Firebase.auth.currentUser
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "NewQuiz")
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            item { Spacer(modifier = Modifier.height(25.dp)) }
            item { AuthUserMainScreen(navController, authUserApi) }
            item { Spacer(modifier = Modifier.height(if (authUserApi.isSignedIn) 50.dp else 16.dp)) }
            item {
                QuickQuizItem(onClick = { Screen.QuizScreen.navigate(navController, QuizOption.QuickQuiz) })
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun QuickQuizItem(
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        role = Role.Button,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(10.dp),
        indication = rememberRipple(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Quick Quiz",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            val composition by rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(R.raw.quick_quiz)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier.size(100.dp),
                    iterations = LottieConstants.IterateForever,
                )
            }
        }
    }
}