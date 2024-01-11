package com.infinitepower.newquiz.core

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.model.NumberFormatType
import com.infinitepower.newquiz.core.NumberFormatter.Temperature.TemperatureUnit
import com.infinitepower.newquiz.core.NumberFormatter.Distance.DistanceUnit
import com.infinitepower.newquiz.core.NumberFormatter.Distance.DistanceUnitType
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Locale

/**
 * Tests for [NumberFormatter].
 */
internal class NumberFormatterTest {
    private val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
    private val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
    private val dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault())

    // Number type tests

    @Test
    fun `test Number formatValueToString without helperValueSuffix`() {
        val number = 12345.6789
        val expected = numberFormat.format(number)

        val formatter = NumberFormatter.from(NumberFormatType.DEFAULT)
        assertThat(formatter.formatValueToString(number)).isEqualTo(expected)
    }

    @Test
    fun `test Number formatValueToString with helperValueSuffix`() {
        val number = 12345.6789
        val suffix = "m"
        val expected = "${numberFormat.format(number)} $suffix"

        val formatter = NumberFormatter.from(NumberFormatType.DEFAULT)
        assertThat(formatter.formatValueToString(number, suffix)).isEqualTo(expected)
    }

    // Date type tests

    @Test
    fun `test Date formatValueToString without helperValueSuffix`() {
        val timestamp = 1627196400000.0 // July 25, 2021
        val expected = dateFormat.format(timestamp.toLong())

        val formatter = NumberFormatter.from(NumberFormatType.DATE)
        assertThat(formatter.formatValueToString(timestamp)).isEqualTo(expected)
    }

    @Test
    fun `test Date formatValueToString with helperValueSuffix`() {
        val timestamp = 1627196400000.0 // July 25, 2021
        val suffix = "UTC"
        val expected = "${dateFormat.format(timestamp.toLong())} $suffix"

        val formatter = NumberFormatter.from(NumberFormatType.DATE)
        assertThat(formatter.formatValueToString(timestamp, suffix)).isEqualTo(expected)
    }

    // Time type tests

    @Test
    fun `test Time formatValueToString without helperValueSuffix`() {
        val timestamp = 1627196400000.0 // July 25, 2021 12:00:00 AM GMT
        val expected = timeFormat.format(timestamp.toLong())

        val formatter = NumberFormatter.from(NumberFormatType.TIME)
        assertThat(formatter.formatValueToString(timestamp)).isEqualTo(expected)
    }

    @Test
    fun `test Time formatValueToString with helperValueSuffix`() {
        val timestamp = 1627196400000.0 // July 25, 2021 12:00:00 AM GMT
        val suffix = "UTC"
        val expected = "${timeFormat.format(timestamp.toLong())} $suffix"

        val formatter = NumberFormatter.from(NumberFormatType.TIME)
        assertThat(formatter.formatValueToString(timestamp, suffix)).isEqualTo(expected)
    }

    // DateTime type tests

    @Test
    fun `test DateTime formatValueToString without helperValueSuffix`() {
        val timestamp = 1627196400000.0 // July 25, 2021 12:00:00 AM GMT
        val expected = dateTimeFormat.format(timestamp.toLong())

        val formatter = NumberFormatter.from(NumberFormatType.DATETIME)
        assertThat(formatter.formatValueToString(timestamp)).isEqualTo(expected)
    }

    @Test
    fun `test DateTime formatValueToString with helperValueSuffix`() {
        val timestamp = 1627196400000.0 // July 25, 2021 12:00:00 AM GMT
        val suffix = "UTC"
        val expected = "${dateTimeFormat.format(timestamp.toLong())} $suffix"

        val formatter = NumberFormatter.from(NumberFormatType.DATETIME)
        assertThat(formatter.formatValueToString(timestamp, suffix)).isEqualTo(expected)
    }

    // Percentage type tests

    @Test
    fun `test percentage formatValueToString should format percentage value to string`() {
        val value = 0.75
        val expected = "75%"

        val formatter = NumberFormatter.from(NumberFormatType.PERCENTAGE)
        assertThat(formatter.formatValueToString(value)).isEqualTo(expected)
    }

    @Test
    fun `test percentage formatValueToString should include helperValueSuffix if not null`() {
        val value = 0.75
        val suffix = "of the time"
        val expected = "75% of the time"

        val formatter = NumberFormatter.from(NumberFormatType.PERCENTAGE)
        assertThat(formatter.formatValueToString(value, suffix)).isEqualTo(expected)
    }

    @Test
    fun `test percentage formatValueToString should format percentage higher than 100 percent`() {
        val formatter = NumberFormatter.from(NumberFormatType.PERCENTAGE)
        assertThat(formatter.formatValueToString(1.3)).isEqualTo("130%")
    }

    // Temperature tests

    @CsvSource(
        "0.0, celsius, de, 0.0 °C", // German: Celsius to Celsius
        "0.0, celsius, us, 32 °F", // US: Celsius to Fahrenheit
        "0.0, fahrenhe, de, -17.78 °C", // German: Fahrenheit to Celsius
        "0.0, fahrenhe, us, 0.0 °F", // US: Fahrenheit to Fahrenheit
    )
    @ParameterizedTest(name = "{0} in {1} should be formatted to {3} in {2} locale")
    fun `test temperature formatValueToString`(
        valueToFormat: Double,
        temperatureUnitStr: String,
        convertCountry: String,
        expected: String
    ) {
        val formatter = NumberFormatter.from(NumberFormatType.TEMPERATURE)

        val locale = Locale("en", convertCountry)

        val formattedValue = formatter.formatValueToString(
            value = valueToFormat,
            helperValueSuffix = temperatureUnitStr,
            regionalPreferences = NumberFormatter.RegionalPreferences(
                locale = locale,
            )
        )
        assertThat(formattedValue).isEqualTo(expected)
    }

    @CsvSource(
        "0.0, CELSIUS, CELSIUS, 0.0 °C", // German: Celsius to Celsius
        "0.0, CELSIUS, FAHRENHEIT, 32 °F", // US: Celsius to Fahrenheit
        "0.0, FAHRENHEIT, CELSIUS, -17.78 °C", // German: Fahrenheit to Celsius
        "0.0, FAHRENHEIT, FAHRENHEIT, 0.0 °F", // US: Fahrenheit to Fahrenheit
    )
    @ParameterizedTest(name = "{0} in {1} should be formatted to {3} in {2} locale")
    fun `test temperature formatValueToString when regionalPreferences temperatureUnit is configured`(
        valueToFormat: Double,
        valueTemperatureUnit: TemperatureUnit,
        convertTemperatureUnit: TemperatureUnit,
        expected: String
    ) {
        val formatter = NumberFormatter.from(NumberFormatType.TEMPERATURE)

        val formattedValue = formatter.formatValueToString(
            value = valueToFormat,
            helperValueSuffix = valueTemperatureUnit.key,
            regionalPreferences = NumberFormatter.RegionalPreferences(
                temperatureUnit = convertTemperatureUnit, // This should override the default locale
            )
        )
        assertThat(formattedValue).isEqualTo(expected)
    }

    // Distance tests

    @CsvSource(
        "1.0, kilometer, de, 1 km", // German: Meter to Meter
        "1.0, kilometer, us, 0.62 mi", // US: Meter to Mile
    )
    @ParameterizedTest(name = "{0} in {1} should be formatted to {3} in {2} locale")
    fun `test distance formatValueToString when regionalPreferences distanceUnit is not configured`(
        valueToFormat: Double,
        distanceUnit: String,
        convertCountry: String,
        expected: String
    ) {
        val formatter = NumberFormatter.from(NumberFormatType.DISTANCE)

        val locale = Locale("en", convertCountry)

        val formattedValue = formatter.formatValueToString(
            value = valueToFormat,
            helperValueSuffix = distanceUnit,
            regionalPreferences = NumberFormatter.RegionalPreferences(
                locale = locale,
            )
        )
        assertThat(formattedValue).isEqualTo(expected)
    }

    @CsvSource(
        "1.0, KILOMETER, METRIC, 1 km", // Kilometer to Kilometer (Metric) system
        "1.0, KILOMETER, IMPERIAL, 0.62 mi", // Kilometer to Mile (Imperial) system
        "1.0, METER, METRIC, 1 m", // Meter to Meter (Metric) system
        "1.0, METER, IMPERIAL, 3.28 ft", // Meter to Foot (Imperial) system
        "1.0, MILE, METRIC, 1.61 km", // Mile to Kilometer (Metric) system
        "1.0, MILE, IMPERIAL, 1 mi", // Mile to Mile (Imperial) system
        "1.0, FOOT, METRIC, 0.3 m", // Foot to Meter (Metric) system
        "1.0, FOOT, IMPERIAL, 1 ft", // Foot to Foot (Imperial) system
    )
    @ParameterizedTest(name = "{0} in {1} should be formatted to {3} in {2} locale")
    fun `test distance formatValueToString when regionalPreferences distanceUnit is configured`(
        valueToFormat: Double,
        valueDistanceUnit: DistanceUnit,
        convertDistanceUnitType: DistanceUnitType,
        expected: String
    ) {
        val formatter = NumberFormatter.from(NumberFormatType.DISTANCE)

        val formattedValue = formatter.formatValueToString(
            value = valueToFormat,
            helperValueSuffix = valueDistanceUnit.key,
            regionalPreferences = NumberFormatter.RegionalPreferences(
                distanceUnitType = convertDistanceUnitType, // This should override the default locale
            )
        )
        assertThat(formattedValue).isEqualTo(expected)
    }
}
