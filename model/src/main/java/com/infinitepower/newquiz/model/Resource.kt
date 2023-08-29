package com.infinitepower.newquiz.model

import kotlinx.coroutines.flow.Flow

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)

    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    class Loading<T>(data: T? = null) : Resource<T>(data)

    override fun toString(): String {
        return when (this) {
            is Success -> "Success with data: $data"
            is Error -> "Error: $message, with data: $data"
            is Loading -> "Loading with data $data"
        }
    }

    fun isSuccess(): Boolean = this is Success

    fun isLoading(): Boolean = this is Loading

    fun isError(): Boolean = this is Error

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Resource<*>) return false

        return this.data == other.data && this.message == other.message
    }

    override fun hashCode(): Int {
        var result = data?.hashCode() ?: 0
        result = 31 * result + (message?.hashCode() ?: 0)
        return result
    }
}

typealias FlowResource<T> = Flow<Resource<T>>
