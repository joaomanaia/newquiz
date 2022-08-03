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
import androidx.navigation.NavController
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.home_presentation.destinations.HomeScreenDestination
import com.infinitepower.newquiz.settings_presentation.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import kotlinx.coroutines.launch

private val navDrawerItems = listOf(
    NavigationDrawerItem.Item(
        text = "Normal Quiz",
        icon = Icons.Rounded.Quiz,
        direction = HomeScreenDestination
    ),
    NavigationDrawerItem.Item(
        text = "Wordle",
        icon = Icons.Rounded.Quiz,
        direction = HomeScreenDestination
    ),
    NavigationDrawerItem.Label(text = "Other"),
    NavigationDrawerItem.Item(
        text = "Settings",
        icon = Icons.Rounded.Settings,
        direction = SettingsScreenDestination()
    ),
)

private fun getDrawerItemBy(route:  DestinationSpec<*>?) = navDrawerItems
    .filterIsInstance<NavigationDrawerItem.Item>()
    .find { item ->
        item.direction == route
    }

@Composable
@ExperimentalMaterial3Api
fun NavigationDrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    onItemClick: (item: NavigationDrawerItem.Item) -> Unit
) {
    val destination by navController.currentDestinationAsState()

    val selectedItem = getDrawerItemBy(destination)

    val items = remember { navDrawerItems }

    NavigationDrawerContentImpl(
        drawerState = drawerState,
        items = items,
        selectedItem = selectedItem,
        onItemClick = onItemClick
    )
}

@Composable
@ExperimentalMaterial3Api
private fun NavigationDrawerContentImpl(
    drawerState: DrawerState,
    items: List<NavigationDrawerItem>,
    selectedItem: NavigationDrawerItem.Item?,
    onItemClick: (item: NavigationDrawerItem.Item) -> Unit
) {
    val scope = rememberCoroutineScope()

    Surface {
        LazyColumn(
            contentPadding = NavigationDrawerItemDefaults.ItemPadding
        ) {
            item {
                NavigationDrawerHeader()
            }

            itemsIndexed(items = items) { index, item ->
                when (item) {
                    is NavigationDrawerItem.Label -> {
                        if (items.getOrNull(index - 1) is NavigationDrawerItem.Item) {
                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                        }
                        NavigationDrawerLabel(text = item.text)
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    }
                    is NavigationDrawerItem.Item -> {
                        NavigationDrawerItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.text) },
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