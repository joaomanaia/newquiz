package com.infinitepower.newquiz.home_presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.home_presentation.model.CardIcon
import com.infinitepower.newquiz.home_presentation.model.HomeCardItem

@Composable
@ExperimentalMaterial3Api
internal fun HomeLargeCard(
    modifier: Modifier = Modifier,
    data: HomeCardItem.LargeCard
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val title = stringResource(id = data.title)

    Card(
        onClick = data.onClick,
        enabled = data.enabled,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(spaceMedium),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(spaceMedium))
            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spaceMedium)
            ) {
                when (data.icon) {
                    is CardIcon.Icon -> {
                        Icon(
                            imageVector = data.icon.vector,
                            contentDescription = title,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    is CardIcon.Lottie -> {
                        val composition by rememberLottieComposition(spec = data.icon.spec)

                        LottieAnimation(
                            composition = composition,
                            modifier = Modifier.size(100.dp),
                            iterations = LottieConstants.IterateForever,
                        )
                    }
                }
            }
        }
    }
}