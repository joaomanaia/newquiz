package com.infinitepower.newquiz.core.util.kotlin

infix operator fun ULong.div(other: Float): Float = toLong() / other

infix operator fun ULong.div(other: Double): Double = toLong() / other
