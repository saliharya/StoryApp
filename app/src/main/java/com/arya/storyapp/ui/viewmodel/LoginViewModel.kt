package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.storyapp.api.AuthService
import com.arya.storyapp.api.LoginResult
import com.arya.storyapp.api.UserLoginRequest
import com.arya.storyapp.api.UserLoginResponse
import com.arya.storyapp.util.DataStoreManager
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
    private val authService: AuthService,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    val loginResult = MutableLiveData<LoginResult?>()

    fun loginUser(email: String, password: String) {
        val request = UserLoginRequest(email, password)
        authService.loginUser(request).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(
                call: Call<UserLoginResponse>,
                response: Response<UserLoginResponse>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if ((loginResponse != null) && !loginResponse.error) {
                        loginResult.value = loginResponse.loginResult
                        viewModelScope.launch {
                            dataStoreManager.saveToken(loginResponse.loginResult?.token ?: "")
                        }
                    } else {
                        loginResult.value = null
                    }
                } else {
                    loginResult.value = null
                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                loginResult.value = null
            }
        })
    }
}
