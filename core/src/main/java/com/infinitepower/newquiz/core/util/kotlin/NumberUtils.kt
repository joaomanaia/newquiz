package com.infinitepower.newquiz.core.util.kotlin

fun Int.toDoubleDigit(): String = String.format("%02d", this)

operator fun UInt.times(multiplierFactor: Float): UInt {
    return this.toInt().times(multiplierFactor).toUInt()
}

operator fun UIntRange.times(multiplierFactor: Float): UIntRange {
    return this.first * multiplierFactor..this.last * multiplierFactor
}
