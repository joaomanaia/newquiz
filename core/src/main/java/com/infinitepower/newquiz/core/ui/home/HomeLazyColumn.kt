package com.infinitepower.newquiz.core.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.infinitepower.newquiz.core.theme.spacing

@Composable
fun HomeLazyColumn(
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(spaceMedium),
        contentPadding = PaddingValues(
            start = spaceMedium,
            end = spaceMedium,
            bottom = MaterialTheme.spacing.large,
        ),
        content = content
    )
}