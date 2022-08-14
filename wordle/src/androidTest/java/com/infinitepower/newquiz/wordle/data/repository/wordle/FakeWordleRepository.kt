package com.infinitepower.newquiz.wordle.data.repository.wordle

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeWordleRepository @Inject constructor() : WordleRepository {
    private val allWords = setOf("TEST")

    override suspend fun getAllWords(): Set<String> = allWords

    override fun generateRandomWord(): FlowResource<String> = flow {
        try {
            emit(Resource.Loading())

            val allWords = getAllWords()
            emit(Resource.Success(allWords.random()))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "A error occurred while getting word"))
        }
    }
}