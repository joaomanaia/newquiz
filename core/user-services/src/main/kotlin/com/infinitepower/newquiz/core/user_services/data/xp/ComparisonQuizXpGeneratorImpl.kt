package com.infinitepower.newquiz.core.user_services.data.xp

import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.user_services.domain.xp.ComparisonQuizXpGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComparisonQuizXpGeneratorImpl @Inject constructor(
    private val remoteConfig: RemoteConfig
) : ComparisonQuizXpGenerator {
    override fun getDefaultXpForAnswer(): UInt {
        return remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_DEFAULT_XP_REWARD).toUInt()
    }

    override fun generateXp(endPosition: UInt): UInt = getDefaultXpForAnswer() * endPosition
}