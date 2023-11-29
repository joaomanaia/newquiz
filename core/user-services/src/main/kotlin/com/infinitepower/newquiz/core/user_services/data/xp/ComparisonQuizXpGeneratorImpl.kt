package com.infinitepower.newquiz.core.user_services.data.xp

import com.infinitepower.newquiz.core.user_services.domain.xp.ComparisonQuizXpGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComparisonQuizXpGeneratorImpl @Inject constructor() : ComparisonQuizXpGenerator {
    override fun generateXp(endPosition: UInt): UInt = getDefaultXpForAnswer() * endPosition
}