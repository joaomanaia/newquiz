@file:Suppress("unused")
package com.infinitepower.newquiz.online_services.di

import com.infinitepower.newquiz.online_services.core.login.LoginCore
import com.infinitepower.newquiz.online_services.core.login.LoginCoreImpl
import com.infinitepower.newquiz.online_services.data.game.xp.WordleXpRepositoryImpl
import com.infinitepower.newquiz.online_services.data.user.FirestoreUserApiImpl
import com.infinitepower.newquiz.online_services.data.user.UserRepositoryImpl
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
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindUserApi(userApiImpl: FirestoreUserApiImpl): UserApi

    @Binds
    abstract fun bindWordleXPRepository(wordleXpRepositoryImpl: WordleXpRepositoryImpl): WordleXpRepository

    @Binds
    abstract fun bindLoginCore(loginCoreImpl: LoginCoreImpl): LoginCore
}