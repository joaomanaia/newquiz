package com.infinitepower.newquiz.model.multi_choice_quiz

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed class MultiChoiceQuestionStep {
    abstract val question: MultiChoiceQuestion

    @Keep
    @Serializable
    data class NotCurrent(
        override val question: MultiChoiceQuestion
    ) : MultiChoiceQuestionStep() {
        fun changeToCurrent() = Current(question)
    }

    @Keep
    @Serializable
    data class Current(
        override val question: MultiChoiceQuestion
    ) : MultiChoiceQuestionStep() {
        fun changeToCompleted(
            correct: Boolean,
            selectedAnswer: SelectedAnswer,
            questionTime: Long
        ) = Completed(question, correct, selectedAnswer, questionTime)
    }

    @Keep
    @Serializable
    data class Completed(
        override val question: MultiChoiceQuestion,
        val correct: Boolean,
        val selectedAnswer: SelectedAnswer = SelectedAnswer.NONE,
        val questionTime: Long = 0
    ) : MultiChoiceQuestionStep()

    fun asCurrent() = Current(question)
}

fun List<MultiChoiceQuestionStep>.isAllCompleted(): Boolean = all { it is MultiChoiceQuestionStep.Completed }

fun List<MultiChoiceQuestionStep.Completed>.countCorrectQuestions(): Int = count { it.correct }