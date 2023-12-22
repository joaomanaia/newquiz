package com.infinitepower.newquiz.feature.settings.screens.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralScreenViewModel @Inject constructor(
    private val recentCategoriesRepository: RecentCategoriesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GeneralScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    defaultShowCategoryConnectionInfo = recentCategoriesRepository.getDefaultShowCategoryConnectionInfo(),
                )
            }
        }
    }

    fun onEvent(event: GeneralScreenUiEvent){
        when(event) {
            is GeneralScreenUiEvent.ClearHomeRecentCategories -> {
                viewModelScope.launch {
                    recentCategoriesRepository.cleanAllSavedCategories()
                }
            }
        }
    }
}