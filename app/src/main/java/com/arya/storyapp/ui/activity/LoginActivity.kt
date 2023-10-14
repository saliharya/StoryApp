package com.arya.storyapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arya.storyapp.api.AuthService
import com.arya.storyapp.databinding.ActivityLoginBinding
import com.arya.storyapp.ui.viewmodel.LoginViewModel
import com.arya.storyapp.ui.viewmodel.LoginViewModelFactory
import com.arya.storyapp.util.SessionManager
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
        val sessionManager = SessionManager(this)

        val factory = LoginViewModelFactory(authService, sessionManager)
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.loginUser(email, password)
        }

        viewModel.loginResult.observe(this, Observer {
            if (it != null) {
                // Login successful, navigate to next activity
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
        })

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
