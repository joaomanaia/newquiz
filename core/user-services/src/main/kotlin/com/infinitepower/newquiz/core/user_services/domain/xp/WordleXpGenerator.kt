package com.infinitepower.newquiz.core.user_services.domain.xp

interface WordleXpGenerator : XpGenerator {
    fun getDefaultXp(): UInt

    /**
     * Calculates the XP awarded to the player for completing a game of Wordle.
     *
     * Currently it does not take into account the difficulty of the game, because
     * the difficulty is not yet implemented in the game.
     *
     * The formula is:
     *
     *    xp = xpForDifficulty * (2 / sqrt(rowsUsed))
     *
     * @param rowsUsed The number of rows used in the game.
     * @return The XP awarded to the player.
     */
    fun generateXp(rowsUsed: UInt): UInt
}
