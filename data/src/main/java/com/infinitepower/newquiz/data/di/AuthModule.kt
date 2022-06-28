package com.infinitepower.newquiz.data.di

import com.infinitepower.newquiz.data.repository.AuthUserRepositoryImpl
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindAuthUserRepository(repository: AuthUserRepositoryImpl): AuthUserRepository
}