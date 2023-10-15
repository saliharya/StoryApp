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
            runCatching {
                authService.loginUser(UserLoginRequest(email, password))
            }.onSuccess { response ->
                response.body()?.let { loginResponse ->
                    if (response.isSuccessful && !loginResponse.error) {
                        loginResponse.loginResult?.let { result ->
                            loginResult.value = result
                            dataStoreManager.saveToken(result.token)
                        } ?: run { loginResult.value = null }
                    } else {
                        loginResult.value = null
                    }
                } ?: run { loginResult.value = null }
            }.onFailure {
                loginResult.value = null
            }
        }
    }
}
