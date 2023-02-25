package com.infinitepower.newquiz.model.math_quiz.maze

internal class MazeQuizTest {
    /*
    @Test
    fun testIsPlayableItem_noPlayedItems() {
        // Create a list of MazeItem objects
        val items = listOf(
            MazeQuiz.MazeItem(formula = MathFormula.fromStringFullFormula("1+1=2")),  // not played
            MazeQuiz.MazeItem(formula = MathFormula.fromStringFullFormula("1+1=2")), // not played
            MazeQuiz.MazeItem(formula = MathFormula.fromStringFullFormula("1+1=2")),  // not played
        )

        // Check if the first item is playable
        assertThat(items.isPlayableItem(0)).isTrue()

        // Check if the second item is playable
        assertThat(items.isPlayableItem(1)).isFalse()

        // Check if the third item is playable
        assertThat(items.isPlayableItem(2)).isFalse()
    }

    @Test
    fun testIsPlayableItem_firstPlayed() {
        // Create a list of MazeItem objects
        val items = listOf(
            MazeQuiz.MazeItem(
                formula = MathFormula.fromStringFullFormula("1+1=2"),
                played = true
            ),  // played
            MazeQuiz.MazeItem(formula = MathFormula.fromStringFullFormula("1+1=2")), // not played
            MazeQuiz.MazeItem(formula = MathFormula.fromStringFullFormula("1+1=2")),  // not played
        )

        // Check if the first item is playable
        assertThat(items.isPlayableItem(0)).isFalse()

        // Check if the second item is playable
        assertThat(items.isPlayableItem(1)).isTrue()

        // Check if the third item is playable
        assertThat(items.isPlayableItem(2)).isFalse()
    }

    @Test
    fun testIsItemPlayed_playedItem() {
        val mazeItems = listOf(
            MazeQuiz.MazeItem(
                formula = MathFormula.fromStringFullFormula("1+1=2"),
                played = true
            ),
            MazeQuiz.MazeItem(
                formula = MathFormula.fromStringFullFormula("1+1=2"),
                played = true
            ),
        )
        assertThat(mazeItems.isItemPlayed(0)).isTrue()
    }

    @Test
    fun testIsItemPlayed_unPlayedItem() {
        val mazeItems = listOf(
            MazeQuiz.MazeItem(
                formula = MathFormula.fromStringFullFormula("1+1=2"),
                played = true
            ),
            MazeQuiz.MazeItem(
                formula = MathFormula.fromStringFullFormula("1+1=2"),
                played = false
            ),
        )
        assertThat(mazeItems.isItemPlayed(1)).isFalse()
    }

    @Test
    fun testIsItemPlayed_outOfBounds() {
        val mazeItems = listOf(
            MazeQuiz.MazeItem(
                formula = MathFormula.fromStringFullFormula("1+1=2"),
                played = true
            ),
            MazeQuiz.MazeItem(
                formula = MathFormula.fromStringFullFormula("1+1=2"),
                played = false
            ),
        )
        assertThat(mazeItems.isItemPlayed(2)).isFalse()
    }

     */
}