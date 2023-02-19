package com.infinitepower.newquiz.settings_presentation.components.other

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.util.rememberAppVersion
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@ExperimentalMaterial3Api
internal fun AppNameWithLogo(
    modifier: Modifier = Modifier
) {
    val appVersion = rememberAppVersion()

    AppNameWithLogo(
        modifier = modifier,
        logoContent = { AppLogo() },
        appNameWithVersion = {
            AppNameWithVersion(
                appName = stringResource(id = CoreR.string.app_name),
                appVersion = appVersion
            )
        }
    )
}

@Composable
@ExperimentalMaterial3Api
internal fun AppNameWithLogo(
    modifier: Modifier = Modifier,
    logoContent: @Composable () -> Unit,
    appNameWithVersion: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        logoContent()
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        appNameWithVersion()
    }
}

@Composable
@ExperimentalMaterial3Api
private fun AppNameWithVersion(
    modifier: Modifier = Modifier,
    appName: String,
    appVersion: String
) {
    BadgedBox(
        badge = {
            Badge {
                Text(text = appVersion)
            }
        },
        modifier = modifier
    ) {
        Text(
            text = appName,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun AppLogo(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit = {}
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedCurve by animateFloatAsState(
        targetValue = if (isPressed) 0f else 0.05f,
        animationSpec = tween(durationMillis = 500),
        label = "Curve"
    )

    val roundedPolygonShape = RoundedPolygonShape(
        sides = 12,
        curve = animatedCurve.toDouble()
    )

    Surface(
        modifier = modifier.size(240.dp),
        color = color,
        shape = roundedPolygonShape,
        interactionSource = interactionSource,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = CoreR.drawable.logo_monochromatic),
            contentDescription = stringResource(id = CoreR.string.app_name),
            modifier = Modifier.size(90.dp)
        )
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun AppNameWithLogoPreview() {
    NewQuizTheme {
        Surface {
            AppNameWithLogo(
                modifier = Modifier.padding(16.dp),
                logoContent = { AppLogo() },
                appNameWithVersion = {
                    AppNameWithVersion(
                        appName = stringResource(id = CoreR.string.app_name),
                        appVersion = "1.0.0"
                    )
                }
            )
        }
    }
}
