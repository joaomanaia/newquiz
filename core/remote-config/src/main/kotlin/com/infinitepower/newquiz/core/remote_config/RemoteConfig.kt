package com.infinitepower.newquiz.core.remote_config

import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizHelperValueState
import com.infinitepower.newquiz.model.question.QuestionDifficulty

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
 * - [ShowCategoryConnectionInfo]
 * - [ComparisonQuizHelperValueState]
 * - [QuestionDifficulty]
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
        ShowCategoryConnectionInfo::class -> ShowCategoryConnectionInfo.valueOf(getString(removeConfigValue.key)) as T
        ComparisonQuizHelperValueState::class -> ComparisonQuizHelperValueState.valueOf(getString(removeConfigValue.key)) as T
        QuestionDifficulty::class -> QuestionDifficulty.from(getString(removeConfigValue.key)) as T
        else -> throw IllegalArgumentException("Unsupported type ${T::class}")
    }
}
