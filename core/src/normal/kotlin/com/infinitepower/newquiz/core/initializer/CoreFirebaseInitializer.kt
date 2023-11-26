package com.infinitepower.newquiz.core.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.google.firebase.FirebaseApp
import com.google.firebase.Firebase
import com.google.firebase.initialize

class CoreFirebaseInitializer : Initializer<FirebaseApp> {
    private companion object {
        private const val TAG = "CoreFirebaseInitializer"
    }

    override fun create(context: Context): FirebaseApp {
        Log.d(TAG, "Initializing Firebase")
        return Firebase.initialize(context) ?: throw IllegalStateException("FirebaseApp is null")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}