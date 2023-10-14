package com.arya.storyapp.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.arya.storyapp.api.AuthService
import com.arya.storyapp.api.UserLoginResponse
import com.arya.storyapp.databinding.ActivityRegisterBinding
import com.arya.storyapp.ui.viewmodel.RegisterViewModel
import com.arya.storyapp.ui.viewmodel.RegisterViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        viewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            val registrationCall = viewModel.registerUser(name, email, password)
            registrationCall.enqueue(object : Callback<UserLoginResponse> {
                override fun onResponse(
                    call: Call<UserLoginResponse>,
                    response: Response<UserLoginResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Oops! It looks like that email is already taken. Try another one and let's get you signed up!\" \uD83D\uDE04",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration failed. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}
