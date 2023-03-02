package com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz

import android.util.Log
import kotlinx.serialization.Serializable

@Serializable
sealed class Continent(
    val key: String
) : java.io.Serializable {
    companion object {
        fun fromName(name: String) = when (name) {
            Africa.key -> Africa
            Asia.key -> Asia
            Europe.key -> Europe
            NorthAmerica.key -> NorthAmerica
            SouthAmerica.key -> SouthAmerica
            Oceania.key -> Oceania
            else -> {
                Log.e("Continent", name)
                throw IllegalArgumentException()
            }
        }
    }

    object Africa : Continent(key = "Africa")

    object Asia : Continent(key = "Asia")

    object Europe : Continent(key = "Europe")

    object NorthAmerica : Continent(key = "North America")

    object SouthAmerica : Continent(key = "South America")

    object Oceania : Continent(key = "Oceania")
}
