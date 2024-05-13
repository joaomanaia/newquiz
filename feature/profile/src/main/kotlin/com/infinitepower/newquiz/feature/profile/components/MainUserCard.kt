package com.infinitepower.newquiz.core.user_services.ui.profile.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.infinitepower.newquiz.core.common.DEFAULT_USER_PHOTO
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.R as CoreR

@Composable
internal fun MainUserCard(
    modifier: Modifier = Modifier,
    level: UInt,
    levelProgress: Float,
    userName: String,
    userPhoto: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large, Alignment.CenterHorizontally)
    ) {
        LevelCard(
            level = level,
            levelProgress = levelProgress,
            userPhoto = userPhoto,
            userName = userName
        )
        GoodDayText(name = userName)
    }
}

private enum class AvatarState {
    LEVEL,
    PHOTO;

    fun toggle() = when (this) {
        LEVEL -> PHOTO
        PHOTO -> LEVEL
    }
}

@Composable
private fun LevelCard(
    modifier: Modifier = Modifier,
    level: UInt,
    levelProgress: Float,
    userPhoto: String,
    userName: String,
    initialAvatarState: AvatarState = AvatarState.LEVEL
) {
    val levelProgressAnimated = animateFloatAsState(
        targetValue = levelProgress,
        label = "Level Progress",
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    var avatarState by remember { mutableStateOf(initialAvatarState) }

    val avatarLevelTransition = updateTransition(
        targetState = avatarState,
        label = "Avatar Level"
    )

    Surface(
        modifier = modifier
            .size(75.dp),
        onClick = { avatarState = avatarState.toggle() },
        shape = CircleShape
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                progress = { levelProgressAnimated.value },
                modifier = Modifier.fillMaxSize(),
                trackColor = ProgressIndicatorDefaults.linearTrackColor,
                strokeCap = StrokeCap.Round,
            )

            avatarLevelTransition.AnimatedContent { state ->
                when (state) {
                    AvatarState.LEVEL -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(MaterialTheme.spacing.small)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                .clip(CircleShape)
                        ) {
                            Text(
                                text = level.toString(),
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    AvatarState.PHOTO -> {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .crossfade(true)
                                .data(userPhoto)
                                .build(),
                            contentDescription = stringResource(id = CoreR.string.photo_of_s, userName),
                            placeholder = rememberVectorPainter(Icons.Rounded.Person),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(MaterialTheme.spacing.small)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }

}

@Composable
@PreviewLightDark
private fun MainUserCardPreview() {
    NewQuizTheme {
        Surface {
            MainUserCard(
                modifier = Modifier.padding(16.dp),
                level = 3u,
                levelProgress = 0.75f,
                userName = "NewQuiz User",
                userPhoto = DEFAULT_USER_PHOTO
            )
        }
    }
}
