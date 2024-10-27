package com.infinitepower.newquiz.core.testing.utils

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.datetime.Clock

fun mockAndroidLog() {
    mockkStatic(Log::class)

    every { Log.d(any(), any()) } answers {
        println("${Clock.System.now()} DEBUG: ${args[0]} - ${args[1]}")
        0
    }

    every { Log.e(any(), any()) } answers {
        println("${Clock.System.now()} ERROR: ${args[0]} - ${args[1]}")
        0
    }

    every { Log.i(any(), any()) } answers {
        println("${Clock.System.now()} INFO: ${args[0]} - ${args[1]}")
        0
    }

    every { Log.v(any(), any()) } answers {
        println("${Clock.System.now()} VERBOSE: ${args[0]} - ${args[1]}")
        0
    }

    every { Log.w(any(), any<String>()) } answers {
        println("${Clock.System.now()} WARN: ${args[0]} - ${args[1]}")
        0
    }
}
