package com.infinitepower.newquiz.core.testing.di

import com.infinitepower.newquiz.core.testing.data.online_services.repository.FakeLoginCoreImpl
import com.infinitepower.newquiz.core.testing.data.online_services.repository.FakeUserApiImpl
import com.infinitepower.newquiz.online_services.core.login.LoginCore
import com.infinitepower.newquiz.online_services.data.game.xp.MultiChoiceQuizXPRepositoryImpl
import com.infinitepower.newquiz.online_services.data.game.xp.WordleXpRepositoryImpl
import com.infinitepower.newquiz.online_services.data.user.UserRepositoryImpl
import com.infinitepower.newquiz.online_services.di.RepositoryModule
import com.infinitepower.newquiz.online_services.domain.game.xp.MultiChoiceQuizXPRepository
import com.infinitepower.newquiz.online_services.domain.game.xp.WordleXpRepository
import com.infinitepower.newquiz.online_services.domain.user.UserApi
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class TestOnlineServicesModule {
    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindUserApi(userApiImpl: FakeUserApiImpl): UserApi

    @Binds
    abstract fun bindMultiChoiceQuizXPRepository(impl: MultiChoiceQuizXPRepositoryImpl): MultiChoiceQuizXPRepository

    @Binds
    abstract fun bindWordleXPRepository(impl: WordleXpRepositoryImpl): WordleXpRepository

    @Binds
    abstract fun bindLoginCore(loginCoreImpl: FakeLoginCoreImpl): LoginCore
}
