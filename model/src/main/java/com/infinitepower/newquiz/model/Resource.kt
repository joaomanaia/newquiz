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
}

typealias FlowResource<T> = Flow<Resource<T>>
