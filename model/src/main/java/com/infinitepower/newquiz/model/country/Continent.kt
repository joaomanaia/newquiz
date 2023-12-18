package com.infinitepower.newquiz.model.country

@JvmInline
value class Continent private constructor(
    val name: String
) {
    companion object {
        private val Africa = Continent("Africa")
        private val Asia = Continent("Asia")
        private val Europe = Continent("Europe")
        private val NorthAmerica = Continent("North America")
        private val SouthAmerica = Continent("South America")
        private val Oceania = Continent("Oceania")

        private val allContinents = listOf(
            Africa,
            Asia,
            Europe,
            NorthAmerica,
            SouthAmerica,
            Oceania
        )

        fun from(name: String): Continent = allContinents
            .firstOrNull { it.name == name }
            ?: throw IllegalArgumentException("Unknown continent with name: $name")
    }
}
