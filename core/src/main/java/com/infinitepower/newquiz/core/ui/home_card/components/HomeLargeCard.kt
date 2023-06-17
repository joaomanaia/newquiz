package com.infinitepower.newquiz.core.ui.home_card.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem

@Composable
@ExperimentalMaterial3Api
fun HomeLargeCard(
    modifier: Modifier = Modifier,
    data: HomeCardItem.LargeCard
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val title = stringResource(id = data.title)

    val containerColor = if (data.backgroundPrimary) {
        MaterialTheme.colorScheme.primary
    } else MaterialTheme.colorScheme.surfaceVariant

    val contentColor = if (data.backgroundPrimary) {
        MaterialTheme.colorScheme.onPrimary
    } else MaterialTheme.colorScheme.onSurfaceVariant

    val isInspectionMode = LocalInspectionMode.current

    Card(
        onClick = data.onClick,
        enabled = data.enabled || isInspectionMode,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
        )
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

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeLargeCardPreview() {
    NewQuizTheme {
        Surface {
            HomeLargeCard(
                data = HomeCardItem.LargeCard(
                    title = R.string.quick_quiz,
                    icon = CardIcon.Icon(Icons.Rounded.Check),
                    onClick = {}
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}