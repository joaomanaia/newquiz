package com.infinitepower.newquiz.model

/**
 * This enum class is used to define the number format type.
 */
enum class NumberFormatType {
    /**
     * Default number format type, no conversion is done.
     */
    DEFAULT,

    /**
     * Converts the number in milliseconds to a date.
     */
    DATE,

    /**
     * Converts the number in milliseconds to a time.
     */
    TIME,

    /**
     * Converts the number in milliseconds to a date and time.
     */
    DATETIME,

    /**
     * Converts the number to a percentage.
     */
    PERCENTAGE,

    /**
     * Converts the number to a temperature.
     * The number is converted to a temperature unit based on the locale
     * or the settings configuration.
     */
    TEMPERATURE,

    /**
     * Converts the number to a distance.
     * The number is converted to a distance unit based on the locale
     * or the settings configuration.
     */
    DISTANCE,
}
