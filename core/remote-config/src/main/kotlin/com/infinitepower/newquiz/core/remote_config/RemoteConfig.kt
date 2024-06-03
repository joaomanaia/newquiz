package com.infinitepower.newquiz.core.remote_config

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

interface RemoteConfig {
    fun initialize(
        fetchInterval: Long = 3600L,
    )

    fun getString(key: String): String

    fun getLong(key: String): Long

    fun getInt(key: String): Int = getLong(key).toInt()

    fun getBoolean(key: String): Boolean
}

/**
 * Gets the value for the given [removeConfigValue] from the remote config.
 * The type of the value is inferred from the type of the [removeConfigValue].
 *
 * Supported types:
 * - [String]
 * - [Long]
 * - [Int]
 * - [Boolean]
 * - [Enum]
 * - Custom Class (must be annotated with [Serializable] annotation)
 *
 * @param removeConfigValue the value to get from the remote config.
 * @return the value for the given [removeConfigValue].
 * @throws IllegalArgumentException if the type of the [removeConfigValue] is not supported.
 */
inline fun <reified T> RemoteConfig.get(removeConfigValue: RemoteConfigValue<T>): T {
    return when (T::class) {
        String::class -> getString(removeConfigValue.key) as T
        Long::class -> getLong(removeConfigValue.key) as T
        Int::class -> getInt(removeConfigValue.key) as T
        Boolean::class -> getBoolean(removeConfigValue.key) as T
        else -> {
            // Check if the type is a custom class with serialization, if so, decode the serialized value.
            // If the type is an enum with serialization, decode the enum value using the deserialization.
            if (T::class.java.isAnnotationPresent(Serializable::class.java)) {
                val serializedValue = getString(removeConfigValue.key)
                Json.decodeFromString(serializedValue)
            } else if (T::class.java.isEnum) {
                // If the type is an enum without serialization, decode the enum value using reflection.
                val enumValue = getString(removeConfigValue.key)

                T::class.java.enumConstants
                    ?.find { it.toString() == enumValue }
                    ?: throw IllegalArgumentException("Invalid enum value: $enumValue")
            } else {
                throw IllegalArgumentException("Unsupported type ${T::class}")
            }
        }
    }
}
