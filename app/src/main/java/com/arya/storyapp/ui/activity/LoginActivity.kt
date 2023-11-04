package com.arya.storyapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.arya.storyapp.R
import com.arya.storyapp.databinding.ActivityLoginBinding
import com.arya.storyapp.local.DataStoreManager
import com.arya.storyapp.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()
    private val dataStoreManager: DataStoreManager by lazy { DataStoreManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupClickListeners()
        observeLoginResult()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.loginUser(email, password)
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(this) { loginResult ->
            if (loginResult != null) {
                saveTokenAndStartMainActivity(loginResult.token)
            } else {
                showErrorMessage()
            }
        }
    }

    private fun saveTokenAndStartMainActivity(token: String) {
        lifecycleScope.launch {
            dataStoreManager.saveToken(token)
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showErrorMessage() {
        val errorMessage = getString(R.string.wrong_username_or_password_message)
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}
