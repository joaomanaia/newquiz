package com.infinitepower.newquiz.settings_presentation.components.other

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Balance
import androidx.compose.material.icons.rounded.ContactSupport
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.R as CoreR

private const val NEWQUIZ_REPOSITORY_LINK = "https://github.com/joaomanaia/newquiz"

@Composable
@ExperimentalMaterial3Api
internal fun AboutAndHelpButtons(
    modifier: Modifier = Modifier,
    iconSize: Dp = 50.dp
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        FilledIconButton(
            imageVector = ImageVector.vectorResource(id = CoreR.drawable.github_logo),
            contentDescription = "Github repository",
            onClick = { uriHandler.openUri(NEWQUIZ_REPOSITORY_LINK) },
            modifier = Modifier.size(iconSize)
        )
        FilledIconButton(
            imageVector = Icons.Rounded.ContactSupport,
            contentDescription = "Contact support",
            onClick = { uriHandler.openUri("https://github.com/joaomanaia/newquiz/issues") },
            modifier = Modifier.size(iconSize)
        )
        FilledIconButton(
            imageVector = Icons.Rounded.Update,
            contentDescription = "Update",
            onClick = { uriHandler.openUri("https://github.com/joaomanaia/newquiz/releases") },
            modifier = Modifier.size(iconSize)
        )
        FilledIconButton(
            imageVector = Icons.Rounded.Balance,
            contentDescription = "Open source licences",
            onClick = {
                context.startActivity(
                    Intent(context, OssLicensesMenuActivity::class.java).apply {
                        action = Intent.ACTION_VIEW
                    }
                )
            },
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
@ExperimentalMaterial3Api
internal fun FilledIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String?,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    FilledIconButton(
        modifier = modifier,
        onClick = onClick,
        colors = IconButtonDefaults.filledIconButtonColors(containerColor = containerColor)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun AboutAndHelpButtonsPreview() {
    NewQuizTheme {
        Surface {
            AboutAndHelpButtons(
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun PrimaryButtonPreview() {
    NewQuizTheme {
        Surface {
            FilledIconButton(
                modifier = Modifier.padding(16.dp),
                imageVector = Icons.Rounded.Favorite,
                contentDescription = "Favorite",
                onClick = {}
            )
        }
    }
}
