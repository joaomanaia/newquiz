package com.infinitepower.newquiz.compose.core.firebase

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase

class EmulatorSuite {
    fun enableAll() {
        enableAuthEmulator()
        enableFirestoreEmulator()
    }

    private fun enableAuthEmulator() {
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        AuthUI.getInstance().useEmulator("10.0.2.2", 9099)
    }

    private fun enableFirestoreEmulator() {
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        val firestore = Firebase.firestore
        firestore.useEmulator("10.0.2.2", 8080)

        firestore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = false
        }
    }
}