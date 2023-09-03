package com.infinitepower.newquiz.online_services.di

import com.infinitepower.newquiz.online_services.data.user.auth.AuthUserRepositoryImpl
import com.infinitepower.newquiz.online_services.domain.user.auth.AuthUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindAuthUserRepository(impl: AuthUserRepositoryImpl): AuthUserRepository
}