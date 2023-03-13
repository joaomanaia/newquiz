package com.infinitepower.newquiz.core.util.base_urls

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

/**
 * Gets the base url for the flag images for the given country code, using the remote config.
 * The url should be in the format: https://countryflagsapi.com/svg/%code%
 * @param countryCode the country code to get the flag image for.
 * @return the base url for the flag images.
 */
fun FirebaseRemoteConfig.getFlagBaseUrl(countryCode: String): String {
    val remoteConfigBaseUrl = getString("flag_base_url")
    return remoteConfigBaseUrl.replace("%code%", countryCode.lowercase())
}
