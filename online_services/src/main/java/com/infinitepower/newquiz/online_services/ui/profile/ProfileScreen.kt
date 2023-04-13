package com.infinitepower.newquiz.online_services.ui.profile

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.online_services.model.user.User
import com.infinitepower.newquiz.online_services.ui.profile.components.GoodDayText
import com.infinitepower.newquiz.online_services.ui.profile.components.ProfileCard
import com.infinitepower.newquiz.online_services.ui.profile.components.marker
import com.patrykandpatryk.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatryk.vico.compose.axis.vertical.startAxis
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.line.lineChart
import com.patrykandpatryk.vico.compose.component.textComponent
import com.patrykandpatryk.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatryk.vico.compose.style.ProvideChartStyle
import com.patrykandpatryk.vico.core.entry.entryModelOf
import com.ramcosta.composedestinations.annotation.Destination
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    uiState.user?.let { user ->
        ProfileScreenImpl(
            user = user
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ProfileScreenImpl(
    user: User
) {
    val spaceMedium = MaterialTheme.spacing.medium
    val spaceLarge = MaterialTheme.spacing.large

    val multiChoiceGameData = user.data.multiChoiceQuizData
    val wordleData = user.data.wordleData

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(spaceMedium),
            verticalArrangement = Arrangement.spacedBy(spaceLarge)
        ) {
            item {
                UserInfoContent(
                    modifier = Modifier
                        .padding(spaceMedium)
                        .fillMaxWidth(),
                    name = user.info.fullName,
                    photoUri = user.info.imageUrl.toUri(),
                    levelProgress = user.getLevelProgress()
                )
            }

            item {
                UserXpRow(
                    level = user.level,
                    totalXp = user.totalXp,
                    requiredXP = user.getRequiredXP()
                )
            }

            if (multiChoiceGameData != null) {
                val lastQuizTimes = user.data.multiChoiceQuizData.lastQuizTimes

                item {
                    UserMultiChoiceQuizData(
                        totalQuestionsPlayed = multiChoiceGameData.totalQuestionsPlayed,
                        totalCorrectAnswers = multiChoiceGameData.totalCorrectAnswers,
                        lastQuizTimes = lastQuizTimes
                    )
                }
            }

            if (wordleData != null) {
                item {
                    UserWordleData(
                        totalWordsPlayed = wordleData.wordsPlayed ,
                        totalCorrectWords = wordleData.wordsCorrect,
                    )
                }
            }
        }
    }
}

@Composable
fun UserMultiChoiceQuizData(
    totalQuestionsPlayed: Long,
    totalCorrectAnswers: Long,
    lastQuizTimes: List<Double> = listOf(5.0, 15.0, 10.0, 20.0, 10.0)
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val entryModel = remember {
        entryModelOf(*lastQuizTimes.toTypedArray())
    }

    Column {
        Text(
            text = stringResource(id = CoreR.string.multi_choice_quiz),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(spaceMedium))
        Row(
            horizontalArrangement = Arrangement.spacedBy(spaceMedium)
        ) {
            ProfileCard(
                title = totalCorrectAnswers,
                description = stringResource(id = CoreR.string.correct_answers),
                modifier = Modifier.weight(1f)
            )
            ProfileCard(
                title = totalQuestionsPlayed,
                description = stringResource(id = CoreR.string.total_questions),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(spaceMedium))
        Text(
            text = stringResource(id = CoreR.string.last_quiz_times),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.height(spaceMedium))
        ProvideChartStyle(chartStyle = m3ChartStyle()) {
            Chart(
                chart = lineChart(),
                model = entryModel,
                startAxis = startAxis(
                    guideline = null,
                    titleComponent = textComponent(),
                    title = stringResource(id = CoreR.string.time)
                ),
                bottomAxis = bottomAxis(
                    guideline = null,
                    titleComponent = textComponent(),
                    title = stringResource(id = CoreR.string.last_questions)
                ),
                marker = marker(),
            )
        }
    }
}

@Composable
fun UserWordleData(
    totalWordsPlayed: Long,
    totalCorrectWords: Long
) {
    val spaceMedium = MaterialTheme.spacing.medium

    Column {
        Text(
            text = stringResource(id = CoreR.string.wordle),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(spaceMedium))
        Row(
            horizontalArrangement = Arrangement.spacedBy(spaceMedium)
        ) {
            ProfileCard(
                title = totalCorrectWords,
                description = stringResource(id = CoreR.string.correct_words),
                modifier = Modifier.weight(1f)
            )
            ProfileCard(
                title = totalWordsPlayed,
                description = stringResource(id = CoreR.string.total_words),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun UserXpRow(
    level: Int,
    totalXp: Long,
    requiredXP: Long
) {
    val spaceMedium = MaterialTheme.spacing.medium

    Column {
        Text(
            text = stringResource(id = CoreR.string.user_xp),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(spaceMedium))
        Row(
            horizontalArrangement = Arrangement.spacedBy(spaceMedium)
        ) {
            ProfileCard(
                title = level,
                description = stringResource(id = CoreR.string.level),
                modifier = Modifier.weight(1f)
            )
            ProfileCard(
                title = totalXp,
                description = stringResource(id = CoreR.string.current_xp),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(spaceMedium))
        ProfileCard(
            title = requiredXP,
            description = stringResource(id = CoreR.string.required_xp_to_next_level),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun UserInfoContent(
    modifier: Modifier = Modifier,
    name: String,
    photoUri: Uri,
    levelProgress: Float
) {
    val spaceMedium = MaterialTheme.spacing.medium

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = levelProgress,
                modifier = Modifier.size(75.dp)
            )

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .crossfade(true)
                    .data(photoUri)
                    .build(),
                contentDescription = stringResource(id = CoreR.string.photo_of_s, name),
                placeholder = rememberVectorPainter(Icons.Rounded.Person),
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.width(spaceMedium))
        GoodDayText(name = name)
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun ProfileScreenPreview() {
    val uiState by remember {
        val user = User(
            info = User.UserInfo(fullName = "NewQuiz"),
            data = User.UserData(
                totalXp = 2361,
                multiChoiceQuizData = User.UserData.MultiChoiceQuizData(
                    totalQuestionsPlayed = 23,
                    totalCorrectAnswers = 14
                )
            )
        )

        mutableStateOf(ProfileScreenUiState(user = user))
    }

    NewQuizTheme {
        uiState.user?.let { user ->
            ProfileScreenImpl(
                user = user
            )
        }
    }
}

@Composable
@PreviewNightLight
private fun UserInfoContentPreview() {
    NewQuizTheme {
        Surface {
            UserInfoContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                name = "NewQuiz",
                photoUri = Uri.EMPTY,
                levelProgress = 0.8f
            )
        }
    }
}

@Composable
@PreviewNightLight
private fun ProfileCardPreview() {
    NewQuizTheme {
        Surface {
            ProfileCard(
                modifier = Modifier.padding(16.dp),
                title = "348",
                description = "XP"
            )
        }
    }
}