package com.infinitepower.newquiz.core

import android.icu.util.LocaleData
import android.icu.util.ULocale
import android.os.Build
import androidx.core.text.util.LocalePreferences
import com.infinitepower.newquiz.model.NumberFormatType
import java.math.RoundingMode
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow

sealed class NumberFormatter(
    val formatType: NumberFormatType
) {
    abstract fun formatValueToString(
        value: Number,
        helperValueSuffix: String? = null,
        regionalPreferences: RegionalPreferences = RegionalPreferences(),
    ): String

    /**
     * The user configuration for the value formatter.
     *
     * @param locale The locale of the user.
     */
    data class RegionalPreferences(
        val locale: Locale = Locale.getDefault(),
        val temperatureUnit: Temperature.TemperatureUnit? = null,
        val distanceUnitType: Distance.DistanceUnitType? = null,
    )

    companion object {
        fun from(formatType: NumberFormatType): NumberFormatter {
            return when (formatType) {
                NumberFormatType.DEFAULT -> Default
                NumberFormatType.DATE -> Date
                NumberFormatType.TIME -> Time
                NumberFormatType.DATETIME -> DateTime
                NumberFormatType.PERCENTAGE -> Percentage
                NumberFormatType.TEMPERATURE -> Temperature
                NumberFormatType.DISTANCE -> Distance
            }
        }

        /**
         * Formats the [value] to a string with the [helperValueSuffix] if it's not null.
         */
        private fun valueWithSuffix(
            value: String,
            helperValueSuffix: String? = null
        ): String = if (helperValueSuffix != null) "$value $helperValueSuffix" else value

        private fun formatNumberToString(
            value: Number,
            helperValueSuffix: String?,
            locale: Locale
        ): String {
            val numberFormat = NumberFormat.getNumberInstance(locale)
            val numberFormatted = numberFormat.format(value)

            return valueWithSuffix(numberFormatted, helperValueSuffix)
        }
    }

    override fun toString(): String = formatType.name.lowercase()

    /**
     * The format type of the quiz is a number.
     */
    object Default : NumberFormatter(formatType = NumberFormatType.DEFAULT) {
        /**
         * Formats the double (number) [value] to a string with the [helperValueSuffix] if it's not null.
         */
        override fun formatValueToString(
            value: Number,
            helperValueSuffix: String?,
            regionalPreferences: RegionalPreferences
        ): String = formatNumberToString(
            value = value,
            helperValueSuffix = helperValueSuffix,
            locale = regionalPreferences.locale
        )
    }

    object Date : NumberFormatter(formatType = NumberFormatType.DATE) {
        /**
         * Formats the [value] to a string with the [helperValueSuffix] if it's not null.
         * The [value] is a timestamp in milliseconds.
         */
        override fun formatValueToString(
            value: Number,
            helperValueSuffix: String?,
            regionalPreferences: RegionalPreferences
        ): String {
            val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, regionalPreferences.locale)
            val dateFormatted = dateFormat.format(value.toLong())

            return valueWithSuffix(dateFormatted, helperValueSuffix)
        }
    }

    object Time : NumberFormatter(formatType = NumberFormatType.TIME) {
        /**
         * Formats the [value] to a string with the [helperValueSuffix] if it's not null.
         * The [value] is a timestamp in milliseconds.
         */
        override fun formatValueToString(
            value: Number,
            helperValueSuffix: String?,
            regionalPreferences: RegionalPreferences
        ): String {
            val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, regionalPreferences.locale)
            val timeFormatted = timeFormat.format(value.toLong())

            return valueWithSuffix(timeFormatted, helperValueSuffix)
        }
    }

    object DateTime : NumberFormatter(formatType = NumberFormatType.DATETIME) {
        /**
         * Formats the [value] to a string with the [helperValueSuffix] if it's not null.
         * The [value] is a timestamp in milliseconds.
         */
        override fun formatValueToString(
            value: Number,
            helperValueSuffix: String?,
            regionalPreferences: RegionalPreferences
        ): String {
            val dateTimeFormat =
                DateFormat.getDateTimeInstance(
                    DateFormat.MEDIUM,
                    DateFormat.SHORT,
                    regionalPreferences.locale
                )
            val dateTimeFormatted = dateTimeFormat.format(value.toLong())

            return valueWithSuffix(dateTimeFormatted, helperValueSuffix)
        }
    }

    object Percentage : NumberFormatter(
        formatType = NumberFormatType.PERCENTAGE
    ) {
        /**
         * Formats the [value] to a string with the [helperValueSuffix] if it's not null.
         * The [value] is a percentage in double.
         */
        override fun formatValueToString(
            value: Number,
            helperValueSuffix: String?,
            regionalPreferences: RegionalPreferences
        ): String {
            val numberFormat = NumberFormat.getPercentInstance(regionalPreferences.locale)
            val percentageFormatted = numberFormat.format(value.toDouble())

            return valueWithSuffix(percentageFormatted, helperValueSuffix)
        }
    }

    object Temperature : NumberFormatter(
        formatType = NumberFormatType.TEMPERATURE
    ) {
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

            fun convert(
                to: TemperatureUnit,
                value: Double
            ): Double {
                if (this == to) return value

                return when (this) {
                    CELSIUS -> when (to) {
                        FAHRENHEIT -> (value * 9 / 5) + 32
                        KELVIN -> value + 273.15
                        else -> throw IllegalArgumentException("Unknown temperature unit: $to")
                    }

                    FAHRENHEIT -> when (to) {
                        CELSIUS -> (value - 32) * 5 / 9
                        KELVIN -> (value + 459.67) * 5 / 9
                        else -> throw IllegalArgumentException("Unknown temperature unit: $to")
                    }

                    KELVIN -> when (to) {
                        CELSIUS -> value - 273.15
                        FAHRENHEIT -> (value * 9 / 5) - 459.67
                        else -> throw IllegalArgumentException("Unknown temperature unit: $to")
                    }
                }
            }

            override fun toString(): String = key
        }

        /**
         * Formats the [value] to a string, the [helperValueSuffix] is the [LocalePreferences.TemperatureUnit] from the [regionalPreferences].
         *
         * @param value The value to format.
         * @param helperValueSuffix The [LocalePreferences.TemperatureUnit] key.
         */
        override fun formatValueToString(
            value: Number,
            helperValueSuffix: String?,
            regionalPreferences: RegionalPreferences,
        ): String {
            checkNotNull(helperValueSuffix) { "The helperValueSuffix cannot be null for the temperature format type." }
            val valueTemperatureUnit = TemperatureUnit.fromKey(helperValueSuffix)

            // If the user has configured a temperature unit, use it instead of the locale
            val convertTemperatureUnitKey = if (regionalPreferences.temperatureUnit != null) {
                regionalPreferences.temperatureUnit.key
            } else {
                LocalePreferences.getTemperatureUnit(regionalPreferences.locale)
            }

            // If the value temperature unit is the same as the convert temperature unit, it's not necessary to convert
            // the value, just return the value with the suffix
            if (valueTemperatureUnit.key == convertTemperatureUnitKey) {
                return valueWithSuffix(value.toString(), valueTemperatureUnit.value)
            }

            val convertTemperatureUnit =
                regionalPreferences.temperatureUnit ?: TemperatureUnit.fromKey(convertTemperatureUnitKey)

            val convertedValue = valueTemperatureUnit.convert(
                to = convertTemperatureUnit,
                value = value.toDouble()
            )

            if (convertedValue.isNaN()) {
                return valueWithSuffix("NaN", convertTemperatureUnit.value)
            }

            val valueStr = convertedValue.toBigDecimal()
                .setScale(2, RoundingMode.HALF_EVEN)
                .stripTrailingZeros()
                .toPlainString()

            return valueWithSuffix(valueStr, convertTemperatureUnit.value)
        }
    }

    object Distance : NumberFormatter(formatType = NumberFormatType.DISTANCE) {
        enum class DistanceUnitType {
            METRIC,
            IMPERIAL,
        }

        enum class DistanceUnit(
            val key: String,
            val value: String,
            val type: DistanceUnitType,
        ) {
            METER(
                key = "meter",
                value = "m",
                type = DistanceUnitType.METRIC
            ),
            KILOMETER(
                key = "kilometer",
                value = "km",
                type = DistanceUnitType.METRIC
            ),
            SQUARE_KILOMETER(
                key = "square_kilometer",
                value = "km²",
                type = DistanceUnitType.METRIC
            ),
            FOOT(
                key = "foot",
                value = "ft",
                type = DistanceUnitType.IMPERIAL
            ),
            MILE(
                key = "mile",
                value = "mi",
                type = DistanceUnitType.IMPERIAL
            ),
            SQUARE_MILE(
                key = "square_mile",
                value = "mi²",
                type = DistanceUnitType.IMPERIAL
            );

            companion object {
                fun fromKey(key: String): DistanceUnit = entries
                    .firstOrNull { it.key == key || it.value == key }
                    ?: throw IllegalArgumentException("Unknown distance unit: $key")

                private const val FOOT_TO_METER_MULTIPLIER = 0.3048
                private const val MILE_TO_KILOMETER_MULTIPLIER = 1.609344
                private const val METER_TO_FOOT_MULTIPLIER = 3.2808399
                private const val KILOMETER_TO_MILE_MULTIPLIER = 0.621371192

                fun convert(
                    value: Double,
                    from: DistanceUnit,
                    to: DistanceUnitType
                ): Pair<Double, DistanceUnit> {
                    if (from.type == to) return Pair(value, from)

                    return when (to) {
                        // Convert to metric
                        DistanceUnitType.METRIC -> {
                            when (from) {
                                FOOT -> Pair(value * FOOT_TO_METER_MULTIPLIER, METER)
                                MILE -> Pair(value * MILE_TO_KILOMETER_MULTIPLIER, KILOMETER)
                                SQUARE_MILE -> Pair(value * MILE_TO_KILOMETER_MULTIPLIER.pow(2), SQUARE_KILOMETER)
                                METER, KILOMETER, SQUARE_KILOMETER -> Pair(value, from) // Already metric
                            }
                        }

                        // Convert to imperial
                        DistanceUnitType.IMPERIAL -> {
                            when (from) {
                                METER -> Pair(value * METER_TO_FOOT_MULTIPLIER, FOOT)
                                KILOMETER -> Pair(value * KILOMETER_TO_MILE_MULTIPLIER, MILE)
                                SQUARE_KILOMETER -> Pair(value * KILOMETER_TO_MILE_MULTIPLIER.pow(2), SQUARE_MILE)
                                FOOT, MILE, SQUARE_MILE -> Pair(value, from) // Already imperial
                            }
                        }
                    }
                }
            }

            override fun toString(): String = key
        }

        private fun Locale.getDistanceUnitType(): DistanceUnitType {
            return if (isMetric()) {
                DistanceUnitType.METRIC
            } else {
                DistanceUnitType.IMPERIAL
            }
        }

        private fun Locale.isMetric(): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val uLocale = ULocale.forLocale(this)
                val measurementSystem = LocaleData.getMeasurementSystem(uLocale)
                measurementSystem == LocaleData.MeasurementSystem.SI
            } else {
                isMetricLower()
            }
        }

        private fun Locale.isMetricLower(): Boolean {
            return when (country.uppercase(this)) {
                "US", "LR", "MM" -> false
                else -> true
            }
        }

        override fun formatValueToString(
            value: Number,
            helperValueSuffix: String?,
            regionalPreferences: RegionalPreferences
        ): String {
            checkNotNull(helperValueSuffix) { "The helperValueSuffix cannot be null for the distance format type." }
            val valueDistanceUnit = DistanceUnit.fromKey(helperValueSuffix)

            // If the user has configured a distance unit, use it instead of the locale
            val convertDistanceUnitType =
                regionalPreferences.distanceUnitType ?: regionalPreferences.locale.getDistanceUnitType()

            // If the value distance unit type is the same as the user distance unit type, return the value with the suffix
            if (valueDistanceUnit.type == convertDistanceUnitType) {
                return formatNumberToString(
                    value = value,
                    helperValueSuffix = valueDistanceUnit.value,
                    locale = regionalPreferences.locale
                )
            }

            val (convertedValue, convertedUnit) = DistanceUnit.convert(
                from = valueDistanceUnit,
                to = convertDistanceUnitType,
                value = value.toDouble()
            )

            if (convertedValue.isNaN()) {
                return valueWithSuffix("NaN", convertedUnit.value)
            }

            val valueRounded = convertedValue.toBigDecimal()
                .setScale(2, RoundingMode.HALF_EVEN)
                .stripTrailingZeros()
                .toDouble()

            return formatNumberToString(
                // Remove the decimals from the converted value
                value = valueRounded,
                helperValueSuffix = convertedUnit.value,
                locale = regionalPreferences.locale
            )
        }
    }
}
