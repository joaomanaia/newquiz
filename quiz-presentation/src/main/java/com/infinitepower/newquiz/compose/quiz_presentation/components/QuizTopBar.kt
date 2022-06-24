package com.infinitepower.newquiz.compose.quiz_presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.infinitepower.newquiz.compose.core.R
import com.infinitepower.newquiz.compose.core.theme.spacing
import com.infinitepower.newquiz.compose.ui.RoundCircularProgressIndicator

@Composable
internal fun QuizTopBar(
    modifier: Modifier = Modifier,
    windowHeightSizeClass: WindowHeightSizeClass,
    progressText: String,
    progressIndicatorValue: Float,
    onBackClick: () -> Unit,
) {
    val spaceMedium = MaterialTheme.spacing.medium

    ConstraintLayout(modifier = modifier) {
        val (btnBackRef, progressRef) = createRefs()

        IconButton(
            onClick = onBackClick,
            modifier = Modifier.constrainAs(btnBackRef) {
                top.linkTo(progressRef.top)
                bottom.linkTo(progressRef.bottom)
                start.linkTo(parent.start, spaceMedium)
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = stringResource(id = R.string.back)
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.constrainAs(progressRef) {
                top.linkTo(parent.top, spaceMedium)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            if (windowHeightSizeClass != WindowHeightSizeClass.Compact) {
                RoundCircularProgressIndicator(
                    progress = progressIndicatorValue,
                    modifier = Modifier.size(75.dp),
                )
            }
            Text(
                text = progressText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}