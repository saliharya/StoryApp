package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.storyapp.local.DataStoreManager
import com.arya.storyapp.remote.request.UserLoginRequest
import com.arya.storyapp.remote.response.LoginResult
import com.arya.storyapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResult?>()
    val loginResult: LiveData<LoginResult?> get() = _loginResult

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.loginUser(UserLoginRequest(email, password))

                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    if (loginResponse?.error == false) {
                        val result = loginResponse.loginResult

                        if (result != null) {
                            _loginResult.value = result
                            dataStoreManager.saveToken(result.token)
                            return@launch
                        }
                    }
                }

                _loginResult.value = null
            } catch (e: Exception) {
                _loginResult.value = null
            }
        }
    }
}
