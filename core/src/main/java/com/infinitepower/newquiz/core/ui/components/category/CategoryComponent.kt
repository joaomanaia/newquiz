package com.infinitepower.newquiz.core.ui.components.category

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.compose.StatusWrapper
import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo

@Composable
fun CategoryComponent(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: Any,
    requireInternetConnection: Boolean = false,
    showConnectionInfo: ShowCategoryConnectionInfo = ShowCategoryConnectionInfo.NONE,
    enabled: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.headlineLarge,
    onClick: () -> Unit = {}
) {
    val shapeMedium = MaterialTheme.shapes.large

    val containerOverlayColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    } else {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    }

    StatusWrapper(
        enabled = enabled
    ) {
        Surface(
            modifier = modifier,
            shape = shapeMedium,
            onClick = onClick,
            enabled = enabled
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = stringResource(id = R.string.image_category_of_s, title),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium),
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
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                if (showConnectionInfo.shouldShowBadge(requireInternetConnection)) {
                    CategoryConnectionInfoBadge(
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.default)
                            .align(Alignment.TopEnd),
                        requireConnection = requireInternetConnection
                    )
                }
            }
        }
    }
}

@Composable
@PreviewNightLight
private fun CategoryPreview() {
    NewQuizTheme {
        Surface {
            CategoryComponent(
                title = "Title",
                imageUrl = "",
                modifier = Modifier
                    .padding(16.dp)
                    .height(200.dp)
                    .fillMaxWidth()
            )
        }
    }
}
