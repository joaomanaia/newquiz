package com.infinitepower.newquiz.multi_choice_quiz

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.RemainingTime
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.SelectedAnswer
import com.infinitepower.newquiz.model.multi_choice_quiz.isAllCompleted

@Keep
data class MultiChoiceQuizScreenUiState(
    val questionSteps: List<MultiChoiceQuestionStep> = emptyList(),
    val currentQuestionIndex: Int = -1,
    val selectedAnswer: SelectedAnswer = SelectedAnswer.NONE,
    val remainingTime: RemainingTime = RemainingTime.ZERO,
    val userDiamonds: Int = -1,
    val userSignedIn: Boolean = false
) {
    val currentQuestionStep: MultiChoiceQuestionStep.Current? = questionSteps.getOrNull(currentQuestionIndex)?.asCurrent()

    fun getQuestionPositionFormatted(): String =
        "Question ${currentQuestionIndex + 1}/${questionSteps.size}"

    /**
     * Gets new question index.
     * If question is the last question retuns -1.
     * @return new question index
     */
    fun getNextIndex(): Int = if (currentQuestionIndex == questionSteps.lastIndex) -1 else currentQuestionIndex + 1

    val isGameEnded: Boolean
        get() = questionSteps.isAllCompleted()
}