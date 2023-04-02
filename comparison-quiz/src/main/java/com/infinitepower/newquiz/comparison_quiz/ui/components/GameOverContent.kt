package com.infinitepower.newquiz.comparison_quiz.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.R as CoreR

@Composable
internal fun GameOverContent(
    modifier: Modifier = Modifier,
    scorePosition: Int,
    highestPosition: Int,
    onBackClick: () -> Unit = {},
    onPlayAgainClick: () -> Unit = {}
) {
    val spaceMedium = MaterialTheme.spacing.medium
    val spaceLarge = MaterialTheme.spacing.large

    val trophySpec = LottieCompositionSpec.RawRes(R.raw.trophy2)
    val trophyLottieComposition by rememberLottieComposition(spec = trophySpec)

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = SimpleColorFilter(MaterialTheme.colorScheme.primary.toArgb()),
            keyPath = arrayOf("**")
        ),
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(
            vertical = MaterialTheme.spacing.extraLarge,
            horizontal = spaceLarge
        )
    ) {
        item("headline") {
            Text(
                text = stringResource(id = CoreR.string.game_over),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(spaceLarge))
        }

        item("animation_image") {
            LottieAnimation(
                composition = trophyLottieComposition,
                modifier = Modifier.size(200.dp),
                dynamicProperties = dynamicProperties
            )
            Spacer(modifier = Modifier.height(spaceLarge))
        }

        item("current_score") {
            Text(
                text = stringResource(id = CoreR.string.your_score).uppercase(),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(spaceMedium))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spaceLarge)
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = scorePosition.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(vertical = MaterialTheme.spacing.small)
                    )
                }
            }
            Spacer(modifier = Modifier.height(spaceLarge))
        }

        item("highest_score") {
            Text(
                text = stringResource(id = CoreR.string.highest_score).uppercase(),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(spaceMedium))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spaceLarge)
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = highestPosition.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(vertical = MaterialTheme.spacing.small)
                    )
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
        }

        item("buttons") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(id = CoreR.string.back))
                }
                Spacer(modifier = Modifier.width(spaceMedium))
                Button(
                    onClick = onPlayAgainClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(id = CoreR.string.play_again))
                }
            }
        }
    }
}

@Composable
@AllPreviewsNightLight
private fun GameOverContentPreview() {
    NewQuizTheme {
        Surface {
            GameOverContent(
                scorePosition = 3,
                highestPosition = 5
            )
        }
    }
}