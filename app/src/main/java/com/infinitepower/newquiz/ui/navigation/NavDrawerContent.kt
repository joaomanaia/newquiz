package com.infinitepower.newquiz.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Badge
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
import com.infinitepower.newquiz.core.R as CoreR

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
            onItemClick = onItemClick,
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
    onItemClick: (item: NavigationItem.Item) -> Unit,
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
                            Icon(
                                imageVector = item.getIcon(item == selectedItem),
                                contentDescription = text
                            )
                        },
                        label = { Text(text = text) },
                        selected = item == selectedItem,
                        onClick = { onItemClick(item) },
                        badge = if (item.badge != null && item.badge.value > 0) {
                            {
                                Badge {
                                    Text(
                                        text = item.badge.value.toString(),
                                        modifier = Modifier.semantics {
                                            contentDescription = item.badge.description
                                        }
                                    )
                                }
                            }
                        } else null
                    )
                }
            }
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
