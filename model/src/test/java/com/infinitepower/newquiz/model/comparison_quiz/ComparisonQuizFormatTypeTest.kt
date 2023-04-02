package com.infinitepower.newquiz.model.comparison_quiz

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Locale

internal class ComparisonQuizFormatTypeTest {
    private val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
    private val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
    private val dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault())

    @Test
    fun `test fromKey with valid keys`() {
        assertThat(ComparisonQuizFormatType.fromKey("number")).isEqualTo(ComparisonQuizFormatType.Number)
        assertThat(ComparisonQuizFormatType.fromKey("date")).isEqualTo(ComparisonQuizFormatType.Date)
        assertThat(ComparisonQuizFormatType.fromKey("time")).isEqualTo(ComparisonQuizFormatType.Time)
        assertThat(ComparisonQuizFormatType.fromKey("datetime")).isEqualTo(ComparisonQuizFormatType.DateTime)
    }

    @Test
    fun `test fromKey with invalid key`() {
        val exception = assertThrows<IllegalArgumentException> { ComparisonQuizFormatType.fromKey("invalid") }
        assertThat(exception.message).isEqualTo("Unknown key: invalid")
    }

    @Test
    fun `text valueWithSuffix format without suffix`() {
        val value = "value"
        assertThat(ComparisonQuizFormatType.valueWithSuffix(value, null)).isEqualTo(value)
    }

    @Test
    fun `text valueWithSuffix format with suffix`() {
        val value = "value"
        val suffix = "suffix"
        val expected = "$value $suffix"
        assertThat(ComparisonQuizFormatType.valueWithSuffix(value, suffix)).isEqualTo(expected)
    }

    // Number type tests

    @Test
    fun `test Number formatValueToString without helperValueSuffix`() {
        val number = 12345.6789
        val expected = numberFormat.format(number)
        assertThat(ComparisonQuizFormatType.Number.formatValueToString(number)).isEqualTo(expected)
    }

    @Test
    fun `test Number formatValueToString with helperValueSuffix`() {
        val number = 12345.6789
        val suffix = "m"
        val expected = "${numberFormat.format(number)} $suffix"
        assertThat(ComparisonQuizFormatType.Number.formatValueToString(number, suffix)).isEqualTo(expected)
    }

    // Date type tests

    @Test
    fun `test Date formatValueToString without helperValueSuffix`() {
        val timestamp = 1627196400000.0 // July 25, 2021
        val expected = dateFormat.format(timestamp.toLong())
        assertThat(ComparisonQuizFormatType.Date.formatValueToString(timestamp)).isEqualTo(expected)
    }

    @Test
    fun `test Date formatValueToString with helperValueSuffix`() {
        val timestamp = 1627196400000.0 // July 25, 2021
        val suffix = "UTC"
        val expected = "${dateFormat.format(timestamp.toLong())} $suffix"
        assertThat(ComparisonQuizFormatType.Date.formatValueToString(timestamp, suffix)).isEqualTo(expected)
    }

    // Time type tests

    @Test
    fun `test Time formatValueToString without helperValueSuffix`() {
        val timestamp = 1627196400000.0 // July 25, 2021 12:00:00 AM GMT
        val expected = timeFormat.format(timestamp.toLong())
        assertThat(ComparisonQuizFormatType.Time.formatValueToString(timestamp)).isEqualTo(expected)
    }

    @Test
    fun `test Time formatValueToString with helperValueSuffix`() {
        val timestamp = 1627196400000.0 // July 25, 2021 12:00:00 AM GMT
        val suffix = "UTC"
        val expected = "${timeFormat.format(timestamp.toLong())} $suffix"
        assertThat(ComparisonQuizFormatType.Time.formatValueToString(timestamp, suffix)).isEqualTo(expected)
    }

    // DateTime type tests

    @Test
    fun `test DateTime formatValueToString without helperValueSuffix`() {
        val timestamp = 1627196400000.0 // July 25, 2021 12:00:00 AM GMT
        val expected = dateTimeFormat.format(timestamp.toLong())
        assertThat(ComparisonQuizFormatType.DateTime.formatValueToString(timestamp)).isEqualTo(expected)
    }

    @Test
    fun `test DateTime formatValueToString with helperValueSuffix`() {
        val timestamp = 1627196400000.0 // July 25, 2021 12:00:00 AM GMT
        val suffix = "UTC"
        val expected = "${dateTimeFormat.format(timestamp.toLong())} $suffix"
        assertThat(ComparisonQuizFormatType.DateTime.formatValueToString(timestamp, suffix)).isEqualTo(expected)
    }

    // Percentage type tests

    @Test
    fun `test percentage formatValueToString should format percentage value to string`() {
        val value = 0.75
        val expected = "75%"

        val result = ComparisonQuizFormatType.Percentage.formatValueToString(value)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test percentage formatValueToString should include helperValueSuffix if not null`() {
        val value = 0.75
        val suffix = "of the time"
        val expected = "75% of the time"

        val result = ComparisonQuizFormatType.Percentage.formatValueToString(value, suffix)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test percentage formatValueToString should format percentage higher than 100 percent`() {
        val result = ComparisonQuizFormatType.Percentage.formatValueToString(1.3)
        assertThat(result).isEqualTo("130%")
    }

    // Currency type tests

    @Test
    fun `test currency formatValueToString should format currency value to string`() {
        val value = 12.34
        val expected = "$12.34"

        val result = ComparisonQuizFormatType.Currency.formatValueToString(value)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test currency formatValueToString should include helperValueSuffix if not null`() {
        val value = 12.34
        val helperValueSuffix = "USD"
        val expected = "$12.34 USD"

        val result = ComparisonQuizFormatType.Currency.formatValueToString(value, helperValueSuffix)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test currency formatValueToString should format currency value to string with custom currency`() {
        val value = 12.34
        val expected = "12,34 €"

        val result = ComparisonQuizFormatType.Currency.formatValueToString(value, locale = Locale.GERMANY)
        // Replace the non-breaking space with a normal space.
        val formattedResult = result.replace("\u00A0", " ")

        assertThat(formattedResult).isEqualTo(expected)
    }
}