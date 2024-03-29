package com.infinitepower.newquiz.core.util

import android.net.Uri
import java.net.URI

fun URI.toAndroidUri(): Uri = Uri.parse(this.toString())

fun Uri.toJavaURI(): URI = URI.create(this.toString())

fun emptyJavaURI(): URI = URI.create("")
