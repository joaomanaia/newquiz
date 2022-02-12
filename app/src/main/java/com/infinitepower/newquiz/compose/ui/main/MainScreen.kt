package com.infinitepower.newquiz.compose.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infinitepower.newquiz.compose.R
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import com.infinitepower.newquiz.compose.ui.destinations.LoginScreenDestination
import com.infinitepower.newquiz.compose.ui.destinations.QuizScreenDestination
import com.infinitepower.newquiz.compose.ui.destinations.SavedQuestionsListDestination
import com.infinitepower.newquiz.compose.ui.destinations.UnscrambleWordQuizScreenDestination
import com.infinitepower.newquiz.compose.ui.quiz.QuizOption
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(
    navigator: DestinationsNavigator,
) {
    val mainScreenViewModel: MainScreenViewModel = hiltViewModel()

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
            item {
                HomeUserContent(
                    authUserApi = mainScreenViewModel.authUserApi,
                    goToLoginScreen = { navigator.navigate(LoginScreenDestination) }
                )
            }
            item { Spacer(modifier = Modifier.height(if (mainScreenViewModel.authUserApi.isSignedIn) 50.dp else 16.dp)) }
            item {
                QuickQuizItem(
                    onClick = {
                        navigator.navigate(
                            QuizScreenDestination(
                                quizOptions = QuizOption.QUICK_QUIZ
                            )
                        )
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                Text(
                    text = "Offline",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                OfflineSavedCard(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = { navigator.navigate(SavedQuestionsListDestination) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                LargeCard(
                    text = "Unscramble Word Quiz",
                    icon = Icons.Rounded.Quiz,
                    onClick = { navigator.navigate(UnscrambleWordQuizScreenDestination) },
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = 16.dp)
                )
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

@Composable
private fun OfflineSavedCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        tonalElevation = 8.dp,
        indication = rememberRipple(),
        role = Role.Button,
        onClick = onClick,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Saved Quick Quiz",
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

@Composable
private fun LargeCard(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        tonalElevation = 8.dp,
        indication = rememberRipple(),
        role = Role.Button,
        onClick = onClick,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = text,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    modifier = Modifier.size(100.dp),
                )
            }
        }
    }
}