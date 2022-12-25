package com.infinitepower.newquiz.core.math.evaluator.internal

import java.math.BigDecimal

abstract class Function {
    abstract fun call(arguments: List<BigDecimal>): BigDecimal
}