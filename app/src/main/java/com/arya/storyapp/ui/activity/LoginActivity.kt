package com.arya.storyapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.arya.storyapp.R
import com.arya.storyapp.databinding.ActivityLoginBinding
import com.arya.storyapp.ui.viewmodel.LoginViewModel
import com.arya.storyapp.local.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()

    private val dataStoreManager: DataStoreManager by lazy {
        DataStoreManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener {
                val email = edLoginEmail.text.toString()
                val password = edLoginPassword.text.toString()
                viewModel.loginUser(email, password)
            }

            tvSignUp.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }

        viewModel.loginResult.observe(this) { loginResult ->
            if (loginResult != null) {
                lifecycleScope.launch {
                    dataStoreManager.saveToken(loginResult.token)
                    startMainActivity()
                }
            } else {
                val errorMessage = getString(R.string.wrong_username_or_password_message)
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
