package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.storyapp.remote.request.UserRegistrationRequest
import com.arya.storyapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean> get() = _registrationResult

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response =
                    authRepository.registerUser(UserRegistrationRequest(name, email, password))
                _registrationResult.value = response.isSuccessful
            } catch (e: Exception) {
                _registrationResult.value = false
            }
        }
    }
}
