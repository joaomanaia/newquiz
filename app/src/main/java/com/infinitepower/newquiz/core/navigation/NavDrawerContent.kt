package com.infinitepower.newquiz.core.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavController
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.home_presentation.destinations.HomeScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizListScreenDestination
import com.infinitepower.newquiz.online_services.ui.profile.destinations.ProfileScreenDestination
import com.infinitepower.newquiz.settings_presentation.destinations.SettingsScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleListScreenDestination
import com.infinitepower.newquiz.math_quiz.list.destinations.MathQuizListScreenDestination
import com.infinitepower.newquiz.core.R as CoreR
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import kotlinx.coroutines.launch

private val navDrawerItems = listOf(
    NavDrawerItem.Item(
        text = CoreR.string.home,
        icon = Icons.Rounded.Home,
        direction = HomeScreenDestination
    ),
    NavDrawerItem.Item(
        text = CoreR.string.multi_choice_quiz,
        icon = Icons.Rounded.Quiz,
        direction = MultiChoiceQuizListScreenDestination
    ),
    NavDrawerItem.Item(
        text = CoreR.string.wordle,
        icon = Icons.Rounded.Quiz,
        direction = WordleListScreenDestination
    ),
    NavDrawerItem.Item(
        text = CoreR.string.math_quiz,
        icon = Icons.Rounded.Numbers,
        direction = MathQuizListScreenDestination
    ),
    NavDrawerItem.Label(
        text = CoreR.string.online,
        group = NavDrawerItemGroup("online")
    ),
    NavDrawerItem.Item(
        text = CoreR.string.profile,
        icon = Icons.Rounded.Person,
        direction = ProfileScreenDestination,
        group = NavDrawerItemGroup("online")
    ),
    NavDrawerItem.Label(text = CoreR.string.other),
    NavDrawerItem.Item(
        text = CoreR.string.settings,
        icon = Icons.Rounded.Settings,
        direction = SettingsScreenDestination()
    ),
)

private fun getDrawerItemBy(route: DestinationSpec<*>?) = navDrawerItems
    .filterIsInstance<NavDrawerItem.Item>()
    .find { item ->
        item.direction == route
    }

@Composable
@ExperimentalMaterial3Api
fun NavigationDrawerContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    drawerState: DrawerState,
    signedIn: Boolean,
    onItemClick: (item: NavDrawerItem.Item) -> Unit
) {
    val destination by navController.currentDestinationAsState()

    val selectedItem = getDrawerItemBy(destination)

    val items = remember(signedIn) {
        if (signedIn) {
            navDrawerItems
        } else {
            navDrawerItems.filterNot { it.group == NavDrawerItemGroup("online") }
        }
    }

    NavigationDrawerContentImpl(
        modifier = modifier,
        drawerState = drawerState,
        items = items,
        selectedItem = selectedItem,
        onItemClick = onItemClick
    )
}

@Composable
@ExperimentalMaterial3Api
private fun NavigationDrawerContentImpl(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    items: List<NavDrawerItem>,
    selectedItem: NavDrawerItem.Item?,
    onItemClick: (item: NavDrawerItem.Item) -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet(modifier = modifier) {
        LazyColumn(
            contentPadding = NavigationDrawerItemDefaults.ItemPadding
        ) {
            item {
                NavigationDrawerHeader()
            }

            itemsIndexed(items = items) { index, item ->
                val text = stringResource(id = item.text)

                when (item) {
                    is NavDrawerItem.Label -> {
                        if (items.getOrNull(index - 1) is NavDrawerItem.Item) {
                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                        }
                        NavigationDrawerLabel(text = text)
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    }
                    is NavDrawerItem.Item -> {
                        NavigationDrawerItem(
                            icon = {
                                NavDrawerIconWithBadge(
                                    item = item,
                                    icon = {
                                        Icon(item.icon, contentDescription = text)
                                    }
                                )
                            },
                            label = { Text(text = text) },
                            selected = item == selectedItem,
                            onClick = {
                                scope.launch { drawerState.close() }
                                onItemClick(item)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun NavDrawerIconWithBadge(
    item: NavDrawerItem.Item,
    icon: @Composable () -> Unit
) {
    if (item.badge == null) {
        icon()
    } else {
        BadgedBox(
            badge = {
                Badge {
                    Text(
                        text = item.badge.value.toString(),
                        modifier = Modifier.semantics {
                            contentDescription = item.badge.description
                        }
                    )
                }
            }
        ) {
            icon()
        }
    }
}

@Composable
private fun NavigationDrawerHeader() {
    Column(
        modifier = Modifier.padding(vertical = MaterialTheme.spacing.large)
    ) {
        Text(
            text = "NewQuiz",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
        Text(text = "Increase you knowledge by playing NewQuiz!")
    }
}

@Composable
private fun NavigationDrawerLabel(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge
    )
}