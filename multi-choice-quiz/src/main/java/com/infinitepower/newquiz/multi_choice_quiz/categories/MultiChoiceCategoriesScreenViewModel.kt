package com.infinitepower.newquiz.multi_choice_quiz.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultiChoiceCategoriesScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(MultiChoiceCategoriesUiState())
    val uiState = _uiState.asStateFlow()

    private val remoteConfig by lazy { Firebase.remoteConfig }

    init {
        _uiState.update { currentState ->
            currentState.copy(categories = multiChoiceQuestionCategories)
        }

        viewModelScope.launch(Dispatchers.IO) {
            remoteConfig
                .getBoolean("show_flag_quiz_in_categories")
                .also { showFlagQuiz ->
                    if (showFlagQuiz) {
                        _uiState.update { currentState ->
                            val flagCategory = MultiChoiceQuestionCategory(
                                id = 1000,
                                name = CoreR.string.flag_quiz,
                                image = CoreR.drawable.round_flag_circle_24
                            )
                            val newCategories = currentState.categories + flagCategory

                            currentState.copy(categories = newCategories)
                        }
                    }
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            remoteConfig
                .getBoolean("show_logo_quiz_in_categories")
                .also { showLogoQuiz ->
                    if (showLogoQuiz) {
                        _uiState.update { currentState ->
                            val logoCategory = MultiChoiceQuestionCategory(
                                id = 1001,
                                name = CoreR.string.logo_quiz,
                                image = CoreR.drawable.round_android_24
                            )
                            val newCategories = currentState.categories + logoCategory

                            currentState.copy(categories = newCategories)
                        }
                    }
                }
        }
    }
}