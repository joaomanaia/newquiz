package com.infinitepower.newquiz.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.infinitepower.newquiz.core.navigation.NavigationItem
import com.infinitepower.newquiz.core.theme.spacing

@Composable
@ExperimentalMaterial3Api
internal fun NavigationDrawerContent(
    modifier: Modifier = Modifier,
    permanent: Boolean = false,
    items: List<NavigationItem>,
    selectedItem: NavigationItem.Item?,
    onItemClick: (item: NavigationItem.Item) -> Unit
) {
    NavigationDrawerContainer(
        modifier = modifier,
        permanent = permanent
    ) {
        NavigationDrawerContent(
            items = items,
            selectedItem = selectedItem,
            onItemClick = onItemClick
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
    onItemClick: (item: NavigationItem.Item) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = NavigationDrawerItemDefaults.ItemPadding
    ) {
        item {
            NavigationDrawerHeader()
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