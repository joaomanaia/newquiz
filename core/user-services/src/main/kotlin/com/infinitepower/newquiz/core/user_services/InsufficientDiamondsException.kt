package com.infinitepower.newquiz.core.user_services

class InsufficientDiamondsException(diamondsNeeded: UInt, diamondsAvailable: UInt) : Exception(
    "Not enough diamonds. Need $diamondsNeeded, but only $diamondsAvailable are available."
)
