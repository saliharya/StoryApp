package com.arya.storyapp.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.arya.storyapp.databinding.ActivityRegisterBinding
import com.arya.storyapp.ui.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            viewModel.registerUser(
                binding.edRegisterName.text.toString(),
                binding.edRegisterEmail.text.toString(),
                binding.edRegisterPassword.text.toString()
            )
        }

        viewModel.registrationResult.observe(this) { isSuccessful ->
            Toast.makeText(
                this,
                if (isSuccessful) "Registration successful" else "Oops! It looks like that email is already taken. Try another one and let's get you signed up!\" \uD83D\uDE04",
                Toast.LENGTH_SHORT
            ).show()

            if (isSuccessful) finish()
        }
    }
}
