package com.infinitepower.newquiz.core.testing.di

import com.infinitepower.newquiz.core.common.BaseApiUrls
import com.infinitepower.newquiz.core.di.KtorModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.headersOf
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [KtorModule::class]
)
object TestKtorModule {
    @Provides
    @Singleton
    fun providerKtorClient(): HttpClient = HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                when (request.url.encodedPath) {
                    "${BaseApiUrls.NEWQUIZ}/api/comparisonquiz/1" -> {
                        respond(
                            content = """
                                [
                                    {
                                        "title": "title1",
                                        "value": 1.0,
                                        "imgUrl": "imgUrl1"
                                    },
                                    {
                                        "title": "title2",
                                        "value": 2.0,
                                        "imgUrl": "imgUrl2"
                                    }
                                ]
                            """.trimIndent(),
                            headers = headersOf("Content-Type", "application/json")
                        )
                    }

                    else -> error("Unhandled ${request.url.encodedPath}")
                }
            }
        }
    }
}
