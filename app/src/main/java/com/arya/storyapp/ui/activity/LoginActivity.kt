package com.arya.storyapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.arya.storyapp.api.AuthService
import com.arya.storyapp.databinding.ActivityLoginBinding
import com.arya.storyapp.ui.viewmodel.LoginViewModel
import com.arya.storyapp.ui.viewmodel.LoginViewModelFactory
import com.arya.storyapp.util.DataStoreManager
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authService = retrofit.create(AuthService::class.java)
        val dataStoreManager = DataStoreManager(this)

        val factory = LoginViewModelFactory(authService, dataStoreManager)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.loginUser(email, password)
        }

        viewModel.loginResult.observe(this) { loginResult ->
            if (loginResult != null) {
                // Login successful, navigate to next activity
                lifecycleScope.launch {
                    dataStoreManager.saveToken(loginResult.token)
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Show error message
                Toast.makeText(
                    this,
                    "Oops! Did you mistype your username or password, or is your cat walking on the keyboard again? \uD83D\uDE40",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
