package com.arya.storyapp.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.arya.storyapp.R
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
            val message = if (isSuccessful) {
                getString(R.string.registration_success)
            } else {
                getString(R.string.registration_failure)
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            if (isSuccessful) finish()
        }
    }
}
