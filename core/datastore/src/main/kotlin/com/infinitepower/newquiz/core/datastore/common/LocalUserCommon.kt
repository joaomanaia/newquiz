package com.infinitepower.newquiz.core.datastore.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.infinitepower.newquiz.core.datastore.PreferenceRequest
import java.util.UUID

val Context.localUserDataStore: DataStore<Preferences> by preferencesDataStore(name = "local_user")

object LocalUserCommon {
    object UserUid : PreferenceRequest<String>(
        key = stringPreferencesKey("uid"),
        defaultValue = UUID.randomUUID().toString()
    )

    object UserTotalXp : PreferenceRequest<Long>(
        key = longPreferencesKey("totalXp"),
        defaultValue = 0
    )

    data class UserDiamonds(
        val initialDiamonds: Int = 0,
    ) : PreferenceRequest<Int>(
        key = intPreferencesKey("diamonds"),
        defaultValue = initialDiamonds
    )
}
