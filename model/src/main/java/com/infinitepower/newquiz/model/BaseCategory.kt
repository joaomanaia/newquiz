package com.infinitepower.newquiz.model

interface BaseCategory {
    val id: String

    val name: UiText

    val image: Any

    val requireInternetConnection: Boolean
}
