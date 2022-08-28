package com.infinitepower.newquiz.multi_choice_quiz.categories.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionCategory
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@ExperimentalMaterial3Api
internal fun CategoryComponent(
    modifier: Modifier = Modifier,
    category: MultiChoiceQuestionCategory,
    maxLines: Int = 2,
    onClick: () -> Unit
) {
    CategoryComponentImpl(
        modifier = modifier,
        category = category,
        maxLines = maxLines,
        onClick = onClick
    )
}

@Composable
@ExperimentalMaterial3Api
private fun CategoryComponentImpl(
    modifier: Modifier = Modifier,
    category: MultiChoiceQuestionCategory,
    maxLines: Int = 2,
    onClick: () -> Unit
) {
    val mediumShape = MaterialTheme.shapes.medium
    val mediumSpace = MaterialTheme.spacing.medium

    Card(
        modifier = modifier.width(175.dp),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val categoryName = stringResource(id = category.name)

            AsyncImage(
                model = category.image,
                contentDescription = categoryName,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(mediumShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(mediumSpace))
            Text(
                text = categoryName,
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
                category = MultiChoiceQuestionCategory(
                    id = 0,
                    name = CoreR.string.animals,
                    image = CoreR.drawable.animals
                ),
                modifier = Modifier.padding(16.dp),
                onClick = {}
            )
        }
    }
}