package com.infinitepower.newquiz.feature.maze.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.theme.NewQuizTheme

@Composable
internal fun MazeItemButton(
    modifier: Modifier = Modifier,
    itemPlayed: Boolean,
    isPlayableItem: Boolean,
    colors: MazeColors = MazeDefaults.defaultColors(),
) {
    val canPress = isPlayableItem || itemPlayed

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        if (isPressed) CirclePressScale else 1f,
        label = "Scale"
    )
    val circleRadius by animateIntAsState(
        if (isPressed) CirclePressedRadius else CircleUnPressedRadius,
        label = "Circle Radius"
    )

    Box(
        modifier = modifier
            .size(CircleSize)
            .scale(scale)
            .background(
                color = colors.circleContainerColor(
                    played = itemPlayed,
                    isPlayItem = isPlayableItem
                ),
                shape = RoundedCornerShape(circleRadius)
            )
            .then(
                if (isPlayableItem) {
                    Modifier
                        .padding(CurrentCircleInnerPadding)
                        .background(
                            color = colors.currentCircleInnerColor(),
                            shape = CircleShape
                        )
                } else {
                    Modifier
                }
            )
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = {},
                enabled = canPress,
                role = Role.Button
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = when {
                !isPlayableItem && !itemPlayed -> Icons.Rounded.Lock
                !isPlayableItem && itemPlayed -> Icons.Rounded.Check
                else -> Icons.Rounded.PlayArrow
            },
            contentDescription = null,
            tint = colors.circleContentColor(
                played = itemPlayed,
                isPlayItem = isPlayableItem
            ),
            modifier = Modifier.size(CircleSize / 2)
        )
    }
}

private val CircleSize = 50.dp
private val CurrentCircleInnerPadding = 6.dp

private const val CirclePressScale = 1.1f

private const val CirclePressedRadius = 20
private const val CircleUnPressedRadius = 50

@Composable
@PreviewLightDark
private fun MazeItemButtonPreview() {
    NewQuizTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MazeItemButton(
                    itemPlayed = true,
                    isPlayableItem = true,
                )
                MazeItemButton(
                    itemPlayed = false,
                    isPlayableItem = true,
                )
                MazeItemButton(
                    itemPlayed = false,
                    isPlayableItem = false,
                )
                MazeItemButton(
                    itemPlayed = true,
                    isPlayableItem = false,
                )
            }
        }
    }
}
