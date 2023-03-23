package com.infinitepower.newquiz.comparison_quiz.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.common.compose.preview.BooleanPreviewParameterProvider
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import java.text.NumberFormat
import java.util.Locale

@Composable
internal fun ComparisonItem(
    modifier: Modifier = Modifier,
    item: ComparisonQuizItem,
    helperValueSuffix: String? = null,
    helperContentAlignment: Alignment,
    helperValueState: HelperValueState = HelperValueState.HIDDEN,
    onClick: () -> Unit
) {
    val numberFormat = remember { NumberFormat.getNumberInstance(Locale.getDefault()) }

    val helperValue = remember(item.value) {
        val numberFormatted = numberFormat.format(item.value)

        if (helperValueSuffix != null) "$numberFormatted $helperValueSuffix" else numberFormatted
    }

    ComparisonItem(
        modifier = modifier,
        title = item.title,
        image = item.imgUri,
        helperValue = helperValue,
        helperContentAlignment = helperContentAlignment,
        helperValueState = helperValueState,
        onClick = onClick
    )
}

enum class HelperValueState { NORMAL, HIDDEN }

@Composable
private fun ComparisonItem(
    modifier: Modifier = Modifier,
    title: String,
    image: Uri,
    helperValue: String,
    helperContentAlignment: Alignment,
    helperValueState: HelperValueState,
    onClick: () -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val helperColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)

    val context = LocalContext.current

    val imageLoader = ImageLoader
        .Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }.build()

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        onClick = onClick,
        tonalElevation = 8.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                imageLoader = imageLoader
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(helperContentAlignment)
                    .padding(spaceMedium),
                tonalElevation = 8.dp,
                shape = MaterialTheme.shapes.large,
                color = helperColor
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.padding(spaceMedium)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (helperValueState == HelperValueState.NORMAL) {
                        Divider(
                            modifier = Modifier
                                .height(24.dp)
                                .width(DividerDefaults.Thickness)
                        )
                        Text(
                            text = helperValue,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
@PreviewNightLight
private fun ComparisonQuizScreenPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) firstItem: Boolean
) {
    val alignment = if (firstItem) Alignment.TopCenter else Alignment.BottomCenter

    NewQuizTheme {
        Surface {
            ComparisonItem(
                modifier = Modifier
                    .padding(16.dp)
                    .size(400.dp),
                title = "NewQuiz",
                image = Uri.EMPTY,
                helperValue = "12,345",
                onClick = {},
                helperContentAlignment = alignment,
                helperValueState = HelperValueState.NORMAL
            )
        }
    }
}