package com.infinitepower.newquiz.core.user_services.di

import com.infinitepower.newquiz.core.user_services.data.xp.MultiChoiceQuizXpGeneratorImpl
import com.infinitepower.newquiz.core.user_services.data.xp.WordleXpGeneratorImpl
import com.infinitepower.newquiz.core.user_services.domain.xp.MultiChoiceQuizXpGenerator
import com.infinitepower.newquiz.core.user_services.domain.xp.WordleXpGenerator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class XpGeneratorsModule {
    @Binds
    @Singleton
    abstract fun bindMultiChoiceQuizXpGenerator(impl: MultiChoiceQuizXpGeneratorImpl): MultiChoiceQuizXpGenerator

    @Binds
    @Singleton
    abstract fun bindWordleXpGenerator(impl: WordleXpGeneratorImpl): WordleXpGenerator
}