package com.infinitepower.newquiz.core.ui.home_card.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.common.compose.preview.BooleanPreviewParameterProvider
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

    val cardColors = if (data.backgroundPrimary) {
        getPrimaryCardColors()
    } else {
        CardDefaults.cardColors()
    }

    Card(
        onClick = data.onClick,
        enabled = data.enabled,
        modifier = modifier,
        colors = cardColors
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
private fun getPrimaryCardColors(): CardColors = CardDefaults.cardColors(
    containerColor = MaterialTheme.colorScheme.primary,
    contentColor = MaterialTheme.colorScheme.onPrimary,
    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = DisabledAlpha),
    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = DisabledAlpha),
)

private const val DisabledAlpha = 0.38f

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeLargeCardPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) enabled: Boolean
) {
    NewQuizTheme {
        Surface {
            HomeLargeCard(
                data = HomeCardItem.LargeCard(
                    title = R.string.quick_quiz,
                    icon = CardIcon.Icon(Icons.Rounded.Check),
                    onClick = {},
                    enabled = enabled
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun PrimaryHomeLargeCardPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) enabled: Boolean
) {
    NewQuizTheme {
        Surface {
            HomeLargeCard(
                data = HomeCardItem.LargeCard(
                    title = R.string.quick_quiz,
                    icon = CardIcon.Icon(Icons.Rounded.Check),
                    onClick = {},
                    enabled = enabled,
                    backgroundPrimary = true
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
