package com.infinitepower.newquiz.online_services.di

import com.infinitepower.newquiz.online_services.data.UserApiImpl
import com.infinitepower.newquiz.online_services.data.UserRepositoryImpl
import com.infinitepower.newquiz.online_services.data.game.xp.MultiChoiceQuizXPRepositoryImpl
import com.infinitepower.newquiz.online_services.data.game.xp.WordleXpRepositoryImpl
import com.infinitepower.newquiz.online_services.domain.game.xp.MultiChoiceQuizXPRepository
import com.infinitepower.newquiz.online_services.domain.game.xp.WordleXpRepository
import com.infinitepower.newquiz.online_services.domain.user.UserApi
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    internal abstract fun bindUserApi(
        userApiImpl: UserApiImpl
    ): UserApi

    @Binds
    abstract fun bindMultiChoiceQuizXPRepository(
        multiChoiceQuizXPRepositoryImpl: MultiChoiceQuizXPRepositoryImpl
    ): MultiChoiceQuizXPRepository

    @Binds
    abstract fun bindWordleXPRepository(
        wordleXpRepositoryImpl: WordleXpRepositoryImpl
    ): WordleXpRepository
}