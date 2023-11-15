package com.infinitepower.newquiz.core.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.common.compose.preview.BooleanPreviewParameterProvider
import com.infinitepower.newquiz.core.theme.NewQuizTheme

@Composable
internal fun ExpandCategoriesButton(
    modifier: Modifier = Modifier,
    seeAllCategories: Boolean,
    onSeeAllCategoriesClick: () -> Unit
) {
    val transition = updateTransition(
        targetState = seeAllCategories,
        label = "Expand Categories Button"
    )

    val seeAllText = if (seeAllCategories) {
        stringResource(id = R.string.see_less_categories)
    } else {
        stringResource(id = R.string.see_all_categories)
    }

    // Rotates the icon when the button is pressed
    val rotation by transition.animateFloat(
        label = "Icon Rotation",
    ) { state ->
        if (state) 180f else 0f
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        TextButton(onClick = onSeeAllCategoriesClick) {
            Icon(
                imageVector = Icons.Rounded.ExpandMore,
                contentDescription = seeAllText,
                modifier = Modifier
                    .size(ButtonDefaults.IconSize)
                    .graphicsLayer {
                        rotationZ = rotation
                    }
            )
            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
            Text(
                text = seeAllText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.animateContentSize()
            )
        }
    }
}

@Composable
@PreviewNightLight
private fun ExpandCategoriesButtonPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) seeAllCategories: Boolean
) {
    NewQuizTheme {
        Surface {
            ExpandCategoriesButton(
                seeAllCategories = seeAllCategories,
                onSeeAllCategoriesClick = {}
            )
        }
    }
}
