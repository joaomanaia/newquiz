package com.infinitepower.newquiz.online_services.core.util

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

fun isGooglePlayServicesAvailable(context: Context): Boolean {
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    val status = googleApiAvailability.isGooglePlayServicesAvailable(context)
    return status == ConnectionResult.SUCCESS
}
