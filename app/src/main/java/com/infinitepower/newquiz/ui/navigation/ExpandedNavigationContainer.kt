package com.infinitepower.newquiz.ui.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * Container with permanent navigation drawer
 */
@Composable
@ExperimentalMaterial3Api
internal fun ExpandedContainer(
    navController: NavController,
    primaryItems: ImmutableList<NavigationItem.Item>,
    otherItems: ImmutableList<NavigationItem>,
    selectedItem: NavigationItem.Item?,
    userDiamonds: UInt = 0u,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    val text = selectedItem?.text?.let { id ->
        stringResource(id = id)
    } ?: "NewQuiz"

    val allNavigationItems = remember(primaryItems, otherItems) {
        (primaryItems + otherItems).toImmutableList()
    }

    PermanentNavigationDrawer(
        drawerContent = {
            NavigationDrawerContent(
                modifier = Modifier.fillMaxHeight(),
                permanent = true,
                selectedItem = selectedItem,
                items = allNavigationItems,
                onItemClick = { item ->
                    navController.navigate(item.direction)
                },
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

@Composable
@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=480",
    group = "Expanded"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=1280dp,height=800dp,dpi=480",
    group = "Expanded"
)
@OptIn(ExperimentalMaterial3Api::class)
private fun MediumContainerPreview() {
    val otherItems = getOtherItems()

    val selectedItem = otherItems
        .filterIsInstance<NavigationItem.Item>()
        .firstOrNull()

    NewQuizTheme {
        Surface {
            ExpandedContainer(
                navController = rememberNavController(),
                content = {
                    Text(text = "NewQuiz")
                },
                primaryItems = getPrimaryItems(),
                otherItems = otherItems,
                selectedItem = selectedItem,
                userDiamonds = 100u
            )
        }
    }
}
