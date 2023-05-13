package com.infinitepower.newquiz.online_services.ui.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.text.CompactDecimalText

@Composable
internal fun ProfileCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String
) {
    ProfileCard(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        description = description
    )
}

@Composable
internal fun ProfileCard(
    modifier: Modifier = Modifier,
    title: ULong,
    description: String
) {
    ProfileCard(
        modifier = modifier,
        title = {
            CompactDecimalText(
                value = title.toLong(),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        description = description
    )
}

@Composable
private fun ProfileCard(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    description: String
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            title()
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}