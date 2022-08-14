package com.infinitepower.newquiz.core.ui.home_card.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem

@Composable
@ExperimentalMaterial3Api
fun HomeGroupTitle(
    modifier: Modifier = Modifier,
    data: HomeCardItem.GroupTitle
) {
    Surface(
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = data.title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium)
        )
    }
}