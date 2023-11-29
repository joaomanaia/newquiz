package com.infinitepower.newquiz.core.user_services.domain.xp

interface ComparisonQuizXpGenerator : XpGenerator {
    fun getDefaultXpForAnswer(): UInt = 10u

    fun generateXp(endPosition: UInt): UInt
}