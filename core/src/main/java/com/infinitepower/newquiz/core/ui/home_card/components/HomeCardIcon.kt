package com.infinitepower.newquiz.core.ui.home_card.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon

@Composable
internal fun HomeCardIcon(
    modifier: Modifier = Modifier,
    icon: CardIcon,
    contentDescription: String
) {
    when (icon) {
        is CardIcon.Icon -> {
            Icon(
                imageVector = icon.vector,
                contentDescription = contentDescription,
                modifier = modifier
            )
        }
        is CardIcon.Lottie -> {
            val composition by rememberLottieComposition(spec = icon.spec)

            LottieAnimation(
                composition = composition,
                modifier = modifier,
                iterations = LottieConstants.IterateForever,
            )
        }
    }
}