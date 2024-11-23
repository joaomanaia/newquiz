package com.infinitepower.newquiz.core.ui.components.category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.DISABLED_ALPHA
import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo

@Composable
fun CategoryComponent(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: Any,
    requireInternetConnection: Boolean = false,
    showConnectionInfo: ShowCategoryConnectionInfo = ShowCategoryConnectionInfo.NONE,
    checked: Boolean = false,
    enabled: Boolean = true,
    clickEnabled: Boolean = enabled,
    textStyle: TextStyle = MaterialTheme.typography.headlineLarge,
    shape: Shape = MaterialTheme.shapes.large,
    onClick: () -> Unit = {},
    onCheckClick: () -> Unit = {}
) {
    val containerOverlayColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.primary
    }.copy(alpha = if (checked) 0.6f else 0.5f)

    val textColor = Color.White.copy(
        alpha = if (enabled) 1f else DISABLED_ALPHA
    )

    Surface(
        modifier = modifier.height(120.dp),
        shape = shape,
        onClick = onClick,
        enabled = enabled && clickEnabled,
        border = if (checked) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            null
        }
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(id = R.string.image_category_of_s, title),
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium)
                .alpha(if (enabled) 1f else DISABLED_ALPHA),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(containerOverlayColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = textStyle,
                color = textColor,
                textAlign = TextAlign.Center,
            )

            Row(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.default)
                    .align(Alignment.TopEnd),
            ) {
                if (showConnectionInfo.shouldShowBadge(requireInternetConnection)) {
                    CategoryConnectionInfoBadge(
                        requireConnection = requireInternetConnection
                    )
                }
                AnimatedVisibility(
                    visible = checked,
                    label = "Checked badge"
                ) {
                    CategoryCheckedBadge(
                        onClick = onCheckClick
                    )
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun CategoryPreview() {
    NewQuizTheme {
        Surface {
            CategoryComponent(
                title = "Title",
                imageUrl = "",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                requireInternetConnection = true,
                showConnectionInfo = ShowCategoryConnectionInfo.BOTH,
                checked = true
            )
        }
    }
}
