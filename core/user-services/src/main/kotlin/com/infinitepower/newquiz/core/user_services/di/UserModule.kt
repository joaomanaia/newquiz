package com.infinitepower.newquiz.core.user_services.di

import com.infinitepower.newquiz.core.user_services.LocalUserServiceImpl
import com.infinitepower.newquiz.core.user_services.UserService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {
    @Binds
    @Singleton
    abstract fun bindUserService(
        impl: LocalUserServiceImpl
    ): UserService
}
