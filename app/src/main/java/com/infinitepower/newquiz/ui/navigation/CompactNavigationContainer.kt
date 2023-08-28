package com.infinitepower.newquiz.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.navigation.NavigationItem
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.ramcosta.composedestinations.navigation.navigate
import kotlinx.coroutines.launch

/**
 * Container with navigation bar and modal drawer
 */
@Composable
@ExperimentalMaterial3Api
internal fun CompactContainer(
    navController: NavController,
    primaryItems: List<NavigationItem.Item>,
    navDrawerItems: List<NavigationItem>,
    selectedItem: NavigationItem.Item?,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    showLoginCard: Boolean,
    onSignInClick: () -> Unit,
    onSignDismissClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()

    val text = selectedItem?.text?.let { id ->
        stringResource(id = id)
    } ?: "NewQuiz"

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                modifier = Modifier.fillMaxHeight(),
                permanent = false,
                selectedItem = selectedItem,
                items = navDrawerItems,
                onItemClick = { item ->
                    scope.launch { drawerState.close() }
                    navController.navigate(item.direction)
                },
                onSignInClick = onSignInClick,
                onSignDismissClick = onSignDismissClick,
                showLoginCard = showLoginCard
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = text)
                    },
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Menu,
                                contentDescription = "Open menu"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    primaryItems.forEach { item ->
                        NavigationBarItem(
                            selected = item == selectedItem,
                            onClick = {
                                navController.navigate(item.direction) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id)
                                    // Avoid multiple copies of the same destination when re-selecting the same item
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = stringResource(id = item.text)
                                )
                            }
                        )
                    }
                }
            },
            content = content
        )
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun CompactContainerPreview() {
    val selectedItem = getNavigationItems()
        .filterIsInstance<NavigationItem.Item>()
        .firstOrNull()

    NewQuizTheme {
        Surface {
            CompactContainer(
                navController = rememberNavController(),
                content = {
                    Text(text = "NewQuiz")
                },
                primaryItems = getNavigationItems().filterIsInstance<NavigationItem.Item>(),
                navDrawerItems = getNavigationItems(),
                selectedItem = selectedItem,
                onSignInClick = {},
                onSignDismissClick = {},
                showLoginCard = true
            )
        }
    }
}