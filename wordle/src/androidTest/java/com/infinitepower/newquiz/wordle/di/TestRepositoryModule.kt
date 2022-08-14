package com.infinitepower.newquiz.wordle.di

import com.infinitepower.newquiz.data.di.RepositoryModule
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleRepository
import com.infinitepower.newquiz.wordle.data.repository.wordle.FakeWordleRepository
import com.infinitepower.newquiz.wordle.data.repository.wordle.daily.FakeDailyWordleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class TestRepositoryModule {
    @Binds
    abstract fun bindWordleRepository(wordleRepository: FakeWordleRepository): WordleRepository

    @Binds
    abstract fun bindDailyWordleRepository(dailyWordleRepository: FakeDailyWordleRepository): DailyWordleRepository
}