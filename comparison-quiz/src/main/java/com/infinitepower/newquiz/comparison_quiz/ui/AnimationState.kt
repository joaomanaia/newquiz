package com.infinitepower.newquiz.comparison_quiz.ui

/**
 * Represents the state of the animation in the comparison quiz game.
 */
internal enum class AnimationState {
    /**
     * This is the state before the game starts.
     * Animates the items into the screen.
     *
     * Next [AnimationState]: [Normal]
     */
    StartGame,

    /**
     * This is the state for when no animation is running.
     *
     * Next [AnimationState]: [NextQuestion]
     */
    Normal,

    /**
     * This animation state runs when the user selects the correct answer
     * and the question is transitioning to the next question.
     *
     * Next [AnimationState]: [Normal]
     */
    NextQuestion;

    /**
     * Returns the next [AnimationState] in the sequence.
     */
    fun next(): AnimationState {
        return when (this) {
            StartGame -> Normal
            Normal -> NextQuestion
            NextQuestion -> Normal
        }
    }
}