package com.infinitepower.newquiz.feature.settings.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.infinitepower.newquiz.feature.settings.model.ScreenKey
import com.infinitepower.newquiz.feature.settings.screens.about_and_help.AboutAndHelpScreen
import com.infinitepower.newquiz.feature.settings.screens.analytics.AnalyticsScreen
import com.infinitepower.newquiz.feature.settings.screens.animations.AnimationsScreen
import com.infinitepower.newquiz.feature.settings.screens.general.GeneralScreen
import com.infinitepower.newquiz.feature.settings.screens.multi_choice_quiz.MultiChoiceQuizScreen
import com.infinitepower.newquiz.feature.settings.screens.translation.TranslationScreen
import com.infinitepower.newquiz.feature.settings.screens.wordle.WordleScreen

@Composable
@ExperimentalMaterial3Api
internal fun PreferencesScreen(
    modifier: Modifier = Modifier,
    currentScreen: ScreenKey?,
    isScreenExpanded: Boolean,
    onBackClick: () -> Unit,
    navigateToScreen: (key: ScreenKey) -> Unit,
) {
    when (currentScreen) {
        null -> {}
        ScreenKey.GENERAL -> GeneralScreen(
            modifier = modifier,
            isScreenExpanded = isScreenExpanded,
            onBackClick = onBackClick,
            navigateToScreen = navigateToScreen
        )
        ScreenKey.MULTI_CHOICE_QUIZ -> MultiChoiceQuizScreen(
            modifier = modifier,
            isScreenExpanded = isScreenExpanded,
            onBackClick = onBackClick,
        )
        ScreenKey.WORDLE -> WordleScreen(
            modifier = modifier,
            isScreenExpanded = isScreenExpanded,
            onBackClick = onBackClick,
        )
        ScreenKey.ABOUT_AND_HELP -> AboutAndHelpScreen(
            modifier = modifier,
            isScreenExpanded = isScreenExpanded,
            onBackClick = onBackClick,
        )
        ScreenKey.ANIMATIONS -> AnimationsScreen(
            modifier = modifier,
            isScreenExpanded = isScreenExpanded,
            onBackClick = onBackClick,
        )
        ScreenKey.ANALYTICS -> AnalyticsScreen(
            modifier = modifier,
            isScreenExpanded = isScreenExpanded,
            onBackClick = onBackClick,
        )
        ScreenKey.TRANSLATION -> TranslationScreen(
            modifier = modifier,
            isScreenExpanded = isScreenExpanded,
            onBackClick = onBackClick,
        )
    }
}
