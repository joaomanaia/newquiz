package com.infinitepower.newquiz.core.util.kotlin

import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.roundToUInt(): UInt = roundToInt().toUInt()

infix fun UInt.pow(n: Int): UInt = toDouble().pow(n).toUInt()
