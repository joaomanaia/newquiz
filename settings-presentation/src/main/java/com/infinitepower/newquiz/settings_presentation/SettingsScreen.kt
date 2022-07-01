package com.infinitepower.newquiz.settings_presentation

import androidx.annotation.Keep
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinitepower.newquiz.core.navigation.MainNavGraph
import com.infinitepower.newquiz.core.theme.CustomColor
import com.infinitepower.newquiz.core.theme.extendedColors
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.settings_presentation.components.PreferencesScreen
import com.infinitepower.newquiz.settings_presentation.data.SettingsScreenPageData
import com.infinitepower.newquiz.settings_presentation.destinations.SettingsScreenDestination
import com.infinitepower.newquiz.settings_presentation.model.ScreenKey
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction

data class SettingsScreenNavArgs(
    val screenKey: String = SettingsScreenPageData.MainPage.key.value
)

@Composable
@MainNavGraph
@Destination(navArgsDelegate = SettingsScreenNavArgs::class)
fun SettingsScreen(
    navigator: DestinationsNavigator,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by settingsViewModel.uiState.collectAsState()

    SettingsScreenImpl(
        uiState = uiState,
        onBackClick = navigator::popBackStack,
        onNavigateClickClick = navigator::navigate
    )
}

@Composable
private fun SettingsScreenImpl(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onNavigateClickClick: (direction: Direction) -> Unit
) {
    when (uiState.screenKey) {
        SettingsScreenPageData.MainPage.key -> MainSettingsScreen(
            onNavigateClickClick = onNavigateClickClick,
            onBackClick = onBackClick
        )
        else -> PreferencesScreen(
            page = SettingsScreenPageData.getPage(uiState.screenKey),
            onBackClick = onBackClick
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainSettingsScreen(
    onNavigateClickClick: (direction: Direction) -> Unit,
    onBackClick: () -> Unit
) {
    val (searchQuery, setSearchQuery) = remember {
        mutableStateOf("")
    }

    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec = decayAnimationSpec,
        state = rememberTopAppBarScrollState()
    )

    val settingsItems = listOf(
        SettingsBaseItemData(
            key = SettingsScreenPageData.General.key,
            icon = Icons.Rounded.Settings,
            name = stringResource(id = SettingsScreenPageData.General.stringRes),
            colorRoles = MaterialTheme.extendedColors.getColorRolesByKey(key = CustomColor.Keys.Blue)
        ),
        SettingsBaseItemData(
            key = SettingsScreenPageData.Quiz.key,
            icon = Icons.Rounded.Quiz,
            name = stringResource(id = SettingsScreenPageData.Quiz.stringRes),
            colorRoles = MaterialTheme.extendedColors.getColorRolesByKey(key = CustomColor.Keys.Blue)
        ),
    )

    val spaceMedium = MaterialTheme.spacing.medium

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.settings))
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = { BackIconButton(onClick = onBackClick) }
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = MaterialTheme.spacing.small)
        ) {
            item {
                SettingsSearchComponent(
                    value = searchQuery,
                    onValueChange = setSearchQuery,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = spaceMedium)
                )
                Spacer(modifier = Modifier.height(spaceMedium))
            }
            items(settingsItems) { item ->
                SettingsBaseItem(
                    modifier = Modifier.fillParentMaxWidth(),
                    data = item,
                    onClick = {
                        onNavigateClickClick(SettingsScreenDestination(item.key.value))
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsBaseItem(
    modifier: Modifier = Modifier,
    data: SettingsBaseItemData,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(
            onClick = onClick,
            role = Role.Button,
            indication = rememberRipple(),
            interactionSource = remember { MutableInteractionSource() },
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Surface(
                color = data.colorRoles.accent,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = data.icon,
                    contentDescription = data.name,
                    tint = data.colorRoles.onAccent,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Text(
                text = data.name,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Keep
private data class SettingsBaseItemData(
    val key: ScreenKey,
    val icon: ImageVector,
    val name: String,
    val colorRoles: CustomColor.ColorRoles
)

@Composable
private fun SettingsSearchComponent(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (value: String) -> Unit
) {
    Surface(
        tonalElevation = 8.dp,
        shape = CircleShape,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search"
            )
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                textStyle = TextStyle(
                    color =  LocalContentColor.current,
                ),
                maxLines = 1
            ) {
                if (value.isBlank()) Text(text = "Search Settings")
            }
        }
    }
}