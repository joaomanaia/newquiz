package com.infinitepower.newquiz

import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class NewQuizTestApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Firebase.initialize(this)
    }
}