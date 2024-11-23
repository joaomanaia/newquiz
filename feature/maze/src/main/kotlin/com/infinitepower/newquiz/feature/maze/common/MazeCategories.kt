package com.infinitepower.newquiz.feature.maze.common

import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.data.local.wordle.WordleCategories
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.core.R as CoreR

object MazeCategories {
    private val availableMultiChoiceCategoriesIds = listOf(
        MultiChoiceBaseCategory.Logo.id,
        MultiChoiceBaseCategory.Flag.id,
        MultiChoiceBaseCategory.CountryCapitalFlags.id,
        MultiChoiceBaseCategory.GuessMathSolution.id,
    )

    fun getMultiChoiceCategories(): List<MultiChoiceCategory> {
        val filteredCategories = multiChoiceQuestionCategories.filter { category ->
            availableMultiChoiceCategoriesIds.contains(category.id)
        }

        // Because with the implementation with OpenTriviaDB, we can't select
        // specific category, so we need to create a special category
        // for this case, that contains all the categories.
        val othersCategory = MultiChoiceCategory(
            id = "others",
            name = UiText.DynamicString("Others"),
            image = CoreR.drawable.general_knowledge,
            requireInternetConnection = true
        )

        return filteredCategories + othersCategory
    }

    val availableWordleCategories = WordleCategories.allCategories.filter { category ->
        category.id != WordleQuizType.NUMBER_TRIVIA.name
    }
}
