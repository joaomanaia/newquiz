package com.infinitepower.newquiz.data.repository.wordle

import android.content.Context
import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.data.R
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordleRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : WordleRepository {
    override suspend fun getAllWords(): Set<String> = withContext(Dispatchers.IO) {
        val wordleListInputStream = context.resources.openRawResource(R.raw.wordle_list_pt)

        try {
            wordleListInputStream
                .readBytes()
                .decodeToString()
                .split("\r\n", "\n")
                .filter { it.length == 5 }
                .map { it.uppercase().trim() }
                .shuffled()
                .toSet()
        } catch (e: Exception) {
            throw e
        } finally {
            wordleListInputStream.close()
        }
    }

    override  fun generateRandomWord(): FlowResource<String> = flow {
        try {
            emit(Resource.Loading())

            val allWords = getAllWords()
            val randomWord = allWords.random()

            emit(Resource.Success(randomWord))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "A error occurred while getting word"))
        }
    }
}