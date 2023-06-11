package com.infinitepower.newquiz.core.ui.components.skip_question

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.infinitepower.newquiz.core.R

@Composable
fun SkipIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Rounded.SkipNext,
            contentDescription = stringResource(id = R.string.skip)
        )
    }
}
