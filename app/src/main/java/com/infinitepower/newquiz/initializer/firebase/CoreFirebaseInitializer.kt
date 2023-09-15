package com.infinitepower.newquiz.initializer.firebase


import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class CoreFirebaseInitializer : Initializer<FirebaseApp> {
    override fun create(context: Context): FirebaseApp {
        return Firebase.initialize(context) ?: throw IllegalStateException("FirebaseApp is null")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}