package com.infinitepower.newquiz.settings_presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.infinitepower.newquiz.core.common.dataStore.settingsDataStore
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManagerImpl
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleRepository
import com.infinitepower.newquiz.settings_presentation.data.SettingsScreenPageData
import com.infinitepower.newquiz.translation_dynamic_feature.TranslatorUtil
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PreferencesScreen(
    page: SettingsScreenPageData,
    dailyWordleRepository: DailyWordleRepository,
    translationModelState: TranslatorUtil.TranslatorModelState,
    downloadTranslationModel: () -> Unit,
    deleteTranslationModel: () -> Unit,
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val context = LocalContext.current
    val dataStore = context.settingsDataStore
    val dataStoreManager = remember {
        DataStoreManagerImpl(dataStore)
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(id = page.stringRes))
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(id = CoreR.string.back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        PreferenceScreen(
            items = when(page) {
                is SettingsScreenPageData.MainPage -> emptyList()
                is SettingsScreenPageData.General -> page.items(scope, dataStoreManager)
                is SettingsScreenPageData.MultiChoiceQuiz -> page.items(
                    translationModelState = translationModelState,
                    downloadTranslationModel = downloadTranslationModel,
                    deleteTranslationModel = deleteTranslationModel
                )
                is SettingsScreenPageData.Wordle -> page.items(
                    scope,
                    dataStoreManager,
                    dailyWordleRepository
                )
            },
            dataStore = dataStore,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}