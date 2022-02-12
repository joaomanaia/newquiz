package com.infinitepower.newquiz.compose.core

const val USERS_COLLECTION = "users"

object FirebaseCommon {
    object Users {
        object UserData {
            const val LEVEL_FIELD = "data.level"

            object UserXp {
                const val XP_FIELD = "data.userXp.xp"
                const val TOTAL_XP_FIELD = "data.userXp.totalXp"
            }
        }
    }
}