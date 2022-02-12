package com.infinitepower.newquiz.compose.ui.saved_questions_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.infinitepower.newquiz.compose.core.common.spacing
import com.infinitepower.newquiz.compose.data.local.question.Question
import com.infinitepower.newquiz.compose.data.local.question.getBasicQuestion
import com.infinitepower.newquiz.compose.ui.saved_questions_list.SavedQuestionsListEvent
import com.infinitepower.newquiz.compose.ui.theme.NewQuizTheme

@Composable
@OptIn(ExperimentalMaterialApi::class)
internal fun SavedQuestionItem(
    modifier: Modifier = Modifier,
    question: Question,
    checked: Boolean = false,
    onEvent: (event: SavedQuestionsListEvent) -> Unit
) {
    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd) {
                onEvent(SavedQuestionsListEvent.OnDeleteQuestionClick(question))
            }
            true
        }
    )

    LaunchedEffect(key1 = true) {
        dismissState.reset()
    }

    SwipeToDismiss(
        state = dismissState,
        background = {
            val color = when(dismissState.dismissDirection) {
                DismissDirection.EndToStart -> Color.Transparent
                DismissDirection.StartToEnd -> MaterialTheme.colorScheme.primary
                null -> Color.Transparent
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = color),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete Icon",
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.padding(start = MaterialTheme.spacing.medium)
                )
            }
        },
        directions = setOf(DismissDirection.StartToEnd),
        modifier = modifier
    ) {
        Surface(
            onClick = {
                onEvent(SavedQuestionsListEvent.OnQuestionClick(question))
            },
            role = Role.Button,
            indication = rememberRipple(),
            modifier = modifier
        ) {
            Row(
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        onEvent(SavedQuestionsListEvent.OnQuestionClick(question))
                    }
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                Text(
                    text = question.description,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
internal fun SavedQuestionItemPlaceHolder(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = false,
                onCheckedChange = {},
                enabled = false
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
            Text(
                text = "",
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.secondary),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SavedQuestionItemPreview() {
    var item1Selected by remember { mutableStateOf(true) }
    var item2Selected by remember { mutableStateOf(false) }

    NewQuizTheme {
        LazyColumn {
            item {
                SavedQuestionItem(
                    modifier = Modifier.fillMaxWidth(),
                    question = getBasicQuestion(),
                    checked = item1Selected,
                    onEvent = {
                        if (it is SavedQuestionsListEvent.OnQuestionClick)
                            item1Selected = !item1Selected
                    }
                )
            }
            item {
                SavedQuestionItem(
                    modifier = Modifier.fillMaxWidth(),
                    question = getBasicQuestion(),
                    checked = item2Selected,
                    onEvent = {
                        if (it is SavedQuestionsListEvent.OnQuestionClick)
                            item2Selected = !item2Selected
                    }
                )
            }
        }
    }
}