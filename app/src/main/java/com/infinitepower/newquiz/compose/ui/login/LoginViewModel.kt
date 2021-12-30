package com.infinitepower.newquiz.compose.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import com.infinitepower.newquiz.compose.data.remote.user.User
import com.infinitepower.newquiz.compose.data.remote.user.UserApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUserApi: AuthUserApi,
    private val userApi: UserApi
) : ViewModel() {
    fun updateAuthNewUser() = viewModelScope.launch(Dispatchers.IO) {
        try {
            authUserApi.updateAuthNewUser()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun createNewUser() = viewModelScope.launch(Dispatchers.IO) {
        val user = User(
            uid = authUserApi.getUid(),
            info = User.UserInfo(
                fullname = authUserApi.getName(),
                imageUrl = authUserApi.getPhotoUrl()?.toString()
            )
        )

        userApi.createUser(user)
    }
}