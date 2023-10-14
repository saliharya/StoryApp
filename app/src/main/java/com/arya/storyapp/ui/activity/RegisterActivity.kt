package com.arya.storyapp.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.arya.storyapp.api.AuthService
import com.arya.storyapp.databinding.ActivityRegisterBinding
import com.arya.storyapp.ui.viewmodel.RegisterViewModel
import com.arya.storyapp.ui.viewmodel.RegisterViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authService = retrofit.create(AuthService::class.java)

        val factory = RegisterViewModelFactory(authService)
        viewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            viewModel.registerUser(name, email, password)
        }

        viewModel.registrationResult.observe(this) { isSuccessful ->
            if (!isSuccessful) {
                Toast.makeText(
                    this,
                    "Oops! It looks like that email is already taken. Try another one and let's get you signed up!\" \uD83D\uDE04",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Registration successful",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}
