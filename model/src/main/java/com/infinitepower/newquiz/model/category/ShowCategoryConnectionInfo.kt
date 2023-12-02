package com.infinitepower.newquiz.model.category

import androidx.annotation.Keep

/**
 * Enum class that represents the possible values for the category connection info badge.
 * The badge is shown on the category card in the home screen.
 *
 * This is used in an entry of the settings screen.
 */
@Keep
enum class ShowCategoryConnectionInfo {
    /**
     * Don't show the badge.
     */
    NONE,

    /**
     * Show the badge if the category requires internet connection or not.
     */
    BOTH,

    /**
     * Show the badge only if the category requires internet connection.
     */
    REQUIRE_CONNECTION,

    /**
     * Show the badge only if the category doesn't require internet connection.
     */
    DONT_REQUIRE_CONNECTION;

    /**
     * Returns true if the badge should be shown for the category.
     *
     * @param requireInternetConnection Whether the category requires internet connection.
     * @return True if the badge should be shown for the category.
     */
    fun shouldShowBadge(
        requireInternetConnection: Boolean,
    ): Boolean {
        return when (this) {
            NONE -> false
            BOTH -> true
            REQUIRE_CONNECTION -> requireInternetConnection
            DONT_REQUIRE_CONNECTION -> !requireInternetConnection
        }
    }
}