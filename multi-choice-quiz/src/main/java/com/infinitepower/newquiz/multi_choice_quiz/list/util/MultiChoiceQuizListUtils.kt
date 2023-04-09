package com.infinitepower.newquiz.multi_choice_quiz.list.util

import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory

object MultiChoiceQuizListUtils {
    /**
     * This function returns all the categories of the multi-choice questions.
     * If there is no internet connection, it sorts the categories that don't require internet connection
     * in the top of the list.
     *
     * @param isInternetAvailable Whether the internet is available or not
     * @return All the categories of the multi-choice questions.
     */
    fun getAllCategories(
        isInternetAvailable: Boolean
    ): List<MultiChoiceCategory> {
        // If there is no internet, we make the categories that don't require internet connection
        // in the top of the list
        return if (isInternetAvailable) {
            multiChoiceQuestionCategories
        } else {
            multiChoiceQuestionCategories.sortedBy { it.requireInternetConnection }
        }
    }

    /**
     * This function returns the recent categories if there are any, otherwise it returns 3 random categories
     * from the list of all categories (if there is no internet connection, it only returns the categories that don't
     * require internet connection).
     *
     * @param recentCategories The list of recent categories
     * @param allCategories The list of all categories
     * @param isInternetAvailable Whether the internet is available or not
     * @return The list of recent categories if there are any, otherwise it returns 3 random categories
     * from the list of all categories (if there is no internet connection, it only returns the categories that don't
     * require internet connection).
     */
    fun getRecentCategories(
        recentCategories: List<MultiChoiceCategory>,
        allCategories: List<MultiChoiceCategory>,
        isInternetAvailable: Boolean
    ): List<MultiChoiceCategory> {
        // When there are recent categories, we return them
        return recentCategories.ifEmpty {
            // If there are no recent categories, we take 3 random ones,
            // So we don't show all categories initially
            allCategories
                // If there is no internet, we only show the categories that don't require internet connection
                .filter { !it.requireInternetConnection || isInternetAvailable }
                .shuffled()
                .take(3)
        }
    }
}
