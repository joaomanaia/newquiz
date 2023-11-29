package com.infinitepower.newquiz.core.user_services.domain.xp

interface ComparisonQuizXpGenerator : XpGenerator {
    fun getDefaultXpForAnswer(): UInt

    fun generateXp(endPosition: UInt): UInt
}