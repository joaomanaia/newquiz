package com.infinitepower.newquiz.math_quiz.list

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.ui.home_card.HomeListContent
import com.infinitepower.newquiz.math_quiz.list.data.getMathQuizListCardItemData
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun MathQuizListScreen(
    navigator: DestinationsNavigator
) {
    val cardItemData = remember { getMathQuizListCardItemData(navigator) }

    HomeListContent(items = cardItemData)

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("MathQuizListScreen")
    }
}