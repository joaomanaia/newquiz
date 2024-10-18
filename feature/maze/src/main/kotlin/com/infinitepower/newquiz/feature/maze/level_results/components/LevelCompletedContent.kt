package com.infinitepower.newquiz.feature.maze.level_results.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.R as CoreR

@Composable
internal fun LevelCompletedContent(
    onNext: () -> Unit,
    popBackStack: () -> Unit,
    modifier: Modifier = Modifier,
    nextLevelExists: Boolean = true
) {
    Column(
        modifier = modifier
            .padding(MaterialTheme.spacing.large)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = CoreR.string.level_completed),
            style = MaterialTheme.typography.headlineLarge
        )
        TrophyIcon(modifier = Modifier.padding(vertical = MaterialTheme.spacing.large))
        if (nextLevelExists) {
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = CoreR.string.next_level))
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            }
        }
        TextButton(
            onClick = popBackStack,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.Rounded.Home,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
            Text(text = stringResource(id = CoreR.string.main_menu))
        }
    }
}

@Composable
private fun TrophyIcon(modifier: Modifier = Modifier) {
    val trophySpec = LottieCompositionSpec.RawRes(CoreR.raw.trophy2)
    val trophyLottieComposition by rememberLottieComposition(spec = trophySpec)

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = SimpleColorFilter(MaterialTheme.colorScheme.primary.toArgb()),
            keyPath = arrayOf("**")
        ),
    )

    LottieAnimation(
        composition = trophyLottieComposition,
        modifier = modifier.size(200.dp),
        dynamicProperties = dynamicProperties
    )
}

@Composable
@PreviewLightDark
private fun LevelCompletedContentPreview() {
    NewQuizTheme {
        Surface {
            LevelCompletedContent(
                onNext = {},
                popBackStack = {},
            )
        }
    }
}
