package com.infinitepower.newquiz.initializer.firebase


import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.ktx.initialize

object CoreFirebaseInitializer : Initializer<FirebaseApp> {
    override fun create(context: Context): FirebaseApp {
        Firebase.initialize(context)

        return Firebase.app
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}