package com.infinitepower.newquiz.core.testing.di

import com.infinitepower.newquiz.core.testing.data.online_services.repository.FakeAuthUserRepository
import com.infinitepower.newquiz.online_services.di.AuthModule
import com.infinitepower.newquiz.online_services.domain.user.auth.AuthUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AuthModule::class]
)
abstract class TestAuthModule {
    @Binds
    abstract fun bindAuthUserRepository(impl: FakeAuthUserRepository): AuthUserRepository
}
