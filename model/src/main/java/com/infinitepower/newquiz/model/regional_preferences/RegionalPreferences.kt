package com.infinitepower.newquiz.model.regional_preferences

import java.util.Locale

/** *
 * @param locale The locale of the user.
 */
data class RegionalPreferences(
    val locale: Locale = Locale.getDefault(),
    val temperatureUnit: TemperatureUnit? = null,
    val distanceUnitType: DistanceUnitType? = null,
)
