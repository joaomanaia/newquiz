package com.infinitepower.newquiz.compose.ui.unscramble_word_quiz

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinitepower.newquiz.compose.core.theme.spacing
import androidx.constraintlayout.compose.ConstraintLayout
import com.infinitepower.newquiz.compose.ui.components.text_field.filled.BackgroundOpacity
import com.infinitepower.newquiz.compose.ui.components.text_field.filled.IconOpacity
import com.infinitepower.newquiz.compose.ui.components.text_field.filled.UnfocusedIndicatorLineOpacity
import com.infinitepower.newquiz.compose.core.theme.NewQuizTheme
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun UnscrambleWordQuizScreen() {
    val unscrambleWordQuizViewModel: UnscrambleWordQuizViewModel = hiltViewModel()

    val unscrambleWord by unscrambleWordQuizViewModel.currentScrambledWord.collectAsState(initial = "")

    UnscrambleWordQuizContent(unscrambleWord = unscrambleWord)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
private fun UnscrambleWordQuizContent(
    unscrambleWord: String
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "Unscramble Word")
                }
            )
        }
    ) { innerPadding ->
        ConstraintLayout(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val (wordsRef, scoreRef, wordRef, wordInputRef, submitButtonRef) = createRefs()

            val spaceMedium = MaterialTheme.spacing.medium
            val spaceLarge = MaterialTheme.spacing.large

            Text(
                text = "1/10 Words",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.constrainAs(wordsRef) {
                    top.linkTo(parent.top, spaceMedium)
                    start.linkTo(parent.start, spaceMedium)
                }
            )
            Text(
                text = "Score: 0",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.constrainAs(scoreRef) {
                    top.linkTo(parent.top, spaceMedium)
                    end.linkTo(parent.end, spaceMedium)
                }
            )
            AnimatedContent(
                targetState = unscrambleWord,
                transitionSpec = {
                    // Compare the incoming number with the previous number.
                    slideInVertically { height -> height } + fadeIn() with slideOutVertically { height -> -height } + fadeOut() using(SizeTransform(clip = false))
                },
                modifier = Modifier.constrainAs(wordRef) {
                    top.linkTo(wordsRef.bottom, spaceLarge)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) { targetWord ->
                Text(
                    text = targetWord,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .constrainAs(wordInputRef) {
                        top.linkTo(wordRef.bottom, spaceLarge)
                        start.linkTo(parent.start, spaceMedium)
                        end.linkTo(parent.end, spaceMedium)
                    },
                placeholder = {
                    Text(text = "Word")
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                    backgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = BackgroundOpacity),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = ContentAlpha.high),
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = UnfocusedIndicatorLineOpacity),
                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                    leadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = IconOpacity),
                    trailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = IconOpacity),
                    errorLeadingIconColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = ContentAlpha.high),
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.high),
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    placeholderColor = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.high)
                )
            )
            Button(
                onClick = {},
                modifier = Modifier.constrainAs(submitButtonRef) {
                    top.linkTo(wordInputRef.bottom, spaceMedium)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) {
                Text(text = "Submit")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun UnscrambleWordQuizContentPreview() {
    NewQuizTheme {
        UnscrambleWordQuizContent(
            unscrambleWord = "ABCDE"
        )
    }
}