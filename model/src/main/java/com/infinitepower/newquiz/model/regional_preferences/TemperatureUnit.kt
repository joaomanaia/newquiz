package com.infinitepower.newquiz.model.regional_preferences

enum class TemperatureUnit(
    val key: String,
    val value: String,
) {
    CELSIUS(key = "celsius", value = "°C"),
    FAHRENHEIT(key = "fahrenhe", value = "°F"),
    KELVIN(key = "kelvin", value = "K");

    companion object {
        fun fromKey(key: String): TemperatureUnit = entries
            .firstOrNull { it.key == key }
            ?: throw IllegalArgumentException("Unknown temperature unit: $key")

    }

    @Suppress("MagicNumber")
    fun convert(
        to: TemperatureUnit,
        value: Double
    ): Double {
        if (this == to) return value

        return when (this) {
            CELSIUS -> when (to) {
                FAHRENHEIT -> (value * 9 / 5) + 32
                KELVIN -> value + 273.15
                else -> value
            }

            FAHRENHEIT -> when (to) {
                CELSIUS -> (value - 32) * 5 / 9
                KELVIN -> (value + 459.67) * 5 / 9
                else -> value
            }

            KELVIN -> when (to) {
                CELSIUS -> value - 273.15
                FAHRENHEIT -> (value * 9 / 5) - 459.67
                else -> value
            }
        }
    }

    override fun toString(): String = key
}
