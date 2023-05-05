package com.infinitepower.newquiz.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.infinitepower.newquiz.core.navigation.NavigationItem
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.ui.components.SignInCard
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@ExperimentalMaterial3Api
internal fun NavigationDrawerContent(
    modifier: Modifier = Modifier,
    permanent: Boolean = false,
    items: List<NavigationItem>,
    selectedItem: NavigationItem.Item?,
    showLoginCard: Boolean,
    onItemClick: (item: NavigationItem.Item) -> Unit,
    onSignInClick: () -> Unit,
    onSignDismissClick: () -> Unit
) {
    NavigationDrawerContainer(
        modifier = modifier,
        permanent = permanent
    ) {
        NavigationDrawerContent(
            items = items,
            selectedItem = selectedItem,
            onItemClick = onItemClick,
            onSignInClick = onSignInClick,
            onSignDismissClick = onSignDismissClick,
            showLoginCard = showLoginCard
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun NavigationDrawerContainer(
    modifier: Modifier = Modifier,
    permanent: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    if (permanent) {
        PermanentDrawerSheet(
            modifier = modifier,
            content = content
        )
    } else {
        ModalDrawerSheet(
            modifier = modifier,
            content = content
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun NavigationDrawerContent(
    modifier: Modifier = Modifier,
    items: List<NavigationItem>,
    selectedItem: NavigationItem.Item?,
    showLoginCard: Boolean,
    onItemClick: (item: NavigationItem.Item) -> Unit,
    onSignInClick: () -> Unit,
    onSignDismissClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = NavigationDrawerItemDefaults.ItemPadding
    ) {
        item {
            Column(
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.large)
            ) {
                Text(
                    text = stringResource(id = CoreR.string.app_name),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        if (showLoginCard) {
            item {
                SignInCard(
                    onSignInClick = onSignInClick,
                    onDismissClick = onSignDismissClick
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            }
        }

        itemsIndexed(items = items) { index, item ->
            val text = stringResource(id = item.text)

            when (item) {
                is NavigationItem.Label -> {
                    if (items.getOrNull(index - 1) is NavigationItem.Item) {
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    }
                    NavigationDrawerLabel(text = text)
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                }
                is NavigationItem.Item -> {
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
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun NavDrawerIconWithBadge(
    item: NavigationItem.Item,
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
private fun NavigationDrawerLabel(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge
    )
}