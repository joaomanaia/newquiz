package com.infinitepower.newquiz.wordle.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.common.compose.preview.BooleanPreviewParameterProvider
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.wordle.WordleChar
import com.infinitepower.newquiz.model.wordle.WordleItem
import com.infinitepower.newquiz.model.wordle.WordleRowItem

@Composable
internal fun InfoDialog(
    isColorBlindEnabled: Boolean,
    onDismissRequest: () -> Unit
) {
    // Word: QUIZ
    val rowItem = WordleRowItem(
        items = listOf(
            WordleItem.fromChar('Q'), // None
            WordleItem.Present(WordleChar('U')),
            WordleItem.Correct(WordleChar('I')),
            WordleItem.Present(WordleChar('Z')),
        )
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(id = R.string.info))
        },
        text = {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    WordleRowComponent(
                        wordleRowItem = rowItem,
                        word = "QUIZ",
                        enabled = false, // Disable click
                        animationEnabled = false,
                        isColorBlindEnabled = isColorBlindEnabled,
                        onItemClick = {}
                    )
                }

                item { Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium)) }

                item {
                    InfoDialogCard(isColorBlindEnabled = isColorBlindEnabled)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.close))
            }
        }
    )
}

@Composable
private fun InfoDialogCard(
    isColorBlindEnabled: Boolean
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val presentBackgroundColor = getItemRowBackgroundColor(
        item = WordleItem.Present(WordleChar('C')),
        isColorBlindEnabled = isColorBlindEnabled
    )
    val presentTextColor = getItemRowTextColor(
        item = WordleItem.Present(WordleChar('C')),
        isColorBlindEnabled = isColorBlindEnabled
    )

    val correctBackgroundColor = getItemRowBackgroundColor(
        item = WordleItem.Correct(WordleChar('C')),
        isColorBlindEnabled = isColorBlindEnabled
    )
    val correctTextColor = getItemRowTextColor(
        item = WordleItem.Correct(WordleChar('C')),
        isColorBlindEnabled = isColorBlindEnabled
    )

    Card {
        Column(
            modifier = Modifier.padding(spaceMedium),
            verticalArrangement = Arrangement.spacedBy(spaceMedium)
        ) {
            // Char none: Q
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = ITEM_DESCRIPTION_FONT_SIZE,
                        )
                    ) {
                        append('Q')
                    }
                    append(stringResource(id = R.string.is_not_in_the_target_word_wordle))
                }
            )

            // Chars present: U, Z
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            background = presentBackgroundColor,
                            color = presentTextColor,
                            fontSize = ITEM_DESCRIPTION_FONT_SIZE,
                        )
                    ) {
                        append(" U ")
                    }
                    append(" , ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            background = presentBackgroundColor,
                            color = presentTextColor,
                            fontSize = ITEM_DESCRIPTION_FONT_SIZE
                        )
                    ) {
                        append(" Z ")
                    }
                    append(stringResource(id = R.string.is_in_the_word_but_in_the_wrong_spot_wordle))
                }
            )

            // Char correct: I
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            background = correctBackgroundColor,
                            color = correctTextColor,
                            fontSize = ITEM_DESCRIPTION_FONT_SIZE
                        )
                    ) {
                        append(" I ")
                    }
                    append(stringResource(id = R.string.is_in_the_word_and_in_the_correct_spot_wordle))
                }
            )
        }
    }
}

private val ITEM_DESCRIPTION_FONT_SIZE = 18.sp

@Composable
@PreviewLightDark
private fun InfoDialogPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) isColorBlindEnabled: Boolean
) {
    NewQuizTheme {
        InfoDialog(
            isColorBlindEnabled = isColorBlindEnabled,
            onDismissRequest = {}
        )
    }
}
