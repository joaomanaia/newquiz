package com.infinitepower.newquiz.comparison_quiz.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.comparison_quiz.ui.AnimationState
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.CustomColor
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.extendedColors

/**
 * A circle with the text "or" in the middle.
 * Used to separate the two options in the comparison quiz.
 *
 * When the question is answered, the circle is filled with the correct color
 * with an animation.
 */
@Composable
internal fun MiddleCircle(
    modifier: Modifier = Modifier,
    animationState: AnimationState = AnimationState.Normal
) {
    val animationTransition = updateTransition(
        targetState = animationState,
        label = "Middle Circle"
    )

    val greenColor = MaterialTheme.extendedColors.getColorAccentByKey(
        key = CustomColor.Keys.Green
    )

    val color by animationTransition.animateColor(
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION_MILLIS) },
        label = "Color"
    ) { state ->
        when (state) {
            AnimationState.StartGame -> MaterialTheme.colorScheme.surface
            AnimationState.Normal -> MaterialTheme.colorScheme.tertiary
            AnimationState.NextQuestion -> greenColor
        }
    }

    val tonalElevation by animationTransition.animateDp(
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION_MILLIS) },
        label = "Tonal Elevation"
    ) { state ->
        when (state) {
            AnimationState.StartGame -> 0.dp
            AnimationState.Normal, AnimationState.NextQuestion -> DEFAULT_TONAL_ELEVATION
        }
    }

    MiddleCircle(
        modifier = modifier,
        animationTransition = animationTransition,
        color = color,
        tonalElevation = tonalElevation
    )
}

private const val ANIMATION_DURATION_MILLIS = 310
private val DEFAULT_TONAL_ELEVATION = 2.dp

@Composable
private fun MiddleCircle(
    modifier: Modifier = Modifier,
    animationTransition: Transition<AnimationState>,
    color: Color = MaterialTheme.colorScheme.tertiary,
    tonalElevation: Dp = DEFAULT_TONAL_ELEVATION
) {
    Surface(
        shape = CircleShape,
        modifier = modifier.size(48.dp),
        color = color,
        tonalElevation = tonalElevation
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            animationTransition.AnimatedContent { state ->
                when (state) {
                    AnimationState.StartGame -> Unit

                    AnimationState.Normal -> {
                        Text(
                            text = stringResource(id = R.string.or).uppercase(),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    AnimationState.NextQuestion -> {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@PreviewNightLight
private fun MiddleCirclePreview() {
    NewQuizTheme {
        Surface {
            MiddleCircle(
                modifier = Modifier.padding(16.dp),
                animationState = AnimationState.Normal
            )
        }
    }
}
