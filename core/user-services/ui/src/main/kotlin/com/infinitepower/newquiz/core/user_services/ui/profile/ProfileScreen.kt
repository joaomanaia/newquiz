package com.infinitepower.newquiz.core.user_services.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.core.user_services.model.User
import com.infinitepower.newquiz.core.user_services.ui.profile.components.MainUserCard
import com.infinitepower.newquiz.core.user_services.ui.profile.components.UserXpAndLevelCard
import com.infinitepower.newquiz.core.user_services.ui.profile.components.XpEarnedByDayCard
import com.infinitepower.newquiz.core.R as CoreR
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun ProfileScreen(
    navigator: DestinationsNavigator,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreen(
        uiState = uiState,
        onBackClick = navigator::popBackStack
    )
}

@Composable
@ExperimentalMaterial3Api
private fun ProfileScreen(
    uiState: ProfileScreenUiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = CoreR.string.profile)) },
                navigationIcon = { BackIconButton(onClick = onBackClick) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
        ) {
            if (uiState.loading) {
                item { CircularProgressIndicator() }
            }

            uiState.user?.let { user ->
                item {
                    MainUserCard(
                        modifier = Modifier.fillParentMaxWidth(),
                        level = user.level,
                        levelProgress = user.getLevelProgress(),
                        userName = user.fullName,
                        userPhoto = user.imageUrl
                    )
                }

                item {
                    UserXpAndLevelCard(
                        modifier = Modifier.fillParentMaxWidth(),
                        totalXp = user.totalXp,
                        level = user.level
                    )
                }

                item {
                    Text(
                        text = "XP earned last 7 days",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.padding(top = MaterialTheme.spacing.medium))
                    OutlinedCard(
                        modifier = Modifier.fillParentMaxWidth()
                    ) {
                        XpEarnedByDayCard(
                            modifier = Modifier.padding(MaterialTheme.spacing.medium),
                            xpEarnedByDay = uiState.xpEarnedLast7Days
                        )
                    }
                }
            }
        }
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun ProfileScreenPreview() {
    val now = Clock.System.now()
    val tz = TimeZone.currentSystemDefault()
    val today = now.toLocalDateTime(tz).date

    NewQuizTheme {
        ProfileScreen(
            uiState = ProfileScreenUiState(
                loading = false,
                user = User(
                    uid = "uid",
                    totalXp = 1235u
                ),
                xpEarnedLast7Days = mapOf(
                    (now - 4.days).toLocalDateTime(tz).date to 20,
                    (now - 3.days).toLocalDateTime(tz).date to 10,
                    (now - 1.days).toLocalDateTime(tz).date to 30,
                    today to 15
                )
            ),
            onBackClick = {}
        )
    }
}