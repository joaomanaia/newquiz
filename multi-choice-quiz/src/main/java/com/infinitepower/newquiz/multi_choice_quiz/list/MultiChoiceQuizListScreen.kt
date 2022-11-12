package com.infinitepower.newquiz.multi_choice_quiz.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.home_card.components.HomeCardItemContent
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.core.util.ui.nav_drawer.NavDrawerUtil
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionCategory
import com.infinitepower.newquiz.multi_choice_quiz.categories.components.CategoryComponent
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceCategoriesScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.list.data.getMultiChoiceQuizListCardItemData
import com.infinitepower.newquiz.core.R as CoreR
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun MultiChoiceQuizListScreen(
    navigator: DestinationsNavigator,
    navDrawerUtil: NavDrawerUtil,
    viewModel: MultiChoiceQuizListScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val questionsAvailableText = stringResource(
        id = CoreR.string.n_questions_available,
        uiState.savedQuestionsSize
    )

    val cardItemData = remember(uiState.savedQuestionsSize) {
        getMultiChoiceQuizListCardItemData(
            navigator = navigator,
            savedQuestionsText = questionsAvailableText,
            recentCategories = uiState.recentCategories
        )
    }

    MultiChoiceQuizListScreenImpl(
        cardItemData = cardItemData,
        openDrawer = navDrawerUtil::open
    )

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("MultiChoiceListScreen")
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MultiChoiceQuizListScreenImpl(
    cardItemData: List<HomeCardItem>,
    openDrawer: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    val spaceMedium = MaterialTheme.spacing.medium

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = CoreR.string.multi_choice_quiz))
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = "Open drawer"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(spaceMedium),
            verticalArrangement = Arrangement.spacedBy(spaceMedium)
        ) {
            items(
                items = cardItemData,
                key = { it.getId() },
            ) { item ->
                HomeCardItemContent(
                    modifier = Modifier.fillParentMaxWidth(),
                    item = item
                )
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
fun MultiChoiceCategoriesSelector(
    recentCategories: List<MultiChoiceQuestionCategory>,
    navigateToCategoriesScreen: () -> Unit,
    navigateToQuizScreen: (category: Int) -> Unit
) {
    val mediumSpace = MaterialTheme.spacing.medium

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(mediumSpace)
    ) {
        items(items = recentCategories) { category ->
            CategoryComponent(
                category = category,
                maxLines = 1,
                onClick = { navigateToQuizScreen(category.id) }
            )
        }

        item {
            SeeAllCategoriesCard(onClick = navigateToCategoriesScreen)
        }
    }
}

@Composable
@ExperimentalMaterial3Api
fun SeeAllCategoriesCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val mediumShape = MaterialTheme.shapes.medium
    val mediumSpace = MaterialTheme.spacing.medium

    OutlinedCard(
        modifier = modifier.requiredWidth(175.dp),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.padding(MaterialTheme.spacing.large)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(id = CoreR.string.see_all),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(mediumShape)
                )
            }
            Spacer(modifier = Modifier.height(mediumSpace))
            Text(
                text = stringResource(id = CoreR.string.see_all),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}