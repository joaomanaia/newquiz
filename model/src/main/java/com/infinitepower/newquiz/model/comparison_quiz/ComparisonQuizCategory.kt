package com.infinitepower.newquiz.model.comparison_quiz

import androidx.annotation.Keep
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Locale

/**
 * A category of comparison quizzes.
 * @param id The id of the category.
 * @param title The title of the category.
 * @param imageUrl The url of the image of the category.
 * @param questionDescription The description of the question.
 * @param helperValueSuffix The suffix of the question value.
 */
@Keep
@Serializable
data class ComparisonQuizCategory(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val questionDescription: QuestionDescription,
    val formatType: ComparisonQuizFormatType,
    val helperValueSuffix: String? = null,
    val dataSourceAttribution: DataSourceAttribution? = null
) : java.io.Serializable {
    fun formatValueToString(value: Double): String {
        return formatType.formatValueToString(value, helperValueSuffix)
    }

    fun getQuestionDescription(
        comparisonMode: ComparisonModeByFirst
    ): String = when (comparisonMode) {
        ComparisonModeByFirst.GREATER -> questionDescription.greater
        ComparisonModeByFirst.LESSER -> questionDescription.less
    }

    @Keep
    @Serializable
    data class QuestionDescription(
        val greater: String,
        val less: String
    ) : java.io.Serializable

    /**
     * The data source attribution of the category.
     */
    @Keep
    @Serializable
    data class DataSourceAttribution(
        val text: String,
        val logo: String? = null
    ) : java.io.Serializable
}

@Serializable(with = ComparisonQuizFormatTypeSerializer::class)
sealed class ComparisonQuizFormatType(
    val key: String
) : java.io.Serializable {
    abstract fun formatValueToString(
        value: Double,
        helperValueSuffix: String? = null,
        locale: Locale = Locale.getDefault()
    ): String

    companion object {
        fun fromKey(key: String): ComparisonQuizFormatType = when (key) {
            Number.key -> Number
            Date.key -> Date
            Time.key -> Time
            DateTime.key -> DateTime
            Percentage.key -> Percentage
            Currency.key -> Currency
            else -> throw IllegalArgumentException("Unknown key: $key")
        }

        /**
         * Formats the [value] to a string with the [helperValueSuffix] if it's not null.
         */
        internal fun valueWithSuffix(
            value: String,
            helperValueSuffix: String? = null
        ): String = if (helperValueSuffix != null) "$value $helperValueSuffix" else value
    }

    override fun toString(): String = key

    /**
     * The format type of the quiz is a number.
     */
    object Number : ComparisonQuizFormatType("number") {
        /**
         * Formats the double (number) [value] to a string with the [helperValueSuffix] if it's not null.
         */
        override fun formatValueToString(
            value: Double,
            helperValueSuffix: String?,
            locale: Locale
        ): String {
            val numberFormat = NumberFormat.getNumberInstance(locale)
            val numberFormatted = numberFormat.format(value)

            return valueWithSuffix(numberFormatted, helperValueSuffix)
        }
    }

    object Date : ComparisonQuizFormatType("date") {
        /**
         * Formats the [value] to a string with the [helperValueSuffix] if it's not null.
         * The [value] is a timestamp in milliseconds.
         */
        override fun formatValueToString(
            value: Double,
            helperValueSuffix: String?,
            locale: Locale
        ): String {
            val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale)
            val dateFormatted = dateFormat.format(value.toLong())

            return valueWithSuffix(dateFormatted, helperValueSuffix)
        }
    }

    object Time : ComparisonQuizFormatType("time") {
        /**
         * Formats the [value] to a string with the [helperValueSuffix] if it's not null.
         * The [value] is a timestamp in milliseconds.
         */
        override fun formatValueToString(
            value: Double,
            helperValueSuffix: String?,
            locale: Locale
        ): String {
            val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale)
            val timeFormatted = timeFormat.format(value.toLong())

            return valueWithSuffix(timeFormatted, helperValueSuffix)
        }
    }

    object DateTime : ComparisonQuizFormatType("datetime") {
        /**
         * Formats the [value] to a string with the [helperValueSuffix] if it's not null.
         * The [value] is a timestamp in milliseconds.
         */
        override fun formatValueToString(
            value: Double,
            helperValueSuffix: String?,
            locale: Locale
        ): String {
            val dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale)
            val dateTimeFormatted = dateTimeFormat.format(value.toLong())

            return valueWithSuffix(dateTimeFormatted, helperValueSuffix)
        }
    }

    object Percentage : ComparisonQuizFormatType("percentage") {
        /**
         * Formats the [value] to a string with the [helperValueSuffix] if it's not null.
         * The [value] is a percentage in double.
         */
        override fun formatValueToString(
            value: Double,
            helperValueSuffix: String?,
            locale: Locale
        ): String {
            val numberFormat = NumberFormat.getPercentInstance(locale)
            val percentageFormatted = numberFormat.format(value)

            return valueWithSuffix(percentageFormatted, helperValueSuffix)
        }
    }

    object Currency : ComparisonQuizFormatType("currency") {
        /**
         * Formats the [value] to a string with the [helperValueSuffix] if it's not null.
         * The [value] is a currency in double.
         */
        override fun formatValueToString(
            value: Double,
            helperValueSuffix: String?,
            locale: Locale
        ): String {
            val currencyFormat = NumberFormat.getCurrencyInstance(locale)
            val currencyFormatted = currencyFormat.format(value)

            return valueWithSuffix(currencyFormatted, helperValueSuffix)
        }
    }
}

/**
 * A serializer for the [ComparisonQuizFormatType] class.
 */
object ComparisonQuizFormatTypeSerializer : KSerializer<ComparisonQuizFormatType> {
    override fun serialize(encoder: Encoder, value: ComparisonQuizFormatType) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): ComparisonQuizFormatType {
        return ComparisonQuizFormatType.fromKey(decoder.decodeString())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("ComparisonQuizFormat", PrimitiveKind.STRING)
}
