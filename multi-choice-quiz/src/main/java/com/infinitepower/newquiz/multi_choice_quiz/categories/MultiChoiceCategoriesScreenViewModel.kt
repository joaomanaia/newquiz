package com.infinitepower.newquiz.multi_choice_quiz.categories

import androidx.lifecycle.ViewModel
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MultiChoiceCategoriesScreenViewModel @Inject constructor(
    private val multiChoiceQuestionRepository: MultiChoiceQuestionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MultiChoiceCategoriesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(categories = multiChoiceQuestionCategories)
        }

        multiChoiceQuestionRepository
            .isFlagQuizInCategories()
            .also { showFlagQuiz ->
                if (showFlagQuiz) {
                    _uiState.update { currentState ->
                        val flagCategory = MultiChoiceQuestionCategory(
                            id = 1000,
                            name = UiText.StringResource(CoreR.string.flag_quiz),
                            image = CoreR.drawable.round_flag_circle_24
                        )
                        val newCategories = currentState.categories + flagCategory

                        currentState.copy(categories = newCategories)
                    }
                }
            }

        multiChoiceQuestionRepository
            .isLogoQuizInCategories()
            .also { showLogoQuiz ->
                if (showLogoQuiz) {
                    _uiState.update { currentState ->
                        val logoCategory = MultiChoiceQuestionCategory(
                            id = 1001,
                            name = UiText.StringResource(CoreR.string.logo_quiz),
                            image = CoreR.drawable.round_android_24
                        )
                        val newCategories = currentState.categories + logoCategory

                        currentState.copy(categories = newCategories)
                    }
                }
            }
    }
}