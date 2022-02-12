package com.infinitepower.newquiz.compose.data.local.question

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.compose.data.local.AppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class SavedQuestionDaoTest {
    private lateinit var database: AppDatabase

    private lateinit var savedQuestionDao: SavedQuestionDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        savedQuestionDao = database.savedQuestionDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertQuestionTest() = runTest {
        val question = getBasicQuestion()
        savedQuestionDao.insertQuestions(question)

        val allSavedQuestion = savedQuestionDao.getAllQuestions()
        assertThat(allSavedQuestion).contains(question)
    }
}