package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.storyapp.api.AuthService
import com.arya.storyapp.api.LoginResult
import com.arya.storyapp.api.UserLoginRequest
import com.arya.storyapp.util.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    val loginResult = MutableLiveData<LoginResult?>()

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authService.loginUser(UserLoginRequest(email, password))
                response.body()?.let { loginResponse ->
                    if (response.isSuccessful && !loginResponse.error) {
                        loginResult.value = loginResponse.loginResult
                        dataStoreManager.saveToken(loginResponse.loginResult?.token ?: "")
                    } else {
                        loginResult.value = null
                    }
                } ?: run { loginResult.value = null }
            } catch (e: Exception) {
                loginResult.value = null
            }
        }
    }
}