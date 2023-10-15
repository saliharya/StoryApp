package com.arya.storyapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.arya.storyapp.databinding.ActivityLoginBinding
import com.arya.storyapp.ui.viewmodel.LoginViewModel
import com.arya.storyapp.util.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var dataStoreManager: DataStoreManager

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
                lifecycleScope.launch { dataStoreManager.saveToken(loginResult.token) }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Oops! Did you mistype your username or password, or is your cat walking on the keyboard again? \uD83D\uDE40",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
