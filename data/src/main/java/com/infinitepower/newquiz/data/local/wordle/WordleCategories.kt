package com.infinitepower.newquiz.data.local.wordle

import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.wordle.WordleCategory
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import kotlin.random.Random
import com.infinitepower.newquiz.core.R as CoreR

object WordleCategories  {
    /**
     * Returns a random [WordleCategory] from the list of [allCategories].
     * If [isInternetAvailable] is false, it will only return categories that don't require internet connection.
     *
     * @param isInternetAvailable Whether the internet is available or not.
     * @param random The random instance to use.
     * @see [WordleCategory]
     */
    fun random(
        isInternetAvailable: Boolean,
        random: Random = Random
    ): WordleCategory = if (isInternetAvailable) {
        allCategories.random(random)
    } else {
        allCategories.filter { !it.requireInternetConnection }.random(random)
    }

    val allCategories = listOf(
        WordleCategory(
            name = UiText.StringResource(CoreR.string.guess_the_word),
            image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fwordle_illustration.jpg?alt=media&token=69019438-4904-4656-8b1c-18678c537d6b",
            wordleQuizType = WordleQuizType.TEXT
        ),
        WordleCategory(
            name = UiText.StringResource(CoreR.string.guess_the_number),
            image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fnumbers_12345_illustration.jpg?alt=media&token=f170e7ca-02a3-4dae-87f0-63b0f1205bc5",
            wordleQuizType = WordleQuizType.NUMBER
        ),
        WordleCategory(
            name = UiText.StringResource(CoreR.string.guess_math_formula),
            image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fnumber_illustration.jpg?alt=media&token=68faf243-2b0e-4a13-aa9c-223743e263fd",
            wordleQuizType = WordleQuizType.MATH_FORMULA
        ),
        WordleCategory(
            name = UiText.StringResource(CoreR.string.number_trivia),
            image = "https://firebasestorage.googleapis.com/v0/b/newquiz-app.appspot.com/o/Illustrations%2Fnumber_12_in_beach.jpg?alt=media&token=9b888c81-c51c-49ac-a376-0b3bde45db36",
            wordleQuizType = WordleQuizType.NUMBER_TRIVIA,
            requireInternetConnection = true
        )
    )
}
