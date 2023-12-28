package com.infinitepower.newquiz.ui.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.infinitepower.newquiz.core.navigation.NavigationItem
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.ui.components.DiamondsCounter
import com.ramcosta.composedestinations.navigation.navigate
import kotlinx.coroutines.launch

/**
 * Container with navigation rail and modal drawer
 */
@Composable
@ExperimentalMaterial3Api
internal fun MediumContainer(
    navController: NavController,
    primaryItems: List<NavigationItem.Item>,
    navDrawerItems: List<NavigationItem>,
    selectedItem: NavigationItem.Item?,
    userDiamonds: UInt = 0u,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    val text = selectedItem?.text?.let { id ->
        stringResource(id = id)
    } ?: "NewQuiz"

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
                }
            )
        }
    ) {
        Row {
            NavigationRail(
                header = {
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
            ) {
                primaryItems.forEach { item ->
                    NavigationRailItem(
                        selected = item == selectedItem,
                        onClick = { navController.navigate(item.direction) },
                        icon = {
                            Icon(
                                imageVector = item.getIcon(item == selectedItem),
                                contentDescription = stringResource(id = item.text)
                            )
                        }
                    )
                }
            }

            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    CenterAlignedTopAppBar(
                        scrollBehavior = scrollBehavior,
                        title = {
                            Text(text = text)
                        },
                        actions = {
                            DiamondsCounter(
                                diamonds = userDiamonds,
                                modifier = Modifier
                            )
                        }
                    )
                },
                content = content
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    device = "spec:width=673.5dp,height=841dp,dpi=480",
    group = "Medium"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=673.5dp,height=841dp,dpi=480",
    group = "Medium"
)
@OptIn(ExperimentalMaterial3Api::class)
private fun MediumContainerPreview() {
    val selectedItem = getNavigationItems(dailyChallengeClaimCount = 5)
        .filterIsInstance<NavigationItem.Item>()
        .firstOrNull()

    NewQuizTheme {
        Surface {
            MediumContainer(
                navController = rememberNavController(),
                content = {
                    Text(text = "NewQuiz")
                },
                primaryItems = getNavigationItems().filterIsInstance<NavigationItem.Item>(),
                navDrawerItems = getNavigationItems(),
                selectedItem = selectedItem,
            )
        }
    }
}
