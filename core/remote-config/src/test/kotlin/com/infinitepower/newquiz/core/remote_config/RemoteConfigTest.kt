package com.infinitepower.newquiz.core.remote_config

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class RemoteConfigTest {
    private lateinit var remoteConfig: RemoteConfig

    private val testValue1 = RemoteConfigValue<Int>("test_value_1")

    @Serializable
    private data class TestDataClass(
        val value1: Int,
        val value2: String
    )

    private val testData2 = TestDataClass(1, "test")
    private val testValue2 = RemoteConfigValue<TestDataClass>("test_value_2")

    private enum class TestEnum { A, B, C }
    private val testData3 = TestEnum.A
    private val testValue3 = RemoteConfigValue<TestEnum>("test_value_3")

    @Serializable
    private enum class TestEnum2 {
        @SerialName("a") A,
        @SerialName("b") B,
        @SerialName("c") C
    }
    private val testData4 = TestEnum2.A
    private val testValue4 = RemoteConfigValue<TestEnum2>("test_value_4")

    private data class TestDataClass2(val name: String)
    private val testValue5 = RemoteConfigValue<TestDataClass2>("test_value_5")


    @BeforeTest
    fun setUp() {
        println(Json.encodeToString(testData4))

        remoteConfig = LocalRemoteConfig(
            remoteConfigValues = mapOf(
                testValue1.key to "1",
                testValue2.key to Json.encodeToString(testData2),
                testValue3.key to testData3.name,
                testValue4.key to Json.encodeToString(testData4),
                testValue5.key to """{ "name": "test" }"""
            )
        )
        remoteConfig.initialize()
    }

    @Test
    fun `test get value`() {
        // Test for Int primitive type
        val value = remoteConfig.get(testValue1)
        assertThat(value).isEqualTo(1)

        // Test for custom class with serialization
        val value2 = remoteConfig.get(testValue2)
        assertThat(value2).isEqualTo(testData2)

        // When the enum class does not have serialization, it should be decoded using reflection
        val value3 = remoteConfig.get(testValue3)
        assertThat(value3).isEqualTo(testData3)

        // When enum class has serialization, it should be decoded using the deserialization method
        val value4 = remoteConfig.get(testValue4)
        assertThat(value4).isEqualTo(testData4)

        // When the class is not supported, an exception should be thrown
        val exception = assertThrows<IllegalArgumentException> {
            remoteConfig.get(testValue5)
        }
        assertThat(exception.message).isEqualTo("Unsupported type ${TestDataClass2::class}")
    }
}
