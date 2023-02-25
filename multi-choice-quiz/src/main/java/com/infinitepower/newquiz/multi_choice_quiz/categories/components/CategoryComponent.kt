package com.infinitepower.newquiz.multi_choice_quiz.categories.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.core.R as CoreR

private val CARD_WIDTH = 175.dp

@Composable
@ExperimentalMaterial3Api
internal fun CategoryComponent(
    modifier: Modifier = Modifier,
    category: MultiChoiceCategory,
    maxLines: Int = 2,
    onClick: () -> Unit
) {
    CategoryComponent(
        modifier = modifier,
        name = category.name.asString(),
        image = category.image,
        maxLines = maxLines,
        onClick = onClick
    )
}

@Composable
@ExperimentalMaterial3Api
private fun CategoryComponent(
    modifier: Modifier = Modifier,
    name: String,
    image: Any?,
    maxLines: Int = 2,
    onClick: () -> Unit
) {
    val mediumShape = MaterialTheme.shapes.medium
    val mediumSpace = MaterialTheme.spacing.medium

    Card(
        modifier = modifier.width(CARD_WIDTH),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = image,
                contentDescription = name,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(mediumShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(mediumSpace))
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = maxLines,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun CategoryComponentPreview() {
    NewQuizTheme {
        Surface {
            CategoryComponent(
                name = "Animals",
                image = CoreR.drawable.logo_monochromatic,
                modifier = Modifier.padding(16.dp),
                onClick = {}
            )
        }
    }
}