package com.infinitepower.newquiz.model.config

interface RemoteConfigApi {
    fun getString(key: String): String

    fun getLong(key: String): Long

    fun getInt(key: String): Int = getLong(key).toInt()

    fun getBoolean(key: String): Boolean

    /**
     * Gets the base url for the flag images for the given country code, using the remote config.
     * The url should be in the format: https://flagapi.example/svg/%code%
     * @param countryCode the country code to get the flag image for.
     * @return the base url for the flag images.
     */
    fun getFlagUrl(countryCode: String): String {
        val remoteConfigBaseUrl = getString("flag_base_url")
        return remoteConfigBaseUrl.replace("%code%", countryCode.lowercase())
    }
}